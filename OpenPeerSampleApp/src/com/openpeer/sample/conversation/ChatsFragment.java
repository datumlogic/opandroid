package com.openpeer.sample.conversation;

import static com.openpeer.datastore.DatabaseContracts.WindowViewEntry.*;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.openpeer.app.OPSession;
import com.openpeer.datastore.DatabaseContracts;
import com.openpeer.datastore.DatabaseContracts.ContactsViewEntry;
import com.openpeer.datastore.DatabaseContracts.WindowViewEntry;
import com.openpeer.delegates.CallbackHandler;
import com.openpeer.javaapi.ContactStates;
import com.openpeer.javaapi.MessageDeliveryStates;
import com.openpeer.javaapi.OPContact;
import com.openpeer.javaapi.OPConversationThread;
import com.openpeer.javaapi.OPConversationThreadDelegate;
import com.openpeer.javaapi.OPMessage;
import com.openpeer.sample.BaseFragment;
import com.openpeer.sample.R;

public class ChatsFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {

	private ChatInfoAdaptor mAdapter;
	private List<OPSession> mSessions;
	private ListView mMessagesList;

	private ConversationThreadDelegate mConvThreadDelegate;

	public static ChatsFragment newInstance() {
		ChatsFragment fragment = new ChatsFragment();
		Bundle args = new Bundle();
		// args.putString(IntentData.ARG_PEER_CONTACT_ID, peerContactId);
		fragment.setArguments(args);
		return fragment;
	}

	public static ChatsFragment newTestInstance() {
		ChatsFragment fragment = new ChatsFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_chats, null);
		return setupView(view);
	}

	@Override
	public void onResume() {
		super.onResume();
		mSessions = getSessions();
		// CallbackHandler.getInstance().registerConversationThreadDelegate(
		// mConvThreadDelegate);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		CallbackHandler.getInstance().unregisterConversationThreadDelegate(
				mConvThreadDelegate);
	}

	View setupView(View view) {
		mMessagesList = (ListView) view.findViewById(R.id.listview);
		mAdapter = new ChatInfoAdaptor(getActivity(), null);
		mMessagesList.setAdapter(mAdapter);
		getLoaderManager().initLoader(URL_LOADER, null, this);

		return view;
	}

	private List<OPSession> getSessions() {
		if (mSessions == null) {
			mSessions = new ArrayList<OPSession>();
			// TODO: do some setup
		}
		return mSessions;
	}

	class ChatInfoAdaptor extends CursorAdapter {

		public ChatInfoAdaptor(Context context, Cursor c) {
			super(context, c);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			((ChatInfoItemView) view).updateData(cursor);
		}

		@Override
		public View newView(Context view, Cursor cursor, ViewGroup arg2) {
			// TODO Auto-generated method stub
			return new ChatInfoItemView(arg2.getContext());
		}
	}

	class ConversationThreadDelegate extends OPConversationThreadDelegate {

		@Override
		public void onConversationThreadNew(
				OPConversationThread conversationThread) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onConversationThreadContactsChanged(
				OPConversationThread conversationThread) {
			Log.d("ChatFragment", "onConversationThreadContactsChanged"
					+ conversationThread);
		}

		@Override
		public void onConversationThreadContactStateChanged(
				OPConversationThread conversationThread, OPContact contact,
				ContactStates state) {
			Log.d("ChatFragment", "onConversationThreadContactStateChanged = "
					+ contact + " state " + state);
		}

		@Override
		public void onConversationThreadMessage(
				OPConversationThread conversationThread, String messageID) {
			final OPMessage message = conversationThread.getMessage(messageID);
			Log.d("ChatFragment", "onConversationThreadMessage = " + messageID
					+ " full message " + message);
			Log.d("ChatFragment", "onConversationThreadPushMessage = "
					+ messageID + " thread " + conversationThread);
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {

				}

			});
		}

		@Override
		public void onConversationThreadMessageDeliveryStateChanged(
				OPConversationThread conversationThread, String messageID,
				MessageDeliveryStates state) {
			Log.d("ChatFragment",
					"onConversationThreadMessageDeliveryStateChanged = "
							+ messageID + " state " + state + " thread "
							+ conversationThread);
		}

		@Override
		public void onConversationThreadPushMessage(
				OPConversationThread conversationThread, String messageID,
				OPContact contact) {
			Log.d("ChatFragment", "onConversationThreadPushMessage = "
					+ messageID + " thread " + conversationThread);
			// getActivity().runOnUiThread(new Runnable() {
			//
			// @Override
			// public void run() {
			// // TODO Auto-generated method stub
			// mAdapter.notifyDataSetChanged();
			// }
			// });
		}

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_chat, menu);
	}

	// Begin: CursorCallback implementation
	private static final int URL_LOADER = 0;
	static final String LIST_PROJECTION[] = { BaseColumns._ID,
			WindowViewEntry.COLUMN_NAME_PARTICIPANT_NAMES,
			WindowViewEntry.COLUMN_NAME_LAST_MESSAGE,
			WindowViewEntry.COLUMN_NAME_LAST_MESSAGE_TIME,

			WindowViewEntry.COLUMN_NAME_USER_ID,
			WindowViewEntry.COLUMN_NAME_WINDOW_ID };

	@Override
	public Loader<Cursor> onCreateLoader(int loaderID, Bundle arg1) {
		switch (loaderID) {
		case URL_LOADER:
			// Returns a new CursorLoader
			return new CursorLoader(
					getActivity(), // Parent activity context
					DatabaseContracts.WindowViewEntry.CONTENT_URI, // Table to
																	// query
					null,//LIST_PROJECTION, // Projection to return
					null, // No selection clause
					null, // No selection arguments
					null // Default sort order
			);
		default:
			// An invalid id was passed in
			return null;
		}
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		mAdapter.changeCursor(cursor);

	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		mAdapter.changeCursor(null);

	}
	// End: CursorCallback implementation

}
