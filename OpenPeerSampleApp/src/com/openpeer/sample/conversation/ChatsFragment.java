package com.openpeer.sample.conversation;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
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

import com.openpeer.sample.BaseFragment;
import com.openpeer.sample.R;
import com.openpeer.sdk.datastore.OPContentProvider;
import com.openpeer.sdk.datastore.DatabaseContracts;
import com.openpeer.sdk.model.OPSession;

public class ChatsFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {

	private ChatInfoAdaptor mAdapter;
	private List<OPSession> mSessions;
	private ListView mMessagesList;


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

	}

	View setupView(View view) {
		View emptyView = view.findViewById(R.id.empty_view);
		emptyView.findViewById(R.id.start_chat)
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						if (getActivity() instanceof ChatsViewListener) {
							((ChatsViewListener) getActivity()).onChatsEmptyViewClick();
						}
					}
				});
		mMessagesList = (ListView) view.findViewById(R.id.listview);
		mMessagesList.setEmptyView(emptyView);
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
			ChatInfo info = ChatInfo.fromCursor(cursor);
			((ChatInfoItemView) view).updateData(info);
		}

		@Override
		public View newView(Context view, Cursor cursor, ViewGroup arg2) {
			// TODO Auto-generated method stub
			return new ChatInfoItemView(arg2.getContext());
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_chat, menu);
	}

	// Begin: CursorCallback implementation
	private static final int URL_LOADER = 0;

	// static final String LIST_PROJECTION[] = { BaseColumns._ID,
	// WindowViewEntry.COLUMN_NAME_PARTICIPANT_NAMES,
	// WindowViewEntry.COLUMN_NAME_LAST_MESSAGE,
	// WindowViewEntry.COLUMN_NAME_LAST_MESSAGE_TIME,
	//
	// WindowViewEntry.COLUMN_NAME_USER_ID,
	// WindowViewEntry.COLUMN_NAME_WINDOW_ID };

	@Override
	public Loader<Cursor> onCreateLoader(int loaderID, Bundle arg1) {
		switch (loaderID) {
		case URL_LOADER:
			// Returns a new CursorLoader
			return new CursorLoader(
					getActivity(), // Parent activity context
                    OPContentProvider.getContentUri(DatabaseContracts.WindowViewEntry.URI_PATH_INFO),
//					DatabaseContracts.WindowViewEntry.CONTENT_URI, // Table to
																	// query
					null,// LIST_PROJECTION, // Projection to return
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
	public static interface ChatsViewListener {
		public void onChatsEmptyViewClick();
	}

}
