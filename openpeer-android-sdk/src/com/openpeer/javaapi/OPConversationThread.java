package com.openpeer.javaapi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.openpeer.sdk.app.OPDataManager;
import com.openpeer.sdk.model.OPUser;

import android.text.format.Time;
import android.util.Log;

public class OPConversationThread {

	/**
	 * Helper function to make sure required fields are populated.
	 * 
	 * @param messageID
	 * @return
	 */
	public OPMessage getMessageById(String messageID) {
		OPMessage message = getMessage(messageID);
		OPContact from = message.getFrom();
		OPUser user = OPDataManager.getInstance().getUserForMessage(from, this);
		message.setSenderId(user.getUserId());
		return message;
	}

	private long nativeClassPointer;

	// START OF JNI -- DON'T TOUCH THE SIGNATURES!!!
	public static native String toString(MessageDeliveryStates state);

	public static native String toString(ContactStates state);

	public static native String toDebugString(OPConversationThread thread, boolean includeCommaPrefix);

	public static native OPConversationThread create(OPAccount account, List<OPIdentityContact> identityContacts);

	public static native List<OPConversationThread> getConversationThreads(OPAccount account);

	public static native OPConversationThread getConversationThreadByID(OPAccount account, String threadID);

	public native long getStableID();

	public native String getThreadID();

	public native boolean amIHost();

	public native OPAccount getAssociatedAccount();

	public native List<OPContact> getContacts();

	public native List<OPIdentityContact> getIdentityContactList(OPContact contact);

	public native ContactStates getContactState(OPContact contact);

	public native void addContacts(List<OPContactProfileInfo> contactProfileInfos);

	public native void removeContacts(List<OPContact> contacts);

	/**
	 * sending a message will cause the message to be delivered to all the contacts currently in the conversation
	 * 
	 * @param messageID
	 * @param messageType
	 * @param message
	 * @param signMessage
	 *            whether or not to sign the message
	 */
	public native void sendMessage(String messageID, String messageType, String message, boolean signMessage);

	private native OPMessage getMessage(String messageID);

	public native MessageDeliveryStates getMessageDeliveryState(String messageID);

	private native void releaseCoreObjects();

	protected void finalize() throws Throwable {

		if (nativeClassPointer != 0) {
			Log.d("output", "Cleaning conversation thread core objects");
			releaseCoreObjects();
		}

		super.finalize();
	}

	// END OF JNI

	@Override
	public boolean equals(Object o) {
		return o instanceof OPConversationThread && this.getStableID() == ((OPConversationThread) o).getStableID();
	}

}
