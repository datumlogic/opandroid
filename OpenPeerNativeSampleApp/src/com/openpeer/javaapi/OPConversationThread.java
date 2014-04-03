/*
 
 Copyright (c) 2014, SMB Phone Inc.
 All rights reserved.
 
 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:
 
 1. Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution.
 
 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 
 The views and conclusions contained in the software and documentation are those
 of the authors and should not be interpreted as representing official policies,
 either expressed or implied, of the FreeBSD Project.
 
 */

package com.openpeer.javaapi;

import java.util.List;

import android.text.format.Time;

public class OPConversationThread {

	public static native String toString(MessageDeliveryStates state);
	public static native String toString(ContactStates state);

	public static native String toDebugString(OPConversationThread thread, boolean includeCommaPrefix);
	
	public static native OPConversationThread create(
                                         OPAccount account,
                                         List<OPIdentityContact> identityContacts
                                         );

	public static native List<OPConversationThread> getConversationThreads(OPAccount account);
	
	public static native OPConversationThread getConversationThreadByID(
                                                            OPAccount account,
                                                            String threadID
                                                            );
	public native long getStableID();

	public native String getThreadID();

	public native boolean amIHost();
	
	public native OPAccount getAssociatedAccount();

	public native List<OPContact> getContacts();

	public native List<OPIdentityContact> getIdentityContactList(OPContact contact);
	
	public native ContactStates getContactState(OPContact contact);

	public native void addContacts(List<OPContactProfileInfo> contactProfileInfos);
	
	public native void removeContacts(List<OPContact> contacts);

    // sending a message will cause the message to be delivered to all the contacts currently in the conversation
	public native void sendMessage(
                             String messageID,
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
}
