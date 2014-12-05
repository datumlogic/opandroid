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

import android.text.TextUtils;

import com.openpeer.javaapi.OPConversationThread;
import com.openpeer.sdk.utils.OPModelUtils;

import java.util.Hashtable;
import java.util.List;

public class ConversationManager {
    private static ConversationManager instance;
    private Hashtable<Long, OPConversation> cbcIdToConversationTable;
    private Hashtable<String, OPConversation> threadToConversationTable;

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

    void cacheThreadToConversation(String threadId, OPConversation conversation) {
        if (threadToConversationTable == null) {
            threadToConversationTable = new Hashtable<>();
        }
        threadToConversationTable.put(threadId, conversation);
    }

    void onConversationThreadChange(OPConversation conversation, String oldThreadId,
                                    String newThreadId) {
        if (!TextUtils.isEmpty(oldThreadId) && threadToConversationTable != null) {
            threadToConversationTable.remove(oldThreadId);
        }
        cacheThreadToConversation(newThreadId, conversation);
    }

    void onConversationParticipantsChange(OPConversation conversation, long oldCbcId,
                                          long newCbcId) {
        if (oldCbcId != 0 && cbcIdToConversationTable != null) {
            cbcIdToConversationTable.remove(oldCbcId);
        }
        cacheCbcToConversation(newCbcId, conversation);
    }

    OPConversation getConversationOfThread(OPConversationThread thread, boolean createIfNo) {
        OPConversation conversation = null;
//        if (threadToConversationTable != null) {
//            conversation = threadToConversationTable.get(thread.getThreadID());
//        }
//        if (conversation == null && cbcIdToConversationTable != null) {
        if (cbcIdToConversationTable != null) {
            conversation = cbcIdToConversationTable.get(OPModelUtils.getWindowIdForThread(thread));
            if (conversation != null) {
                conversation.setThread(thread);
                cacheThreadToConversation(thread.getThreadID(), conversation);
            }
        }
        if (conversation == null && createIfNo) {
            conversation = new OPConversation(thread);
            conversation.save();
            cacheThreadToConversation(thread.getThreadID(), conversation);
        }
        return conversation;
    }

    public OPConversation getConversationById(long id) {
        if (cbcIdToConversationTable != null) {
            for (OPConversation conversation : cbcIdToConversationTable.values()) {
                if (id == conversation.getId()) {
                    return conversation;
                }
            }
        }
        return null;
    }

    public OPConversation getConversationByCbcId(long id) {
        if (cbcIdToConversationTable != null) {
            return cbcIdToConversationTable.get(id);
        }
        return null;
    }

    private OPConversation getConversationByContextId(List<OPUser> participants, String contextId) {
        return null;
    }

    /**
     * Look up the conversation "for" users. This call use calculated window id to find the
     * conversation.
     *
     * @param users
     * @param create A new thread will be created if true
     * @return
     */
    public OPConversation getConversationForUsers(List<OPUser> users, boolean create) {
        OPConversation conversation = null;
        long cbcId = OPModelUtils.getWindowId(users);
        if (cbcIdToConversationTable != null) {
            conversation = cbcIdToConversationTable.get(cbcId);
        }
        if (conversation == null && create) {
            conversation = new OPConversation(users);
            cacheCbcToConversation(cbcId, conversation);
            conversation.save();

        }
        return conversation;
    }

    public static void clearOnSignout() {
        if (instance != null) {
            instance.cbcIdToConversationTable = null;
            instance.threadToConversationTable = null;
        }
    }
}
