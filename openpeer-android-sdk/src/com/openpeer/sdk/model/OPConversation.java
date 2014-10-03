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

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.openpeer.javaapi.CallStates;
import com.openpeer.javaapi.ComposingStates;
import com.openpeer.javaapi.MessageDeliveryStates;
import com.openpeer.javaapi.OPCall;
import com.openpeer.javaapi.OPContact;
import com.openpeer.javaapi.OPContactProfileInfo;
import com.openpeer.javaapi.OPConversationThread;
import com.openpeer.javaapi.OPIdentityContact;
import com.openpeer.javaapi.OPMessage;
import com.openpeer.sdk.app.OPDataManager;
import com.openpeer.sdk.app.OPSdkConfig;
import com.openpeer.sdk.datastore.DatabaseContracts;
import com.openpeer.sdk.datastore.DatabaseContracts.WindowViewEntry;
import com.openpeer.sdk.datastore.OPContentProvider;
import com.openpeer.sdk.datastore.DatabaseContracts.MessageEntry;
import com.openpeer.sdk.utils.OPModelUtils;
import com.openpeer.sdk.model.MessageEditState;

/**
 * A session represents extact state of a conversation thread.
 * 
 * 
 */
public class OPConversation extends Observable {
    private static final String TAG = OPConversation.class.getSimpleName();

    // So if Alice and Bob, Eric in group chat, Alice then added Mike, a new
    // session is created but from Alice point of view,
    // there's only one group chat and when we construct the chat history after
    // restart,
    private List<OPUser> mParticipants = new ArrayList<OPUser>();
    private OPConversationThread mConvThread;// the active thread being used
    private OPCall currentCall;

    private boolean isRedial;
    private List<OPMessage> mMessages;
    Hashtable<String, OPMessage> undeliveredMessages;
    private String lastReadMessageId;
    private OPMessage mLastMessage;
    private Hashtable<String, OPMessage> mMessageDeliveryQueue;
    long mCbcId;
    GroupChatMode mType;

    public void setType(GroupChatMode type) {
        this.mType = type;
    }

    private boolean mWindowAttached;

    private List<SessionListener> mSessionListeners = new ArrayList<SessionListener>();

    private long mId;// database id

    private String mContextId = "";

    private OPConversationEvent mLastEvent;

    /**
     * Constructor used when starting a new conversation
     * 
     * @param users
     */
    public OPConversation(List<OPUser> users) {
        mType = OPSdkConfig.getInstance().getGroupChatMode();

        // TODO: decide if we need to keep selfcontacts in OPDataManager since
        // we can always get them through account
        mParticipants = users;
        mCbcId = OPModelUtils.getWindowId(mParticipants);
        mLastEvent = new OPConversationEvent(
                OPConversationEvent.EventTypes.NewConversation, "",
                mCbcId, 0,
                mContextId);
        OPDataManager.getDatastoreDelegate().saveParticipants(mCbcId,
                mParticipants);
        mId = OPDataManager.getDatastoreDelegate().saveConversation(this);

    }

    public OPConversation(List<OPUser> users, String contextId) {
        this(users);
        mContextId = contextId;
    }

    /**
     * Constructor used for incoming conversation
     * 
     * @param thread
     */
    public OPConversation(OPConversationThread thread) {
        mType = OPSdkConfig.getInstance().getGroupChatMode();

        mConvThread = thread;
        mContextId = mConvThread.getThreadID();
        List<OPContact> contacts = this.mConvThread.getContacts();
        for (OPContact contact : contacts) {
            if (!contact.isSelf()) {
                OPUser user = OPDataManager.getDatastoreDelegate().getUser(
                        contact,
                        mConvThread.getIdentityContactList(contact));
                // This function will also set the userId so don't worry
                mParticipants.add(user);
            }
        }
        mCbcId = OPModelUtils.getWindowId(mParticipants);
        mLastEvent = new OPConversationEvent(
                OPConversationEvent.EventTypes.NewConversation, "",
                mCbcId, 0,
                mContextId);
        OPDataManager.getDatastoreDelegate().saveParticipants(mCbcId,
                mParticipants);
        mId = OPDataManager.getDatastoreDelegate().saveConversation(this);

    }

    public void registerListener(SessionListener listener) {
        synchronized (mSessionListeners) {
            mSessionListeners.add(listener);
        }
    }

    public void unregisterListener(SessionListener listener) {
        synchronized (mSessionListeners) {
            mSessionListeners.remove(listener);
        }
    }

    public boolean isWindowAttached() {
        return mWindowAttached;
    }

    public void setWindowAttached(boolean windowAttached) {
        this.mWindowAttached = windowAttached;
    }

