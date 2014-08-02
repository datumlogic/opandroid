package com.openpeer.sample.conversation;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.openpeer.javaapi.AccountStates;
import com.openpeer.javaapi.CallStates;
import com.openpeer.javaapi.OPCall;
import com.openpeer.javaapi.OPContact;
import com.openpeer.javaapi.OPConversationThread;
import com.openpeer.javaapi.OPIdentityContact;
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
import com.openpeer.sample.push.PushRegistrationManager;
import com.openpeer.sample.push.PushResult;
import com.openpeer.sample.push.PushToken;
import com.openpeer.sample.push.UAPushProviderImpl;
import com.openpeer.sample.util.DateFormatUtils;
import com.openpeer.sdk.app.OPDataManager;
import com.openpeer.sdk.datastore.DatabaseContracts;
import com.openpeer.sdk.datastore.DatabaseContracts.MessageEntry;
import com.openpeer.sdk.datastore.OPContentProvider;
import com.openpeer.sdk.model.OPSession;
import com.openpeer.sdk.model.OPUser;
import com.openpeer.sdk.utils.OPModelUtils;
import com.squareup.picasso.Picasso;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ChatFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private final static int VIEWTYPE_SELF_MESSAGE_VIEW = 0;
    private final static int VIEWTYPE_RECIEVED_MESSAGE_VIEW = 1;
    private static final int DEFAULT_NUM_MESSAGES_TO_LOAD = 30;
    private static final DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
    private static final String TAG = ChatFragment.class.getSimpleName();
    private ListView mMessagesList;
    private TextView mComposeBox;
    private View mSendButton;
    private MessagesAdaptor mAdapter;
    private List<OPMessage> mMessages;

//    private OPIdentityContact mSelfContact;
//    private OPConversationThread mConvThread;
    private long mWindowId;
    private OPSession mSession;
    long[] mUserIDs;
    List<OPUser> participants;
    private CallInfoView mCallInfoView;

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
            mUserIDs = savedInstanceState.getLongArray(IntentData.ARG_PEER_USER_IDS);
        }
        participants = OPDataManager.getDatastoreDelegate().getUsers(mUserIDs);

        mWindowId = OPModelUtils.getWindowId(mUserIDs);
        OPNotificationBuilder.cancelNotificationForChat((int) mWindowId);
