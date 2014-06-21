package com.openpeer.javaapi;

import android.text.format.Time;

//Important: Don't remove the empty constructor since it's being used in jni
// Don't change any existing functions signature without making sure it doesn't break jni!
public class OPMessage {
	public static class OPMessageType {
		public static final String TYPE_TEXT = "text/x-application-hookflash-message-text";
		public static final String TYPE_CONTROL = "text/x-application-hookflash-message-system";
	}

	public static class SystemMessageType {
		public static final int SystemMessage_EstablishSessionBetweenTwoPeers = 0;
		public static final int SystemMessage_IsContactAvailable = 1;
		public static final int SystemMessage_IsContactAvailable_Response = 2;
		public static final int SystemMessage_CallAgain = 3;
		public static final int SystemMessage_CheckAvailability = 4;
		public static final int SystemMessage_APNS_Request = 5;
		public static final int SystemMessage_APNS_Response = 6;

		public static final int SystemMessage_None = 111;
	}

	private OPContact mFrom;
	private String mMessageType;
	private String mMessage;
	private Time mTime;
	private String mSenderId;
	private String mMessageId;
	private long readTimeInMillis;// read time in millis

	public long getReadTimeInMillis() {
		return readTimeInMillis;
	}

	public void setReadTimeInMillis(long readTimeInMillis) {
		this.readTimeInMillis = readTimeInMillis;
	}

	public String getMessageId() {
		return mMessageId;
	}

	public void setMessageId(String messageId) {
		this.mMessageId = messageId;
	}

	public String getSenderId() {
		return mSenderId;
	}

	public void setSenderId(String mSenderId) {
		this.mSenderId = mSenderId;
	}

	public OPMessage() {
	}

	public OPMessage(String senderId, String mMessageType, String message,
			Time mTime) {
		super();
		this.mSenderId = senderId;
		this.mMessageType = mMessageType;
		this.mMessage = message;
		this.mTime = mTime;
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

	public String toString() {
		return super.toString() + " from " + mFrom + " messageType "
				+ mMessageType + " message " + mMessage;
	}

}
