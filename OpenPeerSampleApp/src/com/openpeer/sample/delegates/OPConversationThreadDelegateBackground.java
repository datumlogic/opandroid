package com.openpeer.sample.delegates;

import android.content.Intent;
import android.util.Log;

import com.openpeer.javaapi.ContactStates;
import com.openpeer.javaapi.MessageDeliveryStates;
import com.openpeer.javaapi.OPContact;
import com.openpeer.javaapi.OPConversationThread;
import com.openpeer.javaapi.OPConversationThreadDelegate;
import com.openpeer.javaapi.OPMessage;
import com.openpeer.sample.OPApplication;
import com.openpeer.sample.R;
import com.openpeer.sample.conversation.ConversationActivity;

public class OPConversationThreadDelegateBackground extends
		OPConversationThreadDelegate {

	@Override
	public void onConversationThreadNew(OPConversationThread conversationThread) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConversationThreadContactsChanged(
			OPConversationThread conversationThread) {
		Log.d("output", "onConversationThreadContactsChanged  thread = "
				+ conversationThread);

	}

	@Override
	public void onConversationThreadContactStateChanged(
			OPConversationThread conversationThread, OPContact contact,
			ContactStates state) {
		// TODO Auto-generated method stub
		Log.d("output", "onConversationThreadContactStateChanged  state = "
				+ state.toString());
	}

	@Override
	public void onConversationThreadMessage(
			OPConversationThread conversationThread, String messageID) {
//		OPMessage message = conversationThread.getMessage(messageID);
//		Log.d("output", "onConversationThreadMessage = " + conversationThread
//				+ " messageId " + messageID + " full message " + message);
//
//		// fire bar notifications if app in background
//		int notificationId = (int) conversationThread.getStableID();
//		String title = "new Messge";
//		String contentText = message.getMessage();
//		int notificationDrawableRes = R.drawable.ic_launcher;
//		Intent intent = ConversationActivity.getIntentForNotification(
//				OPApplication.getInstance(), conversationThread, messageID,
//				null);
//		OPApplication.notify(notificationId, notificationDrawableRes, title,
//				contentText, intent);
	}

	@Override
	public void onConversationThreadMessageDeliveryStateChanged(
			OPConversationThread conversationThread, String messageID,
			MessageDeliveryStates state) {
		// TODO Auto-generated method stub
		Log.d("output", "onConversationThreadMessageDeliveryStateChanged = "
				+ conversationThread + " messageId " + messageID + "state = "
				+ state.toString());

	}

	@Override
	public void onConversationThreadPushMessage(
			OPConversationThread conversationThread, String messageID,
			OPContact contact) {

	}

}