//        mSelfContact = OPDataManager.getInstance().getSelfContacts().get(0);
        this.setHasOptionsMenu(true);
        //TODO:remove this call and use lazy loading.
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.fragment_chat, null);
        return setupView(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        mSession.setWindowAttached(true);
        OPDataManager.getDatastoreDelegate().markMessagesRead(mWindowId);

        // TODO: proper look up
        String peerUri = mSession.getParticipants().get(0).getPeerUri();
        // OPCall call = mSession.getCurrentCall();
        OPCall call = OPSessionManager.getInstance().getOngoingCallForPeer(peerUri);
        if (call != null && (call.getState() == CallStates.CallState_Open
                || call.getState() == CallStates.CallState_Active)) {
            Log.d(TAG, "now show call info");
            mCallInfoView.setVisibility(View.VISIBLE);
            mCallInfoView.bindCall(call);

        } else {
            mCallInfoView.setVisibility(View.GONE);

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mSession.setWindowAttached(false);
        if (mCallInfoView.isShown()) {
            mCallInfoView.unbind();
        }
    }

    void updateUsersView(List<OPUser> users) {
        getActivity().getActionBar().setTitle(users.get(0).getName());
    }

    View setupView(View view) {
        mCallInfoView = (CallInfoView) view.findViewById(R.id.call_info);
        View emptyView = view.findViewById(R.id.empty_view);
        mMessagesList = (ListView) view.findViewById(R.id.listview);
        mMessagesList.setEmptyView(emptyView);
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
                if (mComposeBox.getText() == null || mComposeBox.getText().length() == 0) {
                    return;
                }

                String messageId = java.util.UUID.randomUUID().toString();
                // we use 0 for home user
                final OPMessage msg = new OPMessage(0, OPMessageType.TYPE_TEXT, mComposeBox.getText().toString(), System.currentTimeMillis(),
                        messageId);
                msg.setRead(true);

                mComposeBox.setText("");

                getSession().sendMessage(msg, false);
//                OPContact contact = mSession.getParticipants().get(0).getOPContact();
//                PushRegistrationManager.getInstance().getDeviceToken(contact.getPeerURI(), new Callback<PushToken>() {
//                    @Override
//                    public void success(PushToken token, Response response) {
//                        new UAPushProviderImpl().pushMessage(msg, token, new Callback<PushResult>() {
//                            @Override
//                            public void success(PushResult pushResult, Response response) {
//
//                            }
//
//                            @Override
//                            public void failure(RetrofitError error) {
//
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void failure(RetrofitError error) {
//
//                    }
//                });
            }
        });
        getLoaderManager().initLoader(URL_LOADER, null, this);

        return view;
    }

    private List<OPMessage> getMessages() {
        if (mMessages == null) {
            mMessages = new ArrayList<OPMessage>();
            // TODO: do some setup
        }
        return mMessages;
    }

    class MessagesAdaptor extends CursorAdapter {

        public MessagesAdaptor(Context context, Cursor c) {
            super(context, c);
            // TODO Auto-generated constructor stub
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return super.getItem(position);
        }

        @Override
        public int getItemViewType(int position) {
            Cursor cursor = (Cursor) getItem(position);
            return getItemViewType(cursor);

        }

        public int getItemViewType(Cursor cursor) {
            long sender_id = cursor.getLong(cursor.getColumnIndex(MessageEntry.COLUMN_NAME_SENDER_ID));
            if (sender_id == 0) {
                return 0;
            }
            return 1;

        }

        @Override
        public int getViewTypeCount() {
            // TODO Auto-generated method stub
            return 2;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            OPMessage message = OPMessage.fromCursor(cursor);
            ((ViewHolder) view.getTag()).update(message);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup arg2) {
            int viewType = getItemViewType(cursor);
            View view = null;
            switch (viewType) {
                case VIEWTYPE_SELF_MESSAGE_VIEW:
                    view = View.inflate(getActivity(), R.layout.item_message_self, null);
                    break;
                case VIEWTYPE_RECIEVED_MESSAGE_VIEW:
                    view = View.inflate(getActivity(), R.layout.item_message_peer, null);

                    break;
            }
            ViewHolder viewHolder = new ViewHolder(view, viewType);
            view.setTag(viewHolder);
            return view;
        }
    }

    class ViewHolder {
        ImageView avatarView;
        TextView title;

        TextView time;
        TextView text;
        int viewType;

        public ViewHolder(View view, int viewType) {
            title = (TextView) view.findViewById(R.id.user);
            avatarView = (ImageView) view.findViewById(R.id.avatar);
            text = (TextView) view.findViewById(R.id.message);
            time = (TextView) view.findViewById(R.id.time);

            this.viewType = viewType;
        }

        void update(OPMessage data) {
            switch (viewType) {
                case VIEWTYPE_SELF_MESSAGE_VIEW:
                    // Picasso.with(getActivity()).load(mSelfContact.getDefaultAvatarUrl()).into(avatarView);
                    break;
                case VIEWTYPE_RECIEVED_MESSAGE_VIEW:
                    OPUser sender = mSession.getUserBySenderId(data.getSenderId());
                    // Picasso.with(getActivity()).load(sender.getAvatarUri()).into(avatarView);

                    break;
            }

            String avatar = null;
            if (data.getSenderId() == 0) {
                // self
                // avatar =
                // OPDataManager.getInstance().getSelfContacts().get(0).getDefaultAvatarUrl();
            } else {
                OPUser user = mSession.getUserBySenderId(data.getSenderId());
                if (user != null) {
                    avatar = user.getAvatarUri();
                    if (user.getName() != null) {
                        title.setText(user.getName());
                    }
                }
            }
            // if (avatar != null) {
            // Picasso.with(getActivity()).load(avatar).into(avatarView);
            // }

            time.setText(DateFormatUtils.getSameDayTime(data.getTime().toMillis(true)));
            text.setText(data.getMessage());
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
                if (OPDataManager.getInstance().getSharedAccount().getState(0, null) != AccountStates.AccountState_Ready) {
                    BaseActivity.showInvalidStateWarning(getActivity());
                    return true;
                }
                if (mSession.getCurrentCall() != null) {
                    CallActivity.launchForCall(getActivity(), mSession.getCurrentCall().getPeer().getPeerURI());
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
        List<OPUser> users = OPDataManager.getDatastoreDelegate().getUsers(userIds);
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
        startActivityForResult(intent, ProfilePickerActivity.REQUEST_CODE_ADD_CONTACTS);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onActivityResult requestCode " + requestCode + " resultCode " + resultCode);
        }
        switch (requestCode) {
            case ProfilePickerActivity.REQUEST_CODE_ADD_CONTACTS:
                if (resultCode == Activity.RESULT_OK) {
                    long userIds[] = data.getLongArrayExtra(IntentData.ARG_PEER_USER_IDS);
                    onDone(userIds);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void makeCall(boolean video) {
        String peerUri = mSession.getUserBySenderId(mUserIDs[0]).getPeerUri();
        if (null != OPSessionManager.getInstance().getOngoingCallForPeer(peerUri)) {
            CallActivity.launchForCall(getActivity(), peerUri);
        } else {
            CallActivity.launchForCall(getActivity(), mUserIDs, true, video);
        }
    }

    // Begin: CursorCallback implementation
    private static final int URL_LOADER = 0;

    // static final String LIST_PROJECTION[] = { BaseColumns._ID,
    // MessageEntry.COLUMN_NAME_MESSAGE_ID,
    // MessageEntry.COLUMN_NAME_MESSAGE_TEXT,
    // MessageEntry.COLUMN_NAME_SENDER_ID,
    // MessageEntry.COLUMN_NAME_WINDOW_ID };

    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle arg1) {
        switch (loaderID) {
            case URL_LOADER:
                // Returns a new CursorLoader
                return new CursorLoader(getActivity(), // Parent activity context
                        OPContentProvider.getContentUri(DatabaseContracts.MessageEntry.URI_PATH_WINDOW_ID_URI_BASE + mWindowId),
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

}
