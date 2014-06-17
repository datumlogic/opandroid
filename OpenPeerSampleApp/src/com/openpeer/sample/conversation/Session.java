package com.openpeer.sample.conversation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.util.Log;

import com.openpeer.app.OPDataManager;
import com.openpeer.delegates.CallbackHandler;
import com.openpeer.javaapi.OPCall;
import com.openpeer.javaapi.OPCallDelegate;
import com.openpeer.javaapi.OPContact;
import com.openpeer.javaapi.OPContactProfileInfo;
import com.openpeer.javaapi.OPConversationThread;
import com.openpeer.javaapi.OPIdentityContact;
import com.openpeer.javaapi.OPMessage;

public class Session {

	private List<OPIdentityContact> mParticipants;
	private OPConversationThread mConvThread;
	private OPCall currentCall;

	private boolean isRedial;
	private List<OPMessage> messageArray;
	private String readMessageId;

	/**
	 * Get the message that's displayed. Used to decide from which message to
	 * display
	 * 
	 * @return
	 */
	public String getReadMessageId() {
		return readMessageId;
	}

	public void setReadMessageId(String readMessageId) {
		this.readMessageId = readMessageId;
	}

	// @property (strong) NSMutableSet* sessionIdsHistory;
	// @property (strong) NSMutableArray* arrayMergedConversationThreads;
	public OPCall getCurrentCall() {
		return currentCall;
	}

	public void setCurrentCall(OPCall currentCall) {
		this.currentCall = currentCall;
	}

	public Session initWithContact(OPIdentityContact inContact,
			OPConversationThread inConverationThread) {
		return null;
	}

	public Session initWithContacts(List<OPIdentityContact> inContacts,
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

	public Session(OPIdentityContact contact) {
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
