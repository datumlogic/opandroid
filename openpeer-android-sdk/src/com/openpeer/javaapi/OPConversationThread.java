package com.openpeer.javaapi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.openpeer.sdk.app.OPDataManager;

import android.text.format.Time;

public class OPConversationThread {

	private long nativeClassPointer;

	// START OF JNI -- DON'T TOUCH THE SIGNATURES!!!
	public static native String toString(MessageDeliveryStates state);

	public static native String toString(ContactStates state);

	public static native String toDebugString(OPConversationThread thread,
			boolean includeCommaPrefix);

	public static native OPConversationThread create(OPAccount account,
			List<OPIdentityContact> identityContacts);

	public static native List<OPConversationThread> getConversationThreads(
			OPAccount account);

	public static native OPConversationThread getConversationThreadByID(
			OPAccount account, String threadID);

	public native long getStableID();

	public native String getThreadID();

	public native boolean amIHost();

	public native OPAccount getAssociatedAccount();

	public native List<OPContact> getContacts();

	public native List<OPIdentityContact> getIdentityContactList(
			OPContact contact);

	public native ContactStates getContactState(OPContact contact);

	public native void addContacts(
			List<OPContactProfileInfo> contactProfileInfos);

	public native void removeContacts(List<OPContact> contacts);

	// sending a message will cause the message to be delivered to all the
	// contacts currently in the conversation
	public native void sendMessage(String messageID, String messageType,
			String message, boolean signMessage);

	// returns false if the message ID is not known
	public native OPMessage getMessage(String messageID);

	// returns false if the message ID is not known
	public native MessageDeliveryStates getMessageDeliveryState(String messageID);

	// END OF JNI

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		return o instanceof OPConversationThread
				&& this.nativeClassPointer == ((OPConversationThread) o).nativeClassPointer;
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
