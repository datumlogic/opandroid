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

import com.openpeer.javaapi.OPConversationThread;
import com.openpeer.sdk.app.OPDataManager;
import com.openpeer.sdk.utils.OPModelUtils;

import java.util.Hashtable;
import java.util.List;
import java.util.UUID;

public class ConversationManager {
    private static ConversationManager instance;
    private Hashtable<Long, OPConversation> cbcIdToConversationTable;
    private Hashtable<String, OPConversation> conversationTable;//conversationId to conversation

    public static ConversationManager getInstance() {
        if (instance == null) {
            instance = new ConversationManager();
        }
        return instance;
    }

    private ConversationManager() {
    }

    void cacheCbcToConversation(long cbcId, OPConversation conversation) {
        if (cbcIdToConversationTable == null) {
            cbcIdToConversationTable = new Hashtable<>();
        }
        cbcIdToConversationTable.put(cbcId, conversation);
    }

    void cacheConversation(OPConversation conversation) {
        if (conversationTable == null) {
            conversationTable = new Hashtable<>();
        }
        conversationTable.put(conversation.getConversationId(), conversation);
    }

    void onConversationThreadChange(OPConversation conversation, String oldThreadId,
                                    String newThreadId) {
    }

    void onConversationParticipantsChange(OPConversation conversation, long oldCbcId,
                                          long newCbcId) {
        if (oldCbcId != 0 && cbcIdToConversationTable != null) {
            cbcIdToConversationTable.remove(oldCbcId);
        }
        cacheCbcToConversation(newCbcId, conversation);
    }

    OPConversation getConversation(OPConversationThread thread, boolean createNew) {
        OPConversation conversation = getConversation(thread.getConverationType(),
                                                      thread.getParticipantInfo(),
                                                      thread.getConversationId(), false);
        if (conversation == null && createNew) {
            conversation = new OPConversation(thread);
            cacheCbcToConversation(thread.getParticipantInfo().getCbcId(), conversation);
            cacheConversation(conversation);
            conversation.save();
        }
        return conversation;
    }

    public OPConversation getConversation(GroupChatMode type, ParticipantInfo participantInfo,
                                          String conversationId,
                                          boolean createNew) {
        OPConversation conversation = null;
        switch (type){
        case ContactsBased:{
            long cbcId = participantInfo.getCbcId();
            conversation = getConversationByCbcId(cbcId);
            if (conversation == null) {
                conversation = OPDataManager.getDatastoreDelegate().getConversation
                    (type,participantInfo,conversationId);
                if (conversation != null) {
                    conversation.setParticipants(participantInfo.getParticipants());
                    conversation.getThread(true);
                }
            }
        }
        break;
        case ContextBased:{
            if (conversationId != null) {
                conversation = getConversationById(conversationId);

                if (conversation == null) {
                    conversation = OPDataManager.getDatastoreDelegate().getConversation
                        (type,participantInfo,conversationId);
                    if (conversation != null) {
                        if (conversation.getCurrentWindowId() != participantInfo.getCbcId()) {
                            conversation.setCbcId(participantInfo.getCbcId());
                            conversation.setParticipants(participantInfo.getParticipants());
                            conversation.getThread(true);
                        }
                    }
                }
            }
        }
        break;
        default:
            return null;
        }
        if (conversation == null && createNew) {
            if (conversationId == null) {
                conversationId = UUID.randomUUID().toString();
            }
            conversation = new OPConversation(participantInfo, conversationId, type);
            cacheCbcToConversation(participantInfo.getCbcId(), conversation);
            cacheConversation(conversation);
            conversation.save();
        }
        return conversation;
    }

    public OPConversation getConversationById(String id) {
        if (conversationTable == null) {
            return null;
        }
        return conversationTable.get(id);
    }

    public OPConversation getConversationByCbcId(long id) {
        if (cbcIdToConversationTable != null) {
            return cbcIdToConversationTable.get(id);
        }
        return null;
    }

    public static void clearOnSignout() {
        if (instance != null) {
            instance.cbcIdToConversationTable = null;
            instance.conversationTable = null;
        }
    }
}
