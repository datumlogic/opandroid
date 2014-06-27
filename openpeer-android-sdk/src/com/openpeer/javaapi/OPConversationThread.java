package com.openpeer.javaapi;

import java.util.Arrays;
import java.util.List;

import com.openpeer.app.OPDataManager;

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

	long mWindowId;

	public long getCurrentWindowId() {
		return mWindowId;
	}

	public void setWindowId() {

		List<OPContact> contacts = getContacts();
		long IDs[] = new long[contacts.size()];

		int i = 0;
		for (OPContact contact : getContacts()) {
			// TODO: implement proper identity contact selection algorithm
			OPIdentityContact iContact = this.getIdentityContactList(contact)
					.get(0);
			IDs[i] = OPDataManager.getInstance().getUserIdForContact(contact,
					iContact);
			i++;
		}
		Arrays.sort(IDs);
		mWindowId = IDs.hashCode();
	}

	public long getNativeClassPtr() {
		return nativeClassPointer;
	}

}
