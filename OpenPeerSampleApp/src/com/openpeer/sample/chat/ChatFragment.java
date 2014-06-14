package com.openpeer.sample.chat;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.openpeer.app.OPDataManager;
import com.openpeer.javaapi.OPIdentityContact;
import com.openpeer.sample.BaseFragment;
import com.openpeer.sample.R;

public class ChatFragment extends BaseFragment {
	private static final String ARG_IDENTITY_CONTACT_ID = "icId";
	private static final String ARG_PEER_CONTACT_ID = "peerId";
	private ListView mMessagesList;
	private MessagesAdaptor mMessagesAdaptor;
	private ArrayList mMessages;

	private OPIdentityContact mPeerContact, mIdentityContact;

	public static ChatFragment newInstance(String myContactId,
			String peerContactId) {
		ChatFragment fragment = new ChatFragment();
		Bundle args = new Bundle();
		args.putString(ARG_IDENTITY_CONTACT_ID, myContactId);
		args.putString(ARG_PEER_CONTACT_ID, peerContactId);
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
		mPeerContact=OPDataManager.getInstance().getContact(args.getString(ARG_PEER_CONTACT_ID);
		mIdentityContact = OPDataManager.getInstance().getContact(args.getString(ARG_IDENTITY_CONTACT_ID));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_chat, null);
	}

	class MessagesAdaptor extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			return null;
		}

	}
}
