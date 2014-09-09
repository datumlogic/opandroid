/*******************************************************************************
 *
 *  Copyright (c) 2014 , Hookflash Inc.
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 *  ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those
 *  of the authors and should not be interpreted as representing official policies,
 *  either expressed or implied, of the FreeBSD Project.
 *******************************************************************************/
package com.openpeer.sample.conversation;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.openpeer.javaapi.AccountStates;
import com.openpeer.javaapi.CallStates;
import com.openpeer.javaapi.ComposingStates;
import com.openpeer.javaapi.OPCall;
import com.openpeer.javaapi.OPContact;
import com.openpeer.javaapi.OPMessage;
import com.openpeer.javaapi.OPMessage.OPMessageType;
import com.openpeer.sample.BaseActivity;
import com.openpeer.sample.BaseFragment;
import com.openpeer.sample.BuildConfig;
import com.openpeer.sample.IntentData;
import com.openpeer.sample.OPNotificationBuilder;
import com.openpeer.sample.OPSessionManager;
import com.openpeer.sample.R;
import com.openpeer.sample.contacts.ProfilePickerActivity;
import com.openpeer.sample.util.DateFormatUtils;
import com.openpeer.sdk.app.OPDataManager;
import com.openpeer.sdk.datastore.DatabaseContracts;
import com.openpeer.sdk.datastore.DatabaseContracts.MessageEntry;
import com.openpeer.sdk.datastore.OPContentProvider;
import com.openpeer.sdk.model.MessageState;
import com.openpeer.sdk.model.OPSession;
import com.openpeer.sdk.model.OPUser;
import com.openpeer.sdk.model.SessionListener;
import com.openpeer.sdk.utils.NoDuplicateArrayList;
import com.openpeer.sdk.utils.OPModelUtils;

