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
import com.openpeer.sdk.utils.OPModelUtils;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Observable;

/**
 * A session represents exact state of a conversation thread.
 */
public class OPConversation extends Observable {
    private static final String TAG = OPConversation.class.getSimpleName();

    // So if Alice and Bob, Eric in group chat, Alice then added Mike, a new
    // session is created but from Alice point of view,
    // there's only one group chat and when we construct the chat history after
    // restart,
    private List<OPUser> mParticipants = new ArrayList<OPUser>();
    private OPConversationThread mConvThread;// the active thread being used
    private Hashtable<String, OPConversationThread> mThreadList = new Hashtable<>();
    private OPCall mCurrentCall;

    private boolean isRedial;
    private List<OPMessage> mMessages;
    private Hashtable<String, OPMessage> undeliveredMessages;
    private String lastReadMessageId;
    private OPMessage mLastMessage;
    private Hashtable<String, OPMessage> mMessageDeliveryQueue;
    private long mCbcId;
    private GroupChatMode mType;

    public void setType(GroupChatMode type) {
        this.mType = type;
    }

    private List<SessionListener> mSessionListeners = new ArrayList<SessionListener>();

    private long mId;// database id

    private String mContextId = "";

    private OPConversationEvent mLastEvent;

    public OPConversation() {
    }

    /**
     * @param users
     * @param contextId
     */
    public OPConversation(List<OPUser> users, String contextId) {
        this(users, contextId, OPSdkConfig.getInstance().getGroupChatMode());
    }

    /**
     * Constructor used when starting a new conversation
     *
     * @param users
     */
    public OPConversation(List<OPUser> users) {
        this(users, "", OPSdkConfig.getInstance().getGroupChatMode());

    }

    public OPConversation(List<OPUser> users, String contextId, GroupChatMode mode) {
        mContextId = contextId;
        mType = mode;
        mParticipants = users;
        mCbcId = OPModelUtils.getWindowId(mParticipants);
        mLastEvent = new OPConversationEvent(this,
                                             OPConversationEvent.EventTypes.NewConversation,
                                             "");
    }

