package com.openpeer.javaapi;

import java.util.List;

import android.text.format.Time;
import android.util.Log;

public class OPConversationThread {

	private long nativeClassPointer;
	
	public static native String toString(MessageDeliveryStates state);
	public static native String toString(ContactConnectionStates state);

	public static native String toDebugString(OPConversationThread thread, boolean includeCommaPrefix);
	
	public static native OPConversationThread create(
                                         OPAccount account,
                                         List<OPIdentityContact> identityContactsOfSelf
                                         );

	public static native List<OPConversationThread> getConversationThreads(OPAccount account);
	
	public static native OPConversationThread getConversationThreadByID(
                                                            OPAccount account,
                                                            String threadID
                                                            );
	public native long getID();

	public native String getThreadID();

	public native boolean amIHost();
	
	public native OPAccount getAssociatedAccount();

	public native List<OPContact> getContacts();

	public native List<OPIdentityContact> getIdentityContactList(OPContact contact);
	
	public native ContactConnectionStates getContactConnectionState(OPContact contact);


	public native void addContacts(List<OPContactProfileInfo> contactProfileInfos);
	
	public native void removeContacts(List<OPContact> contacts);
	
	public static native OPElement createEmptyStatus();
	
	public native OPElement getContactStatus(OPContact contact);
	
	public native void setStatusInThread(OPElement contactStatusInThreadOfSelf);

    // sending a message will cause the message to be delivered to all the contacts currently in the conversation
	public native void sendMessage(
                             String messageID,
                             String replacesMessageID,
                             String messageType,
                             String message,
                             boolean signMessage
                             );

    // returns false if the message ID is not known
	public native OPMessage getMessage(
							String messageID
                            );

    // returns false if the message ID is not known
	public native MessageDeliveryStates getMessageDeliveryState(
										 String messageID
                                         );
	
	public native void markAllMessagesRead();
	
    private native void releaseCoreObjects(); 
    
    protected void finalize() throws Throwable {
    	
    	if (nativeClassPointer != 0)
    	{
    		Log.d("output", "Cleaning conversation thread core objects");
    		releaseCoreObjects();
    	}
    		
    	super.finalize();
    }
}
