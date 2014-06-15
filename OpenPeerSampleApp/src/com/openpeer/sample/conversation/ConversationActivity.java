package com.openpeer.sample.conversation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.openpeer.app.OPDataManager;
import com.openpeer.javaapi.AccountStates;
import com.openpeer.javaapi.OPAccount;
import com.openpeer.sample.BaseFragmentActivity;
import com.openpeer.sample.IntentData;
import com.openpeer.sample.R;
import com.openpeer.sample.contacts.ContactsFragment;
import com.openpeer.sample.util.NetworkUtil;

public class ConversationActivity extends BaseFragmentActivity {

	public static void launchForChat(Context context, String peerContactId) {
		Intent intent = new Intent(context, ConversationActivity.class);
		intent.putExtra(IntentData.ARG_CONVERSATION_ACTION,
				IntentData.ACTION_CHAT);
		intent.putExtra(IntentData.ARG_PEER_CONTACT_ID, peerContactId);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_container);
		Intent intent = getIntent();
		String action = intent
				.getStringExtra(IntentData.ARG_CONVERSATION_ACTION);
		switch (action) {
		case IntentData.ACTION_CALL:
			break;
		case IntentData.ACTION_CHAT:
			ChatFragment cFragment = ChatFragment.newInstance(intent.getStringExtra(IntentData.ARG_PEER_CONTACT_ID));
			setContentFragment(cFragment);
			break;
		}
	}
}