    public long save() {
        mId = OPDataManager.getDatastoreDelegate().saveConversation(this);

        onNewEvent(mLastEvent);
        return mId;
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
            this,
            OPConversationEvent.EventTypes.NewConversation,
            "");
        ConversationManager.getInstance().cacheThread(mCbcId,thread);
    }

    public void registerListener(SessionListener listener) {
        synchronized (mSessionListeners) {
            Log.d(TAG, "registerListener " + listener);
            mSessionListeners.add(listener);
        }
    }

    public void unregisterListener(SessionListener listener) {
        synchronized (mSessionListeners) {
            Log.d(TAG, "unregisterListener " + listener);
            mSessionListeners.remove(listener);
        }
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
        getThread(true).sendMessage(message.getMessageId(),
                                message.getReplacesMessageId(),
                                message.getMessageType(), message.getMessage(), signMessage);
        if (!TextUtils.isEmpty(message.getReplacesMessageId())) {
            OPDataManager.getDatastoreDelegate().updateMessage(message, this);
        } else {
            OPDataManager.getDatastoreDelegate().saveMessage(message, this);
        }
        return message;
    }

    private OPMessage addMessage(OPMessage message) {
        mMessages.add(message);
        return message;
    }

    /**
     * Get the message that's displayed. Used to decide from which message to display
     *
     * @return
     */
    private String getReadMessageId() {
        return lastReadMessageId;
    }

    public void setReadMessageId(String readMessageId) {
        this.lastReadMessageId = readMessageId;
    }

    // @property (strong) NSMutableSet* sessionIdsHistory;
    // @property (strong) NSMutableArray* arrayMergedConversationThreads;
    public OPCall getCurrentCall() {
        return mCurrentCall;
    }

    public void setCurrentCall(OPCall mCurrentCall) {
        this.mCurrentCall = mCurrentCall;
    }

    /**
     * If an session existed for an incoming message and thread is null, set thread.
     *
     * @param thread
     */
    public void setThread(OPConversationThread thread) {
        mConvThread = thread;
    }

    public OPConversationThread getThread(boolean createIfNo) {
        if (mConvThread == null) {
            mConvThread = OPConversationThread.create(
                OPDataManager.getInstance().getSharedAccount(),
                OPDataManager.getInstance().getSelfContacts());
            addContactsToThread(mParticipants);
            if (TextUtils.isEmpty(mContextId)) {
                mContextId = mConvThread.getThreadID();
                OPDataManager.getDatastoreDelegate().updateConversation(this);
            }
            ConversationManager.getInstance().cacheThread(mCbcId, mConvThread);
        }
        return mConvThread;
    }

    private OPConversationThread selectActiveThread(OPConversationThread newThread) {
        // for now just use the new thread
        if (mConvThread == null) {
            mConvThread = newThread;
        } else {
            if (mConvThread.getThreadID().equals(newThread.getThreadID())) {
                return mConvThread;
            } else {
                mThreadList.put(mConvThread.getThreadID(), mConvThread);
                mConvThread = newThread;
            }
        }
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
        OPDataManager.getDatastoreDelegate().saveConversationEvent(event);
        mLastEvent = event;

    }

    private void addContactsToThread(List<OPUser> users) {
        List<OPContactProfileInfo> contactProfiles = new ArrayList<>();
        for (OPUser user : users) {
            if (!user.getOPContact().isSelf()) {
                OPContactProfileInfo info = new OPContactProfileInfo();

                OPContact newContact = user.getOPContact();
                info.setIdentityContacts(user.getIdentityContacts());
                info.setContact(newContact);

                contactProfiles.add(info);
            }
        }
        mConvThread.addContacts(contactProfiles);
    }

    public OPCall placeCall(OPUser user,
                            boolean includeAudio, boolean includeVideo) {

        OPContact newContact = user.getOPContact();

        OPCall call = OPCall.placeCall(getThread(true), newContact, includeAudio,
                                       includeVideo);
        return call;
    }

    /**
     * return the current participants, excludign yourself.
     *
     * @return
     */
    public List<OPUser> getParticipants() {
        return mParticipants;
    }

    private OPMessage getLastMessage() {
        return mLastMessage;
    }

    private void setLastMessage(OPMessage lastMessage) {
        mLastMessage = lastMessage;
    }

    private void onMessagePushNeeded(String MessageId, OPContact contact) {

    }

    private void onMessageDeliveryStateChanged(String MessageId,
                                               OPContact contact) {

    }

    private void onMessageDeliveryFailed(String MessageId, OPContact contact) {

    }

    private interface MessageDeliveryCallback {
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

    public void addParticipants(List<OPUser> users) {
        mParticipants.addAll(users);
        if (mConvThread != null) {
            addContactsToThread(users);
        } else {
            mCbcId = OPModelUtils.getWindowId(mParticipants);

            OPConversationEvent event = new OPConversationEvent(
                this,
                OPConversationEvent.EventTypes.ContactsAdded,
                "");
            onNewEvent(event);
            notifyContactsChanged();
        }
    }

    public void removeParticipants(List<OPUser> users) {
        mParticipants.removeAll(users);
        List<OPContact> contacts = new ArrayList<>();
        if (mConvThread != null) {
            for (OPUser user : users) {
                contacts.add(user.getOPContact());
            }
            mConvThread.removeContacts(contacts);
        } else {
            mCbcId = OPModelUtils.getWindowId(mParticipants);
            OPConversationEvent event = new OPConversationEvent(
                this,
                OPConversationEvent.EventTypes.ContactsRemoved,
                null);
            onNewEvent(event);

            notifyContactsChanged();
        }
    }

    public void onMessageReceived(OPConversationThread thread, OPMessage message) {
        if (message.getMessageType().equals(OPMessage.OPMessageType.TYPE_TEXT)) {
            OPContact opContact = message.getFrom();
            OPUser user = OPDataManager.getDatastoreDelegate().
                getUserByPeerUri(opContact.getPeerURI());
            if (user == null) {
                List<OPIdentityContact> contacts = thread.getIdentityContactList(opContact);
                user = OPDataManager.getDatastoreDelegate().getUser(opContact, contacts);
            }
            message.setSenderId(user.getUserId());
            if (!TextUtils.isEmpty(message.getReplacesMessageId())) {
                OPDataManager.getDatastoreDelegate().updateMessage(message,
                                                                   this);
            } else {
                if (!mSessionListeners.isEmpty()) {
                    message.setRead(true);
                    //TODO: decide if this shoudl be done in listener
                }

                OPDataManager.getDatastoreDelegate().saveMessage(message, this);
                // TODO: Now notify observer

            }
        } else {
            Log.d("test",
                  "SessionManager onMessageReceived "
                      + message.getMessageType());
        }
        selectActiveThread(thread);
    }

    /**
     * @return ID array of the participants, excluding yourself
     */
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
        long cbcId = OPModelUtils
            .getWindowIdForThread(thread);
        switch (mType){
        case ContextBased:
            if (mConvThread != null
                && thread.getThreadID().equals(mConvThread.getThreadID())) {

                return true;
            } else if (mCbcId == cbcId) {
                mConvThread = thread;
                return true;
            } else {
                return false;
            }
        case ContactsBased:
            if (mConvThread != null
                && thread.getThreadID().equals(mConvThread.getThreadID())) {

                return true;
            } else if (mCbcId == cbcId) {
                mConvThread = thread;
                return true;
            } else {
                return false;
            }
        case RoomBased:
            return false;
        default:
            return false;
        }

    }

    public void onContactsChanged(List<OPUser> users) {
        switch (mType){
        case ContactsBased:
            long cbcId = OPModelUtils.getWindowId(users);
            OPConversationThread thread = ConversationManager.getInstance().findThreadByCbcId
                (cbcId);
            if (thread != null) {
                mConvThread = thread;
                mParticipants = users;
                mCbcId = cbcId;
            } else {
                if (mConvThread != null) {
                    if(mCbcId!=cbcId) {
                        ConversationManager.getInstance().removeThread(mCbcId);
                        List<OPUser> addedUsers = new ArrayList<>();
                        List<OPUser> removedUsers = new ArrayList<>();
                        OPModelUtils.findChangedUsers(mParticipants, users, addedUsers, removedUsers);

                        if (!addedUsers.isEmpty()) {
                            addParticipants(addedUsers);
                        }
                        if (!removedUsers.isEmpty()) {
                            removeParticipants(removedUsers);
                        }
                        mParticipants = users;
                        mCbcId = cbcId;
                    }
                } else {
                    mCbcId = cbcId;
                    mParticipants = users;
                }
            }
            break;
        }
    }

    /**
     * Find the added/deleted contacts and inform listener.
     *
     * @param conversationThread
     */
    public boolean onContactsChanged(OPConversationThread conversationThread) {

        boolean contactsChanged = false;
        List<OPContact> contacts = conversationThread.getContacts();
        List<OPUser> newContacts = new ArrayList<OPUser>();
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
                List<OPIdentityContact> iContacts = mConvThread
                    .getIdentityContactList(contact);
                OPUser user = OPDataManager.getDatastoreDelegate().getUser(
                    contact, iContacts);
                newContacts.add(user);
            }
        }
        if (!newContacts.isEmpty()) {
            mParticipants.addAll(newContacts);
            contactsChanged = true;
        }

        if (!deletedContacts.isEmpty()) {
            mParticipants.removeAll(deletedContacts);
            contactsChanged = true;
        }
        if (!contactsChanged) {
            Log.e(TAG, "onContactsChanged called when no contacts change");
            return false;
        }
        mCbcId = OPModelUtils.getWindowId(mParticipants);
        if (!newContacts.isEmpty()) {

            OPConversationEvent event = new OPConversationEvent(
                this,
                OPConversationEvent.EventTypes.ContactsAdded,
                "");
            onNewEvent(event);
        }
        if (!deletedContacts.isEmpty()) {
            OPConversationEvent event = new OPConversationEvent(
                this,
                OPConversationEvent.EventTypes.ContactsRemoved, null);
            onNewEvent(event);
        }
        notifyContactsChanged();
        return true;
    }

    void notifyContactsChanged() {
        synchronized (mSessionListeners) {
            for (SessionListener listener : mSessionListeners) {
                listener.onContactsChanged();
            }
        }
    }

    public void onContactComposingStateChanged(ComposingStates state,
                                               OPContact contact) {
        OPUser user = OPDataManager.getDatastoreDelegate().getUserByPeerUri(contact.getPeerURI());
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
     * Set the database record id of this conversation.
     *
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
            OPDataManager.getDatastoreDelegate().saveCall(call, this);
        }
        OPDataManager.getDatastoreDelegate().saveCallEvent(call.getCallID(), event);
    }


    private String getContactsIdString(List<OPUser> users) {
        StringBuilder sb = new StringBuilder();
        if (!users.isEmpty()) {
            for (OPUser user : users) {
                sb.append(user.getUserId() + ",");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        String ids = sb.toString();

        return ids;
    }

    /**
     * THis fucntion calls ConversationThread.markAllMessagesRead and update database. This
     * function should be call on chat view starts and when a new message received when the chat
     * view is open
     */
    public void markAllMessagesRead() {
        OPDataManager.getDatastoreDelegate().markMessagesRead(this);
        if (mConvThread != null) {
            mConvThread.markAllMessagesRead();
        }
    }

    /**
     * Set the user's composing status.This function should be called when user start typing,
     * pausing typing, view shows, view hides.
     *
     * @param status
     */
    public void setComposingStatus(ComposingStates status) {
        if (mConvThread != null) {
            mConvThread.setStatusInThread(status);
        }
    }

}
