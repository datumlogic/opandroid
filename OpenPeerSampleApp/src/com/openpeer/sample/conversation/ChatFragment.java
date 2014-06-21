package com.openpeer.sample.conversation;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.openpeer.app.OPDataManager;
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

public class ChatFragment extends BaseFragment {
	private final static int VIEWTYPE_SELF_MESSAGE_VIEW = 0;
	private final static int VIEWTYPE_RECIEVED_MESSAGE_VIEW = 1;

	private ListView mMessagesList;
	private TextView mComposeBox;
	private View mSendButton;
	private MessagesAdaptor mMessagesAdaptor;
	private List<OPMessage> mMessages;

	private OPIdentityContact mPeerContact, mSelfContact;
	private OPConversationThread mConvThread;
	private ConversationThreadDelegate mConvThreadDelegate;

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
		Bundle args = getArguments();
		mPeerContact = OPDataManager.getDatastoreDelegate().getIdentityContact(
				args.getString(IntentData.ARG_PEER_CONTACT_ID));
		mSelfContact = OPDataManager.getInstance().getSelfContacts()
				.get(mPeerContact.getAssociatedIdentityId());
		setupChat();
		// mSelfContact =
		// OPDataManager.getDatastoreDelegate().getIdentityContact(
		// mSelfContactId);
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
		mMessagesAdaptor = new MessagesAdaptor();
		mMessagesList.setAdapter(mMessagesAdaptor);
		View layout = view.findViewById(R.id.layout_compose);
		mComposeBox = (TextView) layout.findViewById(R.id.text);
		mSendButton = layout.findViewById(R.id.send);
		mSendButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.d("TODO", "call actual send function");
				if (mComposeBox.getText() == null
						|| mComposeBox.getText().length() == 0) {
					return;
				}
				Time time = new Time();
				time.setToNow();
				String messageId = java.util.UUID.randomUUID().toString();
				OPMessage msg = new OPMessage(mSelfContact.getStableID(),
						OPMessageType.TYPE_TEXT, mComposeBox.getText()
								.toString(), time);

				getMessages().add(msg);
				mMessagesAdaptor.notifyDataSetChanged();
				mComposeBox.setText("");

				mConvThread.sendMessage(messageId,
						OPMessage.OPMessageType.TYPE_TEXT, msg.getMessage(),
						false);
			}
		});
		return view;
	}

	private List<OPMessage> getMessages() {
		if (mMessages == null) {
			mMessages = new ArrayList<OPMessage>();
			// TODO: do some setup
		}
		return mMessages;
	}

	class MessagesAdaptor extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mMessages == null ? 0 : mMessages.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return mMessages.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public int getItemViewType(int position) {
			if (((OPMessage) getItem(position)).getSenderId().equals(
					mSelfContact.getStableID())) {
				return VIEWTYPE_SELF_MESSAGE_VIEW;
			} else {
				return VIEWTYPE_RECIEVED_MESSAGE_VIEW;
			}
		}

		@Override
		public int getViewTypeCount() {
			// TODO Auto-generated method stub
			return 2;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			View view = arg1;
			ViewHolder viewHolder;
			int viewType = getItemViewType(arg0);
			if (view == null) {
				switch (viewType) {
				case VIEWTYPE_SELF_MESSAGE_VIEW:
					view = View.inflate(getActivity(),
							R.layout.item_message_self, null);
					break;
				case VIEWTYPE_RECIEVED_MESSAGE_VIEW:
					view = View.inflate(getActivity(),
							R.layout.item_message_peer, null);

					break;
				}
				viewHolder = new ViewHolder(view, viewType);
				view.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) view.getTag();
			}
			viewHolder.update((OPMessage) getItem(arg0));

			return view;
		}

		class ViewHolder {
			ImageView avatarView;
			TextView text;
			int viewType;

			public ViewHolder(View view, int viewType) {
				avatarView = (ImageView) view.findViewById(R.id.avatar);
				text = (TextView) view.findViewById(R.id.message);
				this.viewType = viewType;
			}

			void update(OPMessage data) {
				switch (viewType) {
				case VIEWTYPE_SELF_MESSAGE_VIEW:
					Picasso.with(getActivity())
							.load(mSelfContact.getDefaultAvatarUrl())
							.into(avatarView);
					break;
				case VIEWTYPE_RECIEVED_MESSAGE_VIEW:
					Picasso.with(getActivity())
							.load(mPeerContact.getDefaultAvatarUrl())
							.into(avatarView);

					break;
				}
				text.setText(data.getMessage());

			}
		}
	}

	void setupChat() {
		mConvThreadDelegate = new ConversationThreadDelegate();
		CallbackHandler.getInstance().registerConversationThreadDelegate(
				mConvThreadDelegate);

		mConvThread = OPSessionManager.getInstance()
				.getSessionForContact(mPeerContact).getThread();

		// List<OPIdentityContact> selfContacts = new
		// ArrayList<OPIdentityContact>();
		//
		// selfContacts.add(mSelfContact);
		//
		// mConvThread = OPConversationThread.create(OPDataManager.getInstance()
		// .getSharedAccount(), selfContacts);
		//
		// List<OPIdentityContact> callContacts = new
		// ArrayList<OPIdentityContact>();
		// List<OPContact> contacts = new ArrayList<OPContact>();
		// List<OPContactProfileInfo> contactProfiles = new
		// ArrayList<OPContactProfileInfo>();
		// OPContactProfileInfo info = new OPContactProfileInfo();
		//
		// OPContact newContact =
		// OPContact.createFromPeerFilePublic(OPDataManager
		// .getInstance().getSharedAccount(), mPeerContact
		// .getPeerFilePublic().getPeerFileString());
		//
		// callContacts.add(mPeerContact);
		// info.setIdentityContacts(callContacts);
		// info.setContact(newContact);
		//
		// contactProfiles.add(info);
		// mConvThread.addContacts(contactProfiles);

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
			OPMessage message = conversationThread.getMessage(messageID);
			Log.d("ChatFragment", "onConversationThreadMessage = " + messageID
					+ " full message " + message);

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
		}

	}
}
