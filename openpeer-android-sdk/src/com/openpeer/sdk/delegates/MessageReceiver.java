package com.openpeer.sdk.delegates;

import com.openpeer.javaapi.OPMessage;

public interface MessageReceiver {
	public void onNewMessage(OPMessage message);
}
