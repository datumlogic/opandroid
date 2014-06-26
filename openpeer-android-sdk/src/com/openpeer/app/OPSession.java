package com.openpeer.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import android.util.Log;

import com.openpeer.delegates.CallbackHandler;
import com.openpeer.javaapi.MessageDeliveryStates;
import com.openpeer.javaapi.OPCall;
import com.openpeer.javaapi.OPCallDelegate;
import com.openpeer.javaapi.OPContact;
import com.openpeer.javaapi.OPContactProfileInfo;
import com.openpeer.javaapi.OPConversationThread;
import com.openpeer.javaapi.OPIdentityContact;
import com.openpeer.javaapi.OPMessage;

/**
 * A session represents extact state of a conversation thread.Any state change
 * of a conversation thread will cause an existing session to terminate and a
 * new session to be crated. The state change includes: -- Adding/Removing a
 * contact from the conversation thread.
 * 
 * @author brucexia
 * 
 */
public class OPSession {
	private static final int DEFAULT_NUM_MESSAGES_TO_LOAD = 30;
	// When a new contact is added to the session, a new session is created and
	// its parent is set to the current session.
	// So if Alice and Bob, Eric in group chat, Alice then added Mike, a new
	// session is created but from Alice point of view,
	// there's only one group chat and when we construct the chat history after
	// restart,
	OPSession parent;
	// A window identiti
	private long mUniqueId;
	private List<OPIdentityContact> mParticipants;
	private OPConversationThread mConvThread;
	private OPCall currentCall;

	private boolean isRedial;
	private List<OPMessage> mMessages;
	Hashtable<String, OPMessage> undeliveredMessages;
	private String lastReadMessageId;
	private OPMessage mLastMessage;
	private Hashtable<String, OPMessage> mMessageDeliveryQueue;

	Hashtable<String, OPMessage> getMessageDeliveryQueue() {
		if (mMessageDeliveryQueue == null) {
			mMessageDeliveryQueue = new Hashtable<String, OPMessage>();
		}
		return mMessageDeliveryQueue;
	}

	public OPMessage sendMessage(OPMessage message, boolean signMessage) {
		mConvThread.sendMessage(message.getMessageId(),
				message.getMessageType(), message.getMessage(), signMessage);
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

	/**
	 * If an session existed for an incoming message and thread is null, set
	 * thread.
	 * 
	 * @param thread
	 */
	public void setThread(OPConversationThread thread) {
		mConvThread = thread;
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

	public OPSession(List<OPIdentityContact> contacts) {
		mParticipants = contacts;
	}

	public void getSessionId(){
	}
	public long getCurrentWindowId() {
		if (mUniqueId == 0) {
			long IDs[] = new long[mParticipants.size()];
			for (int i = 0; i < mParticipants.size() - 1; i++) {
				OPIdentityContact contact = mParticipants.get(i);
				IDs[i] = contact.getUserId();
			}
			Arrays.sort(IDs);
			mUniqueId = IDs.hashCode();
		}
		return mUniqueId;
	}

	List<OPMessage> getHistoryMessages() {

		return null;
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
		Log.d("test",
				"OPContact peerURI "
						+ newContact.getPeerURI()
						+ " peerfile "
						+ newContact.getPeerFilePublic()
						+ " IdentityContact File identityURi "
						+ contact.getIdentityURI()
						+ " peerFile equal "
						+ newContact.getPeerFilePublic().equals(
								contact.getPeerFilePublic()));

		mParticipants.add(contact);
		info.setIdentityContacts(mParticipants);
		info.setContact(newContact);

		contactProfiles.add(info);
		mConvThread.addContacts(contactProfiles);
	}

	public OPSession() {
		// TODO Auto-generated constructor stub
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

	public List<OPIdentityContact> getParticipants() {
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

	public void onMessageDeliverd(String MessageId, OPContact contact) {

	}

	public void onMessageDeliveryFailed(String MessageId, OPContact contact) {

	}

	public interface MessageDeliveryCallback {
		public void onMessageDeliveryStateChange(String messageId,
				MessageDeliveryStates state);
	}
}
