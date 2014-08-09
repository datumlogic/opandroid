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

	// sending a message will cause the message to be delivered to all the
	// contacts currently in the conversation
	public native void sendMessage(String messageID, String messageType, String message, boolean signMessage);

	// returns false if the message ID is not known
	private native OPMessage getMessage(String messageID);

	// returns false if the message ID is not known
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
		// TODO Auto-generated method stub
		return o instanceof OPConversationThread && this.nativeClassPointer == ((OPConversationThread) o).nativeClassPointer;
	}

	public List<OPIdentityContact> getIdentityContacts() {
		List<OPIdentityContact> contacts = new ArrayList<OPIdentityContact>();
		for (OPContact contact : getContacts()) {
			List<OPIdentityContact> iContacts = getIdentityContactList(contact);
			if (!iContacts.isEmpty())
				contacts.add(iContacts.get(0));
		}
		return contacts;
	}

	public long getNativeClassPtr() {
		return nativeClassPointer;
	}

}
