package com.openpeer.javaapi;

import java.util.List;

import android.text.format.Time;

public class OPConversationThread {

	public static native String toString(MessageDeliveryStates state);
	public static native String toString(ContactStates state);

	public static native String toDebugString(OPConversationThread thread, Boolean includeCommaPrefix);
	
	public static native OPConversationThread create(
                                         OPAccount account,
                                         String profileBundleEl
                                         );

	public static native List<OPConversationThread> getConversationThreads(OPAccount account);
	
	public static native OPConversationThread getConversationThreadByID(
                                                            OPAccount account,
                                                            String threadID
                                                            );
	public native String getStableID();

	public native String getThreadID();

	public native Boolean amIHost();
	
	public native OPAccount getAssociatedAccount();

	public native List<OPContact> getContacts();

	public native String getProfileBundle(OPContact contact);
	
	public native ContactStates getContactState(OPContact contact);

	public native void addContacts(List<OPContactProfileInfo> contactProfileInfos);
	
	public native void removeContacts(List<OPContact> contacts);

    // sending a message will cause the message to be delivered to all the contacts currently in the conversation
	public native void sendMessage(
                             String messageID,
                             String messageType,
                             String message
                             );

    // returns false if the message ID is not known
	public native Boolean getMessage(
							String messageID,
                            OPContact outFrom,
                            String outMessageType,
                            String outMessage,
                            Time outTime
                            );

    // returns false if the message ID is not known
	public native Boolean getMessageDeliveryState(
										 String messageID,
                                         MessageDeliveryStates outDeliveryState
                                         );
}