    public long getId() {
        return mId;
    }

    private Hashtable<String, OPMessage> getMessageDeliveryQueue() {
        if (mMessageDeliveryQueue == null) {
            mMessageDeliveryQueue = new Hashtable<String, OPMessage>();
        }
        return mMessageDeliveryQueue;
    }

    public OPMessage sendMessage(OPMessage message, boolean signMessage) {
        Log.d("test", "sending messge " + message);
        message.setRead(true);
        getThread().sendMessage(message.getMessageId(),
                message.getReplacesMessageId(),
                message.getMessageType(), message.getMessage(), signMessage);
        if (!TextUtils.isEmpty(message.getReplacesMessageId())) {
            OPDataManager.getDatastoreDelegate().updateMessage(message,
                    mCbcId, mContextId);
        } else {
            OPDataManager.getDatastoreDelegate().saveMessage(message,
                    mCbcId, mContextId, getLastEvent().getId());
        }
        return message;
    }

    public OPMessage addMessage(OPMessage message) {
        mMessages.add(message);
        return message;
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

    public OPConversation initWithContact(OPIdentityContact inContact,
            OPConversationThread inConverationThread) {
        return null;
    }

    public OPConversation initWithContacts(List<OPIdentityContact> inContacts,
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
        if (mConvThread == null) {
            mConvThread = OPConversationThread.create(
                    OPDataManager.getInstance().getSharedAccount(),
                    OPDataManager.getInstance().getSelfContacts());
            addContactToThread(mParticipants);
            if (TextUtils.isEmpty(mContextId)) {
                mContextId = mConvThread.getThreadID();
                OPDataManager.getDatastoreDelegate().updateConversation(this);
            }
        }
        return mConvThread;
    }

    public OPConversationThread selectActiveThread(
            OPConversationThread newThread) {
        // for now just use the new thread
        mConvThread = newThread;
        // TODO: implement proper logic to handle active thread selection for different conversation types
        return mConvThread;
    }

    public void getSessionId() {
    }

    public long getCurrentWindowId() {
        return mCbcId;
    }

    public OPConversationEvent getLastEvent() {
        return mLastEvent;
    }

    public void onNewEvent(OPConversationEvent event) {
        OPDataManager.getDatastoreDelegate().saveConversationEvent(mId, event);
        mLastEvent = event;

    }

    private void addContactToThread(List<OPUser> users) {
        for (OPUser user : users) {
            if (!user.getOPContact().isSelf()) {
                List<OPContactProfileInfo> contactProfiles = new ArrayList<OPContactProfileInfo>();
                OPContactProfileInfo info = new OPContactProfileInfo();

                OPContact newContact = OPContact.createFromPeerFilePublic(
                        OPDataManager
                                .getInstance().getSharedAccount(), user
                                .getPreferredContact().getPeerFilePublic()
                                .getPeerFileString());

                info.setIdentityContacts(user.getIdentityContacts());
                info.setContact(newContact);

                contactProfiles.add(info);
                mConvThread.addContacts(contactProfiles);
            }
        }
    }

    public OPConversation() {
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

    public void onMessageDeliveryStateChanged(String MessageId,
            OPContact contact) {

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

    public boolean isForUsers(List<OPUser> users) {
        return getCurrentWindowId() == OPModelUtils.getWindowId(users);
    }

    public void addParticipant(List<OPUser> users) {
        mParticipants.addAll(users);
        addContactToThread(users);
        mCbcId = OPModelUtils.getWindowId(mParticipants);
        OPDataManager.getDatastoreDelegate().saveParticipants(mCbcId,
                mParticipants);
    }

    public void onMessageReceived(OPConversationThread thread, OPMessage message) {
        if (message.getMessageType().equals(OPMessage.OPMessageType.TYPE_TEXT)) {
            OPUser user = getUserByContact(message.getFrom());
            message.setSenderId(user.getUserId());
            if (!TextUtils.isEmpty(message.getReplacesMessageId())) {
                OPDataManager.getDatastoreDelegate().updateMessage(message,
                        mCbcId, mContextId);
            } else {
                if (isWindowAttached()) {
                    message.setRead(true);
                    thread.markAllMessagesRead();
                }

                OPDataManager.getDatastoreDelegate().saveMessage(message,
                        mCbcId, mContextId,
                        getLastEvent().getId());

            }
        } else {
            Log.d("test",
                    "SessionManager onMessageReceived "
                            + message.getMessageType());
        }
    }

    protected OPUser getUserByContact(OPContact from) {
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
        switch (mType) {
        case ContextBased:
            return thread.getThreadID().equals(mConvThread.getThreadID());
        case ContactsBased:
            if (thread.getThreadID().equals(mConvThread.getThreadID())) {
                if (mCbcId != OPModelUtils
                        .getWindowIdForThread(thread)) {
                    onContactsChanged(thread);
                }
                return true;
            } else if (mCbcId == OPModelUtils
                    .getWindowIdForThread(thread)) {
                mConvThread = thread;
                return true;
            }
        case RoomBased:
            return false;
        default:
            return false;
        }

    }

    /**
     * Find the added/deleted contacts and inform listener -- probably better to be done in core
     * 
     * @param contacts
     */
    public void onContactsChanged(OPConversationThread conversationThread) {

        boolean contactsChanged = false;
        List<OPUser> users = new ArrayList<OPUser>();
        List<OPContact> contacts = conversationThread.getContacts();
        List<OPContact> newContacts = new ArrayList<OPContact>();
        List<OPUser> deletedContacts = new ArrayList<OPUser>();

        for (OPUser user : mParticipants) {
            if (!contacts.contains(user.getOPContact())) {
                deletedContacts.add(user);
            }
        }
        for (OPContact contact : contacts) {
            if (contact.isSelf()) {
                continue;
            }
            if (!hasOPContact(contact)) {
                newContacts.add(contact);
            }
        }
        if (!newContacts.isEmpty()) {
            for (OPContact contact : newContacts) {
                contactsChanged = true;
                // new contact
                List<OPIdentityContact> iContacts = mConvThread
                        .getIdentityContactList(contact);
                OPUser user = OPDataManager.getDatastoreDelegate().getUser(
                        contact, iContacts);

                // This function will also set the userId so don't worry
                mParticipants.add(user);
                OPConversationEvent event = new OPConversationEvent(
                        OPConversationEvent.EventTypes.ContactAdded,
                        user.getUserId() + "",
                        mCbcId,
                        mId,
                        mContextId);
                OPDataManager.getDatastoreDelegate().saveConversationEvent(mId,
                        event);
            }
        }

        if (!deletedContacts.isEmpty()) {
            for (OPUser user : deletedContacts) {
                contactsChanged = true;
                mParticipants.remove(user);
                OPConversationEvent event = new OPConversationEvent(
                        OPConversationEvent.EventTypes.ContactDeleted,
                        user.getUserId() + "",
                        mCbcId,
                        mId,
                        mContextId);
                OPDataManager.getDatastoreDelegate().saveConversationEvent(mId,
                        event);
            }
        }
        mCbcId = OPModelUtils.getWindowId(mParticipants);
        OPDataManager.getDatastoreDelegate().saveParticipants(mCbcId,
                mParticipants);
        synchronized (mSessionListeners) {
            for (SessionListener listener : mSessionListeners) {
                listener.onContactsChanged();
            }
        }
    }

    public void onContactComposingStateChanged(ComposingStates state,
            OPContact contact) {
        OPUser user = getUserByContact(contact);
        if (user == null) {
            Log.e(TAG, "onContactComposingStateChanged couldn't find user");
            return;
        }
        synchronized (mSessionListeners) {
            for (SessionListener listener : mSessionListeners) {
                listener.onContactComposingStateChanged(state, user);
            }
        }
    }

    /**
     * @return
     */
    public GroupChatMode getType() {
        // TODO Auto-generated method stub
        return mType;
    }

    /**
     * @param id
     */
    public void setId(long id) {
        mId = id;
    }

    /**
     * This is currently the thread id.
     * 
     * @return
     */
    public String getContextId() {
        return mContextId;
    }

    public void setContextId(String id) {
        this.mContextId = id;
    }

    /**
     * @param call
     * @param state
     */
    public void onCallStateChanged(OPCall call, CallStates state) {
        CallEvent event = new CallEvent(call.getCallID(), state,
                System.currentTimeMillis());
        if (state == CallStates.CallState_Preparing) {
            OPDataManager.getDatastoreDelegate().saveCall(call, mCbcId,
                    mContextId,
                    getLastEvent().getId());
        }
        OPDataManager.getDatastoreDelegate().saveCallEvent(call.getCallID(),
                event);
    }

    public Uri getMessagesUri() {
        switch (mType) {
        case ContactsBased:
            return OPContentProvider
                    .getContentUri(MessageEntry.URI_PATH_WINDOW_ID_URI_BASE
                            + mCbcId);
        case ContextBased:
            return OPContentProvider
                    .getContentUri(MessageEntry.URI_PATH_INFO_CONTEXT_URI_BASE
                            + mContextId);
        default:
            return null;
        }
    }

}
