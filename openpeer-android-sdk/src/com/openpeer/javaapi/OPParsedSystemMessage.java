package com.openpeer.javaapi;

public class OPParsedSystemMessage {
	private SystemMessageTypes mType;
	private String mSystemMessage;
	
	public String getSystemMessage() {
		return mSystemMessage;
	}
	public void setSystemMessage(String mSystemMessage) {
		this.mSystemMessage = mSystemMessage;
	}
	public SystemMessageTypes getType() {
		return mType;
	}
	public void setType(SystemMessageTypes mType) {
		this.mType = mType;
	}

}
