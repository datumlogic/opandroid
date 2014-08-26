/*******************************************************************************
 *
 *  Copyright (c) 2014 , Hookflash Inc.
 *  All rights reserved.
 *  
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *  
 *  1. Redistributions of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 *  
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 *  ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *  
 *  The views and conclusions contained in the software and documentation are those
 *  of the authors and should not be interpreted as representing official policies,
 *  either expressed or implied, of the FreeBSD Project.
 *******************************************************************************/
package com.openpeer.sdk.model;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Observable;

import android.os.Bundle;
import android.util.Log;

import com.openpeer.javaapi.MessageDeliveryStates;
import com.openpeer.javaapi.OPCall;
import com.openpeer.javaapi.OPContact;
import com.openpeer.javaapi.OPContactProfileInfo;
import com.openpeer.javaapi.OPConversationThread;
import com.openpeer.javaapi.OPIdentityContact;
import com.openpeer.javaapi.OPMessage;
import com.openpeer.sdk.app.OPDataManager;
import com.openpeer.sdk.utils.OPModelUtils;

/**
 * A session represents extact state of a conversation thread.
 * 
 * 
 */
public class OPSession extends Observable {

	// So if Alice and Bob, Eric in group chat, Alice then added Mike, a new
	// session is created but from Alice point of view,
	// there's only one group chat and when we construct the chat history after
	// restart,
	OPSession parent;
	private List<OPUser> mParticipants = new ArrayList<OPUser>();
	private OPConversationThread mConvThread;
	private OPCall currentCall;

	private boolean isRedial;
	private List<OPMessage> mMessages;
	Hashtable<String, OPMessage> undeliveredMessages;
	private String lastReadMessageId;
	private OPMessage mLastMessage;
	private Hashtable<String, OPMessage> mMessageDeliveryQueue;
	long mCurrentWindowId;
	private boolean mWindowAttached;

	public boolean isWindowAttached() {
		return mWindowAttached;
	}

	public void setWindowAttached(boolean windowAttached) {
		this.mWindowAttached = windowAttached;
	}

	/**
	 * This is currently the thread id.
	 * 
	 * @return
	 */
	public String getId() {
		return mConvThread.getThreadID();
	}

	private Hashtable<String, OPMessage> getMessageDeliveryQueue() {
		if (mMessageDeliveryQueue == null) {
			mMessageDeliveryQueue = new Hashtable<String, OPMessage>();
		}
		return mMessageDeliveryQueue;
	}

	public OPMessage sendMessage(OPMessage message, boolean signMessage) {
		Log.d("test", "sending messge " + message);
		getThread().sendMessage(message.getMessageId(),
				message.getMessageType(), message.getMessage(), signMessage);
		OPDataManager.getDatastoreDelegate().saveMessage(message, mCurrentWindowId, getThread().getThreadID());
		return message;
	}

	public OPMessage addMessage(OPMessage message) {
		mMessages.add(message);
		return message;
	}

	public List<OPMessage> getUnreadMessages() {
		if (mMessages == null || mMessages.size() == 0) {
			return null;
		}
		if (lastReadMessageId == null)
			return mMessages;
		int lastReadMessageIndex = -1;
		for (int i = mMessages.size() - 1; i > 0; i--) {
			OPMessage message = mMessages.get(i);
			if (lastReadMessageId.equals(message.getMessageId())) {
				lastReadMessageIndex = i;
			}
		}
		if (lastReadMessageIndex > -1
				&& lastReadMessageIndex < mMessages.size() - 1) {
			return mMessages.subList(lastReadMessageIndex, mMessages.size());
		} else {
			return null;
		}
	}

	/**
	 * Get the message that's displayed. Used to decide from which message to display
	 * 
	 * @return
	 */
	public String getReadMessageId() {
		return lastReadMessageId;
	}

	public void setReadMessageId(String readMessageId) {
		this.lastReadMessageId = readMessageId;
	}

	// @property (strong) NSMutableSet* sessionIdsHistory;
	// @property (strong) NSMutableArray* arrayMergedConversationThreads;
	public OPCall getCurrentCall() {
		return currentCall;
	}

	public void setCurrentCall(OPCall currentCall) {
		this.currentCall = currentCall;
	}

	public OPSession initWithContact(OPIdentityContact inContact,
			OPConversationThread inConverationThread) {
		return null;
	}

	public OPSession initWithContacts(List<OPIdentityContact> inContacts,
			OPConversationThread inConverationThread) {
		return null;

	}

	/**
	 * If an session existed for an incoming message and thread is null, set thread.
	 * 
	 * @param thread
	 */
	public void setThread(OPConversationThread thread) {
		mConvThread = thread;
	}

	public OPConversationThread getThread() {
		if(mConvThread==null){
			mConvThread = OPConversationThread.create(OPDataManager.getInstance()
					.getSharedAccount(), OPDataManager.getInstance()
					.getSelfContacts());
			addContactToThread(mParticipants);			
		}
		return mConvThread;
	}

	public OPSession(OPConversationThread thread) {
		mConvThread = thread;
		List<OPContact> contacts = this.mConvThread.getContacts();
		for (OPContact contact : contacts) {
			if (contact.isSelf()) {
				continue;
			}
			// new contact
			OPUser user = new OPUser(contact, mConvThread.getIdentityContactList(contact));
			user = OPDataManager.getDatastoreDelegate().saveUser(user);
			// This function will also set the userId so don't worry
			mParticipants.add(user);
		}
		mCurrentWindowId = OPModelUtils.getWindowId(mParticipants);
		OPDataManager.getDatastoreDelegate().saveWindow(mCurrentWindowId, mParticipants);
	}

	public void getSessionId() {
	}

