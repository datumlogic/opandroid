package com.openpeer.sample;

import com.openpeer.javaapi.OPConversationThread;
import com.openpeer.javaapi.OPMessage;

public interface NewMessageHandler {
	public boolean handleMessage(OPConversationThread thread, OPMessage message);
}
