package com.openpeer.javaapi;

import com.openpeer.sdk.app.OPDataManager;
import com.openpeer.sdk.datastore.DatabaseContracts.MessageEntry;
import com.openpeer.sdk.model.OPUser;

import android.database.Cursor;
import android.text.format.Time;


public class OPMessage {
	public static final int DS_DISCOVERING = 0;
	public static final int DS_USER_NOT_AVAILABLE = 1;
	public static final int DS_DELIVERED = 2;

	public static class OPMessageType {
		public static final String TYPE_TEXT = "text/x-application-hookflash-message-text";
		public static final String TYPE_CONTROL = "text/x-application-hookflash-message-system";
		// Used to record/show call record
		// public static final String TYPE_INERNAL_CALL_VIDEO = "text/x-application-hookflash-message-call-video";
		// public static final String TYPE_INERNAL_CALL_AUDIO = "text/x-application-hookflash-message-call-audio";

	}

	/**
	 * @ExcludeFromJavadoc
	 */
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
	private long mSenderId;
	private String mMessageId;
	private boolean mRead;// read time in millis
	private int mDeliveryStatus;

	public boolean isRead() {
		return mRead;
	}

	public void setRead(boolean read) {
		this.mRead = read;
	}

	public String getMessageId() {
		return mMessageId;
	}

	public void setMessageId(String messageId) {
		this.mMessageId = messageId;
	}

	public long getSenderId() {
		return mSenderId;
	}

	public void setSenderId(long mSenderId) {
		this.mSenderId = mSenderId;
	}

	public OPMessage() {
	}

	public OPMessage(long senderId, String mMessageType, String message,
			long sendTime, String messageId, boolean isRead, int deliveryStatus) {
		super();
		this.mSenderId = senderId;
		this.mMessageType = mMessageType;
		this.mMessage = message;
		this.mTime = new Time();
		mTime.set(sendTime);
		this.mMessageId = messageId;
		this.mRead = isRead;
		this.mDeliveryStatus = deliveryStatus;
	}

	public OPMessage(long senderId, String mMessageType, String message,
			long sendTime, String messageId) {
		this(senderId, mMessageType, message, sendTime, messageId, true, 0);
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

	public int getDeliveryStatus() {
		return mDeliveryStatus;
	}

	public void setDeliveryStatus(int status) {
		mDeliveryStatus = status;
	}

	public static OPMessage fromCursor(Cursor cursor) {
		return new OPMessage(cursor.getLong(cursor.getColumnIndex(MessageEntry.COLUMN_NAME_SENDER_ID)),
				cursor.getString(cursor.getColumnIndex(MessageEntry.COLUMN_NAME_MESSAGE_TYPE)),
				cursor.getString(cursor.getColumnIndex(MessageEntry.COLUMN_NAME_MESSAGE_TEXT)),
				cursor.getLong(cursor.getColumnIndex(MessageEntry.COLUMN_NAME_MESSAGE_TIME)),
				cursor.getString(cursor.getColumnIndex(MessageEntry.COLUMN_NAME_MESSAGE_ID)),
				cursor.getInt(cursor.getColumnIndex(MessageEntry.COLUMN_NAME_MESSAGE_READ)) == 1,
				cursor.getInt(cursor.getColumnIndex(MessageEntry.COLUMN_NAME_MESSAGE_DELIVERY_STATUS)));
	}

	public String toString() {
		return super.toString() + " from " + mFrom + " messageType "
				+ mMessageType + " message " + mMessage + " id " + mMessageId + " sender id " + mSenderId;
	}

	/**
	 * Helper function. Get from user of received message. Don't call this function for your own message.
	 * 
	 * @return
	 */
	public OPUser getFromUser() {
		return OPDataManager.getInstance().getUserById(mSenderId);
	}

}
