package com.openpeer.delegates;

import com.openpeer.javaapi.OPConversationThread;
import com.openpeer.javaapi.OPMessage;

public interface MessageDispatcher {
	public void registerReceiver(OPConversationThread thread, MessageReceiver receiver);
	public void dispatch(OPConversationThread thread, OPMessage message);

}
