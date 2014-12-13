/*
 * ******************************************************************************
 *  *
 *  *  Copyright (c) 2014 , Hookflash Inc.
 *  *  All rights reserved.
 *  *
 *  *  Redistribution and use in source and binary forms, with or without
 *  *  modification, are permitted provided that the following conditions are met:
 *  *
 *  *  1. Redistributions of source code must retain the above copyright notice, this
 *  *  list of conditions and the following disclaimer.
 *  *  2. Redistributions in binary form must reproduce the above copyright notice,
 *  *  this list of conditions and the following disclaimer in the documentation
 *  *  and/or other materials provided with the distribution.
 *  *
 *  *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  *  DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 *  *  ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *  *
 *  *  The views and conclusions contained in the software and documentation are those
 *  *  of the authors and should not be interpreted as representing official policies,
 *  *  either expressed or implied, of the FreeBSD Project.
 *  ******************************************************************************
 */

package com.openpeer.sdk.model;

import com.openpeer.javaapi.ComposingStates;
import com.openpeer.javaapi.ContactConnectionStates;
import com.openpeer.javaapi.MessageDeliveryStates;
import com.openpeer.javaapi.OPContact;
import com.openpeer.javaapi.OPConversationThread;
import com.openpeer.javaapi.OPConversationThreadDelegate;
import com.openpeer.javaapi.OPLogLevel;
import com.openpeer.javaapi.OPLogger;
import com.openpeer.javaapi.OPMessage;
import com.openpeer.sdk.app.OPDataManager;
import com.openpeer.sdk.utils.OPModelUtils;

import java.util.Hashtable;
import java.util.List;

public class ThreadManager extends OPConversationThreadDelegate {
    PushServiceInterface mPushService;
    private Hashtable<Long, OPConversationThread> mCbcToThreads;
    private Hashtable<String, OPConversationThread> mThreads;

    private static ThreadManager instance;

    public static ThreadManager getInstance() {
        if (instance == null) {
            instance = new ThreadManager();
        }
        return instance;
    }

    private ThreadManager() {
    }

    public void registerPushService(PushServiceInterface pushService) {
        mPushService = pushService;
    }

    public void unregisterPushService() {
        mPushService = null;
    }

    OPConversationThread findThreadByCbcId(long id) {
        if (mCbcToThreads == null) {
            return null;
        }
        return mCbcToThreads.get(id);
    }

    void cacheCbcToThread(long cbcId, OPConversationThread thread) {
        if (mCbcToThreads == null) {
            mCbcToThreads = new Hashtable<>();
        }
        mCbcToThreads.put(cbcId, thread);
    }

    void cacheThread(OPConversationThread thread) {
        if (mThreads == null) {
            mThreads = new Hashtable<>();
        }
        mThreads.put(thread.getThreadID(), thread);
    }

    long getCbcIdOfThread(String threadId) {
        if (mThreads != null) {
            OPConversationThread thread = mThreads.get(threadId);
            if (thread != null) {
                return thread.getParticipantInfo().getCbcId();
            }
        }
        return 0l;
    }

    OPConversationThread getCachedThread(OPConversationThread thread) {
        if (mThreads != null) {
            return mThreads.get(thread.getThreadID());
        } else {
            OPLogger.error(OPLogLevel.LogLevel_Basic, "getCachedThread Weird! thread not cached");
            return thread;
        }
    }

    public OPConversationThread getThread(GroupChatMode mode, String conversationId, long cbcId) {
        OPConversationThread thread = null;
        switch (mode){
        case ContactsBased:{
            thread = findThreadByCbcId(cbcId);
        }
        case ContextBased:{
            if (mThreads != null) {
                thread = mThreads.get(conversationId);
            }
        }
        }
        return thread;
    }


    //Beginning of OPConversationThreadDelegate

    @Override
    public void onConversationThreadNew(
        OPConversationThread thread) {
        List<OPUser> participants = OPModelUtils.getParticipantsOfThread(thread);
        long cbcId = OPModelUtils.getWindowId(participants);
        thread.setParticipantInfo(new ParticipantInfo(cbcId,participants));

        OPDataManager.getDatastoreDelegate().saveParticipants(cbcId, participants);
        cacheThread(thread);
        cacheCbcToThread(cbcId, thread);
    }

    @Override
    public void onConversationThreadContactsChanged(OPConversationThread thread) {
        long oldCbcId = getCbcIdOfThread(thread.getThreadID());
        OPConversation conversation = ConversationManager.getInstance().
            getConversation(getCachedThread(thread), false);
        if (conversation != null) {
            conversation.onContactsChanged(thread);
        }
        if (oldCbcId != 0) {
            mCbcToThreads.remove(oldCbcId);
        }

        List<OPUser> participants = OPModelUtils.getParticipantsOfThread(thread);
        long newCbcId = OPModelUtils.getWindowId(participants);
        thread.setParticipantInfo(new ParticipantInfo(newCbcId,participants));

        OPDataManager.getDatastoreDelegate().saveParticipants(newCbcId, participants);
        cacheThread(thread);
        cacheCbcToThread(newCbcId, thread);
    }

    @Override
    public void onConversationThreadContactStatusChanged(
        OPConversationThread conversationThread, OPContact contact) {
        if (contact.isSelf()) {
            OPLogger.error(OPLogLevel.LogLevel_Basic,
                           "weird! onConversationThreadContactStatusChanged for self " + contact
                               .getPeerURI());
        }

        ComposingStates state = conversationThread
            .getContactComposingStatus(contact);
        if (state != null) {
            OPConversation conversation = ConversationManager.getInstance()
                .getConversation(getCachedThread(conversationThread), true);
            if (conversation != null) {
                conversation.onContactComposingStateChanged(state, contact);
            }
        }
    }

    @Override
    public void onConversationThreadMessage(
        OPConversationThread conversationThread, String messageID) {
        OPMessage message = conversationThread
            .getMessageById(messageID);

        if (message.getFrom().isSelf()) {
            OPLogger.debug(
                OPLogLevel.LogLevel_Basic,
                "Weird! received message from myself!"
                    + message.getMessageId()
                    + " messageId "
                    + messageID + " type "
                    + message.getMessageType());

            return;
        }
        OPConversation conversation = ConversationManager.getInstance().getConversation
            (getCachedThread(conversationThread), true);
        conversation.onMessageReceived(conversationThread, message);
    }

    @Override
    public void onConversationThreadMessageDeliveryStateChanged(
        OPConversationThread conversationThread, String messageID,
        MessageDeliveryStates state) {
        OPDataManager.getDatastoreDelegate().updateMessageDeliveryStatus(messageID, state);
    }

    @Override
    public void onConversationThreadPushMessage(
        OPConversationThread conversationThread,
        final String messageID,
        OPContact contact) {
        if (mPushService != null) {
            final OPMessage message = conversationThread
                .getMessageById(messageID);

            OPConversation conversation = ConversationManager.getInstance()
                .getConversation(conversationThread, true);
            OPUser user = OPDataManager.getInstance().getUserByPeerUri(contact.getPeerURI());
            mPushService.onConversationThreadPushMessage(conversation, message, user);
        }
    }

    @Override
    public void onConversationThreadContactConnectionStateChanged(
        OPConversationThread conversationThread, OPContact contact,
        ContactConnectionStates state) {
    }


    //End of OPConversationThreadDelegate
    public static void clearOnSignout() {
        if (instance != null) {
            instance.mPushService = null;
        }
    }
}
