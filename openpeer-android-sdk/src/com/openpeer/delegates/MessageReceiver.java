package com.openpeer.delegates;

import com.openpeer.javaapi.OPMessage;

public interface MessageReceiver {
	public void onNewMessage(OPMessage message);
}
