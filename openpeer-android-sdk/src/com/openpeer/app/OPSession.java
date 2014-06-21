package com.openpeer.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import android.util.Log;

import com.openpeer.delegates.CallbackHandler;
import com.openpeer.javaapi.OPCall;
import com.openpeer.javaapi.OPCallDelegate;
import com.openpeer.javaapi.OPContact;
import com.openpeer.javaapi.OPContactProfileInfo;
import com.openpeer.javaapi.OPConversationThread;
import com.openpeer.javaapi.OPIdentityContact;
import com.openpeer.javaapi.OPMessage;

public class OPSession {

	private List<OPIdentityContact> mParticipants;
	private OPConversationThread mConvThread;
	private OPCall currentCall;

	private boolean isRedial;
	private List<OPMessage> mMessages;
	Hashtable<String, OPMessage> undeliveredMessages;
	private String lastReadMessageId;

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
	 * Get the message that's displayed. Used to decide from which message to
	 * display
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

	public OPConversationThread getThread() {
		return mConvThread;
	}

	public OPConversationThread getThreadForContact(OPIdentityContact contact) {
		return null;
	}

	public boolean hasContact(OPIdentityContact contact) {
		if (mParticipants == null)
			return false;
		for (OPIdentityContact oc : mParticipants) {
			if (oc.getStableID().equals(contact.getStableID())) {
				return true;
			}
		}
		return false;
	}

	public OPSession(OPConversationThread thread) {
		mConvThread = thread;

	}

	public OPSession(OPIdentityContact contact) {
		List<OPIdentityContact> selfContacts = new ArrayList<OPIdentityContact>();
		for (OPIdentityContact ic : OPDataManager.getInstance()
				.getSelfContacts().values()) {
			selfContacts.add(ic);
		}

		mConvThread = OPConversationThread.create(OPDataManager.getInstance()
				.getSharedAccount(), selfContacts);

		mParticipants = new ArrayList<OPIdentityContact>();
		List<OPContactProfileInfo> contactProfiles = new ArrayList<OPContactProfileInfo>();
		OPContactProfileInfo info = new OPContactProfileInfo();

		OPContact newContact = OPContact.createFromPeerFilePublic(OPDataManager
				.getInstance().getSharedAccount(), contact.getPeerFilePublic()
				.getPeerFileString());

		mParticipants.add(contact);
		info.setIdentityContacts(mParticipants);
		info.setContact(newContact);

		contactProfiles.add(info);
		mConvThread.addContacts(contactProfiles);
	}

	public OPCall placeCall(OPIdentityContact contact, OPCallDelegate delegate,
			boolean includeAudio, boolean includeVideo) {

		OPContact newContact = OPContact.createFromPeerFilePublic(OPDataManager
				.getInstance().getSharedAccount(), contact.getPeerFilePublic()
				.getPeerFileString());
		Log.d("test",
				"contact "
						+ newContact
						+ " Contacts in thread "
						+ Arrays.deepToString(mConvThread.getContacts()
								.toArray()));
		currentCall = OPCall.placeCall(mConvThread, newContact, includeAudio,
				includeVideo);
		CallbackHandler.getInstance().registerCallDelegate(currentCall,
				delegate);
		return currentCall;

	}
}
