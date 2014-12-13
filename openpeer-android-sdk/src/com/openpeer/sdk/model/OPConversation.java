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

    private String lastReadMessageId;
    private OPMessage mLastMessage;
    private Hashtable<String, OPMessage> mMessageDeliveryQueue;
    private OPConversationEvent mLastEvent;


    private List<SessionListener> mSessionListeners = new ArrayList<SessionListener>();
    //try to keep the data fields correspond to database columns
    private long _id;// database id
    private String conversationId = "";
    private String topic;
    private long cbcId;
    private GroupChatMode type;

//    public static enum ConversationType{
//        ContactsBased,
//        ContextBased
//    }
    public OPConversation() {
    }

    /**
     * @param users
     * @param conversationId
     */
    public OPConversation(List<OPUser> users, String conversationId) {
        this(users, conversationId, OPSdkConfig.getInstance().getGroupChatMode());
    }

    /**
     * Constructor used when starting a new conversation
     *
     * @param users
     */
    public OPConversation(List<OPUser> users) {
        this(users, "", OPSdkConfig.getInstance().getGroupChatMode());

    }

    public OPConversation(GroupChatMode mode, long cbcId, String conversationId) {
        this.conversationId = conversationId;
        type = mode;
        this.cbcId = cbcId;
        mParticipants = OPDataManager.getDatastoreDelegate().getUsersByCbcId(cbcId);
        mLastEvent = new OPConversationEvent(this,
                                             OPConversationEvent.EventTypes.NewConversation,
                                             "");
    }
    public OPConversation(ParticipantInfo participantInfo, String conversationId, GroupChatMode mode) {
        this.conversationId = conversationId;
        type = mode;
        mParticipants = participantInfo.getParticipants();
        cbcId = participantInfo.getCbcId();
        mLastEvent = new OPConversationEvent(this,
                                             OPConversationEvent.EventTypes.NewConversation,
                                             "");
    }
    public OPConversation(List<OPUser> users, String conversationId, GroupChatMode mode) {
        this.conversationId = conversationId;
        type = mode;
        mParticipants = users;
        cbcId = OPModelUtils.getWindowId(mParticipants);
        mLastEvent = new OPConversationEvent(this,
                                             OPConversationEvent.EventTypes.NewConversation,
                                             "");
    }

    public long save() {
        _id = OPDataManager.getDatastoreDelegate().saveConversation(this);

        onNewEvent(mLastEvent);
        return _id;
    }

    /**
     * Constructor used for incoming conversation
     *
     * @param thread
     */
    public OPConversation(OPConversationThread thread) {
        type = OPSdkConfig.getInstance().getGroupChatMode();

        mConvThread = thread;
        conversationId = mConvThread.getConversationId();
        cbcId = thread.getParticipantInfo().getCbcId();
        mParticipants = thread.getParticipantInfo().getParticipants();
        mLastEvent = new OPConversationEvent(
            this,
            OPConversationEvent.EventTypes.NewConversation,
            "");
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

    public void setType(GroupChatMode type) {
        this.type = type;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
        sendSystemMessage(topic);
    }

    public long getId() {
        return _id;
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

    void sendSystemMessage(String message){

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

    public OPCall getCurrentCall() {
        return CallManager.getInstance().findCallByCbcId(cbcId);
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
        mConvThread = ThreadManager.getInstance().getThread(type, conversationId, cbcId);
        if (mConvThread == null && createIfNo) {
            mConvThread = OPConversationThread.create(
                OPDataManager.getInstance().getSharedAccount(),
                OPDataManager.getInstance().getSelfContacts());
            addContactsToThread(mParticipants);
        }
        return mConvThread;
    }

    private OPConversationThread selectActiveThread(OPConversationThread newThread) {
        // for now just use the new thread
        if (mConvThread == null) {
            mConvThread = newThread;
        } else {
            if (!mConvThread.getThreadID().equals(newThread.getThreadID())) {
                mConvThread = newThread;
            }
        }
        return mConvThread;
    }

    public void getSessionId() {
    }

    public long getCurrentWindowId() {
        return cbcId;
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
        if (mParticipants == null) {
            mParticipants = OPDataManager.getDatastoreDelegate().getUsersByCbcId(cbcId);
        }
        return mParticipants;
    }
    public void setParticipants(List<OPUser> participants){
        mParticipants = participants;
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
        if (mConvThread != null) {
            addContactsToThread(users);
        } else {
            mParticipants.addAll(users);
            long oldCbcId = cbcId;
            cbcId = OPModelUtils.getWindowId(mParticipants);
            ConversationManager.getInstance().onConversationParticipantsChange(this,oldCbcId, cbcId);

            OPConversationEvent event = new OPConversationEvent(
                this,
                OPConversationEvent.EventTypes.ContactsAdded,
                "");
            onNewEvent(event);
            OPDataManager.getDatastoreDelegate().updateConversation(this);
            notifyContactsChanged();
        }
    }

    public void removeParticipants(List<OPUser> users) {
        List<OPContact> contacts = new ArrayList<>();
        if (mConvThread != null) {
            for (OPUser user : users) {
                contacts.add(user.getOPContact());
            }
            mConvThread.removeContacts(contacts);
        } else {
            mParticipants.removeAll(users);
            long oldCbcId = cbcId;
            cbcId = OPModelUtils.getWindowId(mParticipants);
            ConversationManager.getInstance().onConversationParticipantsChange(this,oldCbcId, cbcId);
            OPConversationEvent event = new OPConversationEvent(
                this,
                OPConversationEvent.EventTypes.ContactsRemoved,
                null);
            onNewEvent(event);
            OPDataManager.getDatastoreDelegate().updateConversation(this);

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

    private boolean isForThread(OPConversationThread thread) {
        // TODO: create appropriate logic,e.g. based on windowId
        long cbcId = OPModelUtils
            .getWindowIdForThread(thread);
        switch (type){
        case ContextBased:
            if (mConvThread != null
                && thread.getThreadID().equals(mConvThread.getThreadID())) {

                return true;
            } else if (this.cbcId == cbcId) {
                mConvThread = thread;
                return true;
            } else {
                return false;
            }
        case ContactsBased:
            if (mConvThread != null
                && thread.getThreadID().equals(mConvThread.getThreadID())) {

                return true;
            } else if (this.cbcId == cbcId) {
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

    /**
     *  This function should be called when user is added/removed from UI.
     *
     * @param users new users list
     */
    public void onContactsChanged(List<OPUser> users) {
        switch (type){
        case ContactsBased:
            long cbcId = OPModelUtils.getWindowId(users);
            if (cbcId == this.cbcId) {
                return;
            }
            //if there's existing thread that matches the participants we'll reuse that thread.
            OPConversationThread thread = ThreadManager.getInstance().findThreadByCbcId(cbcId);
            if (thread != null) {
                //TODO: Merge conversation of that thread
                String oldThreadId = mConvThread == null ? null : mConvThread.getThreadID();
                ConversationManager.getInstance().onConversationThreadChange(this, oldThreadId,
                                                                             thread.getThreadID());
                ConversationManager.getInstance().onConversationParticipantsChange(this, this.cbcId,
                                                                                   cbcId);
                mConvThread = thread;
                mParticipants = users;
                this.cbcId = cbcId;
                //make sure UI call back gets fired
                notifyContactsChanged();
            } else {
                if (mConvThread != null) {
                    if (this.cbcId != cbcId) {
                        List<OPUser> addedUsers = new ArrayList<>();
                        List<OPUser> removedUsers = new ArrayList<>();
                        OPModelUtils.findChangedUsers(mParticipants, users, addedUsers,
                                                      removedUsers);

                        if (!addedUsers.isEmpty()) {
                            addParticipants(addedUsers);
                        }
                        if (!removedUsers.isEmpty()) {
                            removeParticipants(removedUsers);
                        }
//                        mParticipants = users;
//                        cbcId = cbcId;
                    }
                } else {
                    this.cbcId = cbcId;
                    mParticipants = users;
                    notifyContactsChanged();
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

        long oldCbcId = cbcId;
        cbcId = OPModelUtils.getWindowId(mParticipants);
        ConversationManager.getInstance().onConversationParticipantsChange(this, oldCbcId, cbcId);
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
        OPDataManager.getDatastoreDelegate().updateConversation(this);
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
        return type;
    }

    /**
     * Set the database record id of this conversation.
     *
     * @param id
     */
    public void setId(long id) {
        _id = id;
    }

    /**
     * This is the conversationId used to identify a unique conversation
     *
     * @return
     */
    public String getConversationId() {
        if(conversationId==null && mConvThread!=null){
            conversationId = mConvThread.getConversationId();
        }
        return conversationId;
    }

    public void setConversationId(String id) {
        this.conversationId = id;
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

    public void onMessagePushed(String messageId,OPUser user){

    }
    public void onMessagePushFailure(String messageId,OPUser user){

    }
}
