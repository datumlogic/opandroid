package com.openpeer.sample.conversation;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.openpeer.app.OPDataManager;
import com.openpeer.app.OPSession;
import com.openpeer.app.OPSessionManager;
import com.openpeer.delegates.CallbackHandler;
import com.openpeer.javaapi.ContactStates;
import com.openpeer.javaapi.MessageDeliveryStates;
import com.openpeer.javaapi.OPContact;
import com.openpeer.javaapi.OPContactProfileInfo;
import com.openpeer.javaapi.OPConversationThread;
import com.openpeer.javaapi.OPConversationThreadDelegate;
import com.openpeer.javaapi.OPIdentityContact;
import com.openpeer.javaapi.OPMessage;
import com.openpeer.javaapi.OPMessage.OPMessageType;
import com.openpeer.sample.BaseFragment;
import com.openpeer.sample.IntentData;
import com.openpeer.sample.R;
import com.squareup.picasso.Picasso;

public class ChatsFragment extends BaseFragment {

	private ChatInfoAdaptor mChatInfoAdaptor;
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
		mChatInfoAdaptor = new ChatInfoAdaptor();
		mMessagesList.setAdapter(mChatInfoAdaptor);

		return view;
	}

	private List<OPSession> getSessions() {
		if (mSessions == null) {
			mSessions = new ArrayList<OPSession>();
			// TODO: do some setup
		}
		return mSessions;
	}

	class ChatInfoAdaptor extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mSessions == null ? 0 : mSessions.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return mSessions.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public int getViewTypeCount() {
			// TODO Auto-generated method stub
			return 1;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			ChatInfoItemView view = null;
			if (view == null) {
				view = (ChatInfoItemView) View.inflate(getActivity(),
						R.layout.item_chat_info, null);

			} else {
				view = (ChatInfoItemView) arg1;
			}
			view.updateData((OPSession) getItem(arg0));

			return view;
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
			final OPMessage message = conversationThread.getMessage(messageID);
			Log.d("ChatFragment", "onConversationThreadPushMessage = "
					+ messageID + " thread " + conversationThread);
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					mChatInfoAdaptor.notifyDataSetChanged();
				}
			});
		}

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_chat, menu);
	}

}
