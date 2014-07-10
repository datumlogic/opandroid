package com.openpeer.sdk.delegates;

import java.util.Hashtable;

import com.openpeer.javaapi.OPConversationThread;
import com.openpeer.javaapi.OPMessage;

public class GroupBasedMessageDispatcher implements MessageDispatcher {
	Hashtable<String, MessageReceiver> mReceivers = new Hashtable<String, MessageReceiver>();

	public void dispatch(OPConversationThread thread, OPMessage message) {
		MessageReceiver receiver = mReceivers.get(thread.getThreadID());
		if (receiver != null) {
			receiver.onNewMessage(message);
		}
	}

	@Override
	public void registerReceiver(OPConversationThread thread,
			MessageReceiver receiver) {
		mReceivers.put(thread.getThreadID(), receiver);
	}

}