	public long getCurrentWindowId() {
		return mCurrentWindowId;
	}

	public OPSession(List<OPUser> users) {
		// TODO: decide if we need to keep selfcontacts in OPDataManager since
		// we can always get them through account
		mParticipants = users;
		mCurrentWindowId = OPModelUtils.getWindowId(mParticipants);
		OPDataManager.getDatastoreDelegate().saveWindow(mCurrentWindowId, mParticipants);
	}

	private void addContactToThread(List<OPUser> users) {
		for (OPUser user : users) {
			List<OPContactProfileInfo> contactProfiles = new ArrayList<OPContactProfileInfo>();
			OPContactProfileInfo info = new OPContactProfileInfo();

			OPContact newContact = OPContact.createFromPeerFilePublic(OPDataManager
					.getInstance().getSharedAccount(), user.getPreferredContact().getPeerFilePublic()
					.getPeerFileString());

			info.setIdentityContacts(user.getIdentityContacts());
			info.setContact(newContact);

			contactProfiles.add(info);
			mConvThread.addContacts(contactProfiles);
		}
	}

	public OPSession() {
		// TODO Auto-generated constructor stub
	}

	public OPCall placeCall(OPUser user,
			boolean includeAudio, boolean includeVideo) {

		OPContact newContact = user.getOPContact();

		OPCall call = OPCall.placeCall(getThread(), newContact, includeAudio,
				includeVideo);
		// CallbackHandler.getInstance().registerCallDelegate(call,
		// delegate);
		return call;

	}

	public List<OPUser> getParticipants() {
		// TODO Auto-generated method stub
		return mParticipants;
	}

	public OPMessage getLastMessage() {
		// TODO Auto-generated method stub
		return mLastMessage;
	}

	public void setLastMessage(OPMessage lastMessage) {
		mLastMessage = lastMessage;
	}

	public void onMessagePushNeeded(String MessageId, OPContact contact) {

	}

	public void onMessageDeliveryStateChanged(String MessageId, OPContact contact) {

	}

	public void onMessageDeliveryFailed(String MessageId, OPContact contact) {

	}

	public interface MessageDeliveryCallback {
		public void onMessageDeliveryStateChange(String messageId,
				MessageDeliveryStates state);
	}

	boolean hasOPContact(OPContact contact) {
		for (OPUser user : mParticipants) {
			if (user.isSame(contact)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Find the added/deleted contacts and inform listener -- probably better to be done in core
	 * 
	 * @param contacts
	 */
	public void onContactsChanged() {
		boolean contactsChanged = false;
		List<OPUser> users = new ArrayList<OPUser>();
		List<OPContact> contacts = this.mConvThread.getContacts();
		for (OPContact contact : contacts) {
			if (contact.isSelf()) {
				continue;
			}
			if (!hasOPContact(contact)) {
				contactsChanged = true;
				// new contact
				OPUser user = new OPUser(contact, mConvThread.getIdentityContactList(contact));
				// This function will also set the userId so don't worry
				mParticipants.add(OPDataManager.getDatastoreDelegate().saveUser(user));
			}
		}
		if (contactsChanged) {
			Bundle data = new Bundle();
			notify(SessionEvent.SessionEvent_Contact_Change, data);
		}
	}

	void notify(SessionEvent event, Bundle data) {
		notifyObservers(new ChangeEvent(event, data));
	}

	public enum SessionEvent {
		SessionEvent_Contact_Change, SessionEvent_Message_Delivery_Report;

		SessionEvent() {
		}

	};

	public static class ChangeEvent {

		SessionEvent event;
		Bundle data;

		public ChangeEvent(SessionEvent event, Bundle data) {
			super();
			this.event = event;
			this.data = data;
		}

		public SessionEvent getEvent() {
			return event;
		}

		public Bundle getData() {
			return data;
		}

	}

	public boolean isForUsers(long[] userIDs) {
		return getCurrentWindowId() == OPModelUtils.getWindowId(userIDs);
	}

	public void addParticipant(List<OPUser> users) {
		mParticipants.addAll(users);
		addContactToThread(users);
		mCurrentWindowId = OPModelUtils.getWindowId(mParticipants);
		OPDataManager.getDatastoreDelegate().saveWindow(mCurrentWindowId, mParticipants);
	}

	public void onMessageReceived(OPMessage message) {
		if (message.getMessageType().equals(OPMessage.OPMessageType.TYPE_TEXT)) {
			OPUser user = getUserByContact(message.getFrom());
			message.setSenderId(user.getUserId());
			if (isWindowAttached()) {
				message.setRead(true);
			}
			OPDataManager.getDatastoreDelegate().saveMessage(message, mCurrentWindowId, getThread().getThreadID());
		} else {
			Log.d("test", "SessionManager onMessageReceived " + message.getMessageType());
		}
	}

	private OPUser getUserByContact(OPContact from) {
		for (OPUser user : mParticipants) {
			if (user.getPeerUri().equals(from.getPeerURI())) {
				return user;
			}
		}
		return null;
	}

	public OPUser getUserBySenderId(long senderId) {
		for (OPUser user : mParticipants) {
			if (user.getUserId() == senderId) {
				return user;
			}
		}
		return null;
	}

	public long[] getParticipantIDs() {
		long IDs[] = new long[mParticipants.size()];
		for (int i = 0; i < IDs.length; i++) {
			OPUser user = mParticipants.get(i);
			IDs[i] = user.getUserId();
		}
		return IDs;
	}

	public boolean isForThread(OPConversationThread thread) {
		// TODO: create appropriate logic,e.g. based on windowId
		if (thread.getThreadID().equals(getThread().getThreadID())) {
			return true;
		}
		return false;
	}
}
