package com.openpeer.javaapi;

import android.text.format.Time;

public class OPMessage {
	private String mMessageId;
	private String mReplacesMessageId;
	private OPContact mFrom;
	private String mMessageType;
	private String mMessage;
	private Time mTime;
	private boolean mValidated;
	
	public OPMessage() 
	{
		
	}

	public OPContact getFrom() {
		return mFrom;
	}
	public void setFrom(OPContact mFrom) {
		this.mFrom = mFrom;
	}
	public String getMessageType() {
		return mMessageType;
	}
	public void setMessageType(String mMessageType) {
		this.mMessageType = mMessageType;
	}
	public String getMessage() {
		return mMessage;
	}
	public void setMessage(String mMessage) {
		this.mMessage = mMessage;
	}
	public Time getTime() {
		return mTime;
	}
	public void setTime(Time mTime) {
		this.mTime = mTime;
	}

	public String getMessageId() {
		return mMessageId;
	}

	public void setMessageId(String messageId) {
		this.mMessageId = messageId;
	}

	public String getReplacesMessageId() {
		return mReplacesMessageId;
	}

	public void setReplacesMessageId(String mReplacesMessageId) {
		this.mReplacesMessageId = mReplacesMessageId;
	}

	public boolean isValidated() {
		return mValidated;
	}

	public void setValidated(boolean mValidated) {
		this.mValidated = mValidated;
	}

}