public class ChatFragment extends BaseFragment implements
        LoaderManager.LoaderCallbacks<Cursor>, SessionListener {

    private static final int DEFAULT_NUM_MESSAGES_TO_LOAD = 30;
    private static final DateFormat dateFormat = DateFormat
            .getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
    private static final String TAG = ChatFragment.class.getSimpleName();
    private ListView mMessagesList;
    private TextView mComposeBox;
    private View mSendButton;
    private MessagesAdaptor mAdapter;

    private long mWindowId;
    private OPSession mSession;
    long[] mUserIDs;
    List<OPUser> participants;
    private CallInfoView mCallInfoView;
    boolean mTyping;
    private OPMessage mEditingMessage;

    public static ChatFragment newInstance(long[] userIdList) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putLongArray(IntentData.ARG_PEER_USER_IDS, userIdList);
        fragment.setArguments(args);
        return fragment;
    }

    public static ChatFragment newInstance(String peerContactId) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(IntentData.ARG_PEER_CONTACT_ID, peerContactId);
        fragment.setArguments(args);
        return fragment;
    }

    public static ChatFragment newTestInstance() {
        ChatFragment fragment = new ChatFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            Bundle args = getArguments();
            mUserIDs = args.getLongArray(IntentData.ARG_PEER_USER_IDS);
        } else {
            mUserIDs = savedInstanceState
                    .getLongArray(IntentData.ARG_PEER_USER_IDS);
        }
        participants = OPDataManager.getDatastoreDelegate().getUsers(mUserIDs);

        mWindowId = OPModelUtils.getWindowId(mUserIDs);
        OPNotificationBuilder.cancelNotificationForChat((int) mWindowId);
        // mSelfContact = OPDataManager.getInstance().getSelfContacts().get(0);
        this.setHasOptionsMenu(true);
        // TODO:remove this call and use lazy loading.
        getSession();
    }

    /**
     * Lazy creating session. Call this when user sends message
     *
     * @return
     */
    private OPSession getSession() {
        mSession = OPSessionManager.getInstance().getSessionForUsers(mUserIDs);
        if (mSession == null) {
            // this is user intiiated session
            mSession = new OPSession(participants);
            OPSessionManager.getInstance().addSession(mSession);
        }
        return mSession;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.fragment_chat, null);
        return setupView(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        mSession.setWindowAttached(true);
        mSession.registerListener(this);
        OPDataManager.getDatastoreDelegate().markMessagesRead(mWindowId);

        // All following stuff can only be done if the account is in ready state

        // TODO: proper look up
        String peerUri = mSession.getParticipants().get(0).getPeerUri();
        // OPCall call = mSession.getCurrentCall();
        OPCall call = OPSessionManager.getInstance().getOngoingCallForPeer(
                peerUri);
        if (call != null && (call.getState() == CallStates.CallState_Open
                || call.getState() == CallStates.CallState_Active)) {
            Log.d(TAG, "now show call info");
            mCallInfoView.setVisibility(View.VISIBLE);
            mCallInfoView.bindCall(call);

        } else {
            mCallInfoView.setVisibility(View.GONE);
        }
        
        if (!OPDataManager.getInstance().isAccountReady()) {
            return;
        }
        mSession.getThread().setStatusInThread(
                ComposingStates.ComposingState_Active);

    }

    @Override
    public void onPause() {
        super.onPause();
        mSession.setWindowAttached(false);
        mSession.unregisterListener(this);
        if (mCallInfoView.isShown()) {
            mCallInfoView.unbind();
        }
        if (!OPDataManager.getInstance().isAccountReady()) {
            return;
        }
        mSession.getThread().setStatusInThread(
                ComposingStates.ComposingState_Inactive);
    }

    void updateUsersView(List<OPUser> users) {
        getActivity().getActionBar().setTitle(users.get(0).getName());
    }

    View setupView(View view) {
        mCallInfoView = (CallInfoView) view.findViewById(R.id.call_info);
        View emptyView = view.findViewById(R.id.empty_view);
        mMessagesList = (ListView) view.findViewById(R.id.listview);
        mMessagesList.setEmptyView(emptyView);

        registerForContextMenu(mMessagesList);
        mMessagesList
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int arg2,
                                            long arg3) {
                        if (arg1 instanceof SelfMessageView
                                && ((SelfMessageView) arg1).canEditMessage()) {
                            mEditingMessage = ((SelfMessageView) arg1)
                                    .getMessage();

                            mComposeBox.setText(mEditingMessage.getMessage());
                        }
                    }
                });
        mAdapter = new MessagesAdaptor(getActivity(), null);
        mMessagesList.setAdapter(mAdapter);
        View layout = view.findViewById(R.id.layout_compose);
        mComposeBox = (TextView) layout.findViewById(R.id.text);
        mSendButton = layout.findViewById(R.id.send);

        updateUsersView(mSession.getParticipants());
        mSendButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!OPDataManager.getInstance().isAccountReady()) {
                    BaseActivity.showInvalidStateWarning(getActivity());
                    return;
                }
                Log.d("TODO", "call actual send function");
                if (mComposeBox.getText() == null
                        || mComposeBox.getText().length() == 0) {
                    return;
                }
                OPMessage msg = null;
                // we use 0 for home user
                msg = new OPMessage(0,
                        OPMessageType.TYPE_TEXT,
                        mComposeBox.getText().toString(),
                        System.currentTimeMillis(),
                        OPMessage.generateUniqueId());

                if (mEditingMessage != null) {
                    msg.setReplacesMessageId(mEditingMessage.getMessageId());
                    mEditingMessage = null;
                }
                mComposeBox.setText("");

                getSession().sendMessage(msg, false);
                mTyping = false;
                mSession.getThread().setStatusInThread(
                        ComposingStates.ComposingState_Paused);

            }
        });

        mComposeBox.addTextChangedListener(new TextWatcher() {
            Timer pauseTimer;// = new Timer();
            long PAUSE_DELAY = 30 * 1000;
            TimerTask task;

            @Override
            public void afterTextChanged(Editable arg0) {
                pauseTimer = new Timer();
                task = new TimerTask() {

                    @Override
                    public void run() {
                        mTyping = false;
                        mSession.getThread().setStatusInThread(
                                ComposingStates.ComposingState_Paused);
                    }

                };
                pauseTimer.schedule(task, PAUSE_DELAY);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                    int arg2, int arg3) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {
                if (pauseTimer != null) {

                    pauseTimer.cancel();
                    pauseTimer = null;
                    task = null;
                }
                if (!mTyping) {
                    mTyping = true;

                    mSession.getThread().setStatusInThread(
                            ComposingStates.ComposingState_Composing);
                }
            }

        });

        getLoaderManager().initLoader(URL_LOADER, null, this);

        return view;
    }

    class MessagesAdaptor extends CursorAdapter {
        private final static int VIEWTYPE_SELF_MESSAGE_VIEW = 0;
        private final static int VIEWTYPE_RECIEVED_MESSAGE_VIEW = 1;
        private final static int VIEWTYPE_STATUS_VIEW = 2;
        private NoDuplicateArrayList<OPUser> composingStates = new NoDuplicateArrayList<OPUser>();

        private boolean isStatus(int position) {
            return position > super.getCount() - 1;
        }

        public MessagesAdaptor(Context context, Cursor c) {
            super(context, c);
        }

        public void notifyDataSetChanged(ComposingStates state, OPUser contact) {
            if (state == ComposingStates.ComposingState_Composing) {
                composingStates.add(contact);
            } else {
                composingStates.remove(contact);
            }
            super.notifyDataSetChanged();
            mMessagesList.setSelection(mMessagesList.getCount() - 1);
        }

        @Override
        public Object getItem(int position) {
            if (isStatus(position)) {
                return composingStates.get(position - super.getCount());
            } else {
                return super.getItem(position);
            }
        }

        @Override
        public long getItemId(int position) {
            if (isStatus(position)) {
                return -position;
            } else {
                return super.getItemId(position);
            }
        }

        @Override
        public int getCount() {
            return super.getCount() + composingStates.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (isStatus(position)) {
                return VIEWTYPE_STATUS_VIEW;
            }

            Cursor cursor = (Cursor) getItem(position);
            return getItemViewType(cursor);
        }

        public int getItemViewType(Cursor cursor) {
            long sender_id = cursor.getLong(cursor
                    .getColumnIndex(MessageEntry.COLUMN_NAME_SENDER_ID));
            if (sender_id == 0) {
                return VIEWTYPE_SELF_MESSAGE_VIEW;
            }
            return VIEWTYPE_RECIEVED_MESSAGE_VIEW;

        }

        @Override
        public int getViewTypeCount() {
            return 3;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (isStatus(position)) {
                if (convertView == null) {
                    convertView = new ComposingStatusView(parent.getContext());
                }
                ((ComposingStatusView) convertView).update(
                        (OPUser) getItem(position), null);

                return convertView;

            } else {
                return super.getView(position, convertView, parent);
            }
        }

        @Override
        public View getDropDownView(int position, View convertView,
                ViewGroup parent) {
            if (isStatus(position)) {
                if (convertView == null) {
                    convertView = new ComposingStatusView(parent.getContext());
                }
                ((ComposingStatusView) convertView).update(
                        (OPUser) getItem(position), null);
                return convertView;

            } else {
                return super.getDropDownView(position, convertView, parent);
            }
        }

        // this function will not be called for status view
        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            if (cursor != null) {
                OPMessage message = OPMessage.fromCursor(cursor);
                if (view instanceof SelfMessageView) {
                    ((SelfMessageView) view).update(message);
                } else if (view instanceof PeerMessageView) {
                    ((PeerMessageView) view).update(message);
                }
            }
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup arg2) {
            int viewType = getItemViewType(cursor);
            View view = null;
            switch (viewType) {
            case VIEWTYPE_SELF_MESSAGE_VIEW:
                view = new SelfMessageView(context);
                ((SelfMessageView) view).setSession(mSession);
                break;
            case VIEWTYPE_RECIEVED_MESSAGE_VIEW:
                view = new PeerMessageView(context);
                ((PeerMessageView) view).setSession(mSession);

                break;
            case VIEWTYPE_STATUS_VIEW:
                view = new ComposingStatusView(context);
                break;
            }

            return view;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_chat, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_call:
            if (OPDataManager.getInstance().getSharedAccount()
                    .getState(0, null) != AccountStates.AccountState_Ready) {
                BaseActivity.showInvalidStateWarning(getActivity());
                return true;
            }
            if (mSession.getCurrentCall() != null) {
                CallActivity.launchForCall(getActivity(), mSession
                        .getCurrentCall().getPeer().getPeerURI());
                return true;
            } else
                return false;
        case R.id.menu_audio:
            makeCall(false);
            return true;
        case R.id.menu_video:
            makeCall(true);
            return true;
            // case R.id.menu_add:
            // addParticipant();
            // return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    public void onDone(long[] userIds) {

        long _ids[] = new long[mUserIDs.length + userIds.length];
        for (int i = 0; i < mUserIDs.length; i++) {
            _ids[i] = mUserIDs[i];
        }
        for (int i = 0; i < userIds.length; i++) {
            _ids[i + mUserIDs.length] = userIds[i];
        }
        mUserIDs = _ids;
        List<OPUser> users = OPDataManager.getDatastoreDelegate().getUsers(
                userIds);
        if (users != null) {
            mSession.addParticipant(users);
            updateUsersView(mSession.getParticipants());
            mWindowId = mSession.getCurrentWindowId();
            getLoaderManager().restartLoader(URL_LOADER, null, this);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putLongArray(IntentData.ARG_PEER_USER_IDS, mUserIDs);
        super.onSaveInstanceState(outState);
    }

    // After adding a new participant we'll have to switch chat window
    private void addParticipant() {
        Intent intent = new Intent(getActivity(), ProfilePickerActivity.class);
        intent.putExtra(IntentData.ARG_PEER_USER_IDS, mUserIDs);
        startActivityForResult(intent,
                ProfilePickerActivity.REQUEST_CODE_ADD_CONTACTS);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onActivityResult requestCode " + requestCode
                    + " resultCode " + resultCode);
        }
        switch (requestCode) {
        case ProfilePickerActivity.REQUEST_CODE_ADD_CONTACTS:
            if (resultCode == Activity.RESULT_OK) {
                long userIds[] = data
                        .getLongArrayExtra(IntentData.ARG_PEER_USER_IDS);
                onDone(userIds);
            }
            break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void makeCall(boolean video) {
        String peerUri = mSession.getUserBySenderId(mUserIDs[0]).getPeerUri();
        if (null != OPSessionManager.getInstance().getOngoingCallForPeer(
                peerUri)) {
            CallActivity.launchForCall(getActivity(), peerUri);
        } else {
            CallActivity.launchForCall(getActivity(), mUserIDs, true, video);
        }
    }

    // Begin: CursorCallback implementation
    private static final int URL_LOADER = 0;

    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle arg1) {
        switch (loaderID) {
        case URL_LOADER:
            // Returns a new CursorLoader
            return new CursorLoader(getActivity(), // Parent activity context
                    OPContentProvider
                            .getContentUri(DatabaseContracts.MessageEntry.URI_PATH_WINDOW_ID_URI_BASE
                                    + mWindowId),
                    null,
                    null, // No selection clause
                    null, // No selection arguments
                    "time asc" // Default sort order
            );
        default:
            // An invalid id was passed in
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.d("test", "ChatFragment onLoadFinished" + cursor);
        mAdapter.changeCursor(cursor);
        mMessagesList.setSelection(mMessagesList.getCount() - 1);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {
        mAdapter.changeCursor(null);
    }

    // Beginning of SessionListener implementation
    static final int MENUID_DELETE_MESSAGE = 10000;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {
        if (v == mMessagesList) {
            AdapterView.AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) menuInfo;

            if (acmi.targetView instanceof SelfMessageView
                    && ((SelfMessageView) acmi.targetView).canEditMessage()) {
                menu.add(0, MENUID_DELETE_MESSAGE, Menu.NONE, "delete");
            }
        }
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case MENUID_DELETE_MESSAGE:
            AdapterView.AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item
                    .getMenuInfo();
            ((SelfMessageView) acmi.targetView).onDeleteSelected();
            break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onContactComposingStateChanged(ComposingStates composingStates,
            OPUser contact) {
        mAdapter.notifyDataSetChanged(composingStates, contact);
    }

    @Override
    public boolean onNewMessage(OPMessage message) {
        return false;
    }

    @Override
    public boolean onPushMessage(OPMessage message) {
        return false;
    }

    @Override
    public boolean onNewContactJoined(OPContact opContact) {
        return false;
    }

    @Override
    public boolean onContactQuit(OPContact opContact) {
        return false;
    }

    // End of SessionListener implementation
}
