package com.openpeer.delegates;

import java.util.Hashtable;

import com.openpeer.javaapi.OPConversationThread;
import com.openpeer.javaapi.OPMessage;

public class ContactsBasedMessageDispatcher implements MessageDispatcher {
	// key: windowId, which is derived from participants
	Hashtable<Long, MessageReceiver> mReceivers = new Hashtable<Long, MessageReceiver>();

	public void dispatch(OPConversationThread thread, OPMessage message) {
		MessageReceiver receiver = mReceivers.get(thread.getCurrentWindowId());
		if (receiver != null) {
			receiver.onNewMessage(message);
		}
	}

	@Override
	public void registerReceiver(OPConversationThread thread,
			MessageReceiver receiver) {
		mReceivers.put(thread.getCurrentWindowId(), receiver);
	}

}
