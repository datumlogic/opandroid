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
package com.openpeer.sdk.datastore;

import java.util.List;

import android.net.Uri;

import com.openpeer.javaapi.MessageDeliveryStates;
import com.openpeer.javaapi.OPAccount;
import com.openpeer.javaapi.OPCall;
import com.openpeer.javaapi.OPContact;
import com.openpeer.javaapi.OPIdentity;
import com.openpeer.javaapi.OPIdentityContact;
import com.openpeer.javaapi.OPMessage;
import com.openpeer.javaapi.OPRolodexContact;
import com.openpeer.sdk.model.CallEvent;
import com.openpeer.sdk.model.GroupChatMode;
import com.openpeer.sdk.model.MessageEvent;
import com.openpeer.sdk.model.OPConversation;
import com.openpeer.sdk.model.OPConversationEvent;
import com.openpeer.sdk.model.OPUser;
import com.openpeer.sdk.model.ParticipantInfo;

public interface OPDatastoreDelegate {

    /**
     * Retrieve the relogin info string for last logged in account
     * 
     * @return relogin string
     */
    public String getReloginInfo();

    /**
     * Retrieve the saved contacts version.
     * 
     * @param identityUri
     * @return
     */
    public String getDownloadedContactsVersion(String identityUri);

    /**
     * Retrieve the account that's currently logged in.
     * 
     * @return
     */
    OPUser getLoggedinUser();

    /**
     * Returns OpenPeerContact object array
     * 
     * @param userIDs
     *            Id array of the users requested
     * @return
     */
    public List<OPUser> getUsers(long[] userIDs);
    public List<OPUser> getUsersByCbcId(long cbcId);

    /**
     * Retrieve OPUser by the peerUri
     * 
     * @param uri
     * @return
     */
    public OPUser getUserByPeerUri(String uri);

    /**
     * REtrieve OPUser by user Id
     * 
     * @param id
     * @return
     */
    public OPUser getUserById(long id);

    /**
     * Find existing OPUser using OPontact and and list of identity contacts from conversation thread. If not existing, create and save the
     * user
     * 
     * @param contact
     * @param identityContacts
     * @return
     */
    OPUser getUser(OPContact contact, List<OPIdentityContact> identityContacts);

    /**
     * Get closest possible avatar uri as per width and height. Width should take precedence over height
     * 
     * @param rolodexId
     * @param width
     * @param height
     *            TODO
     * @return
     */
    String getAvatarUri(long rolodexId, int width, int height);

    /**
     * Query stored message
     * 
     * @param messageId
     * @return
     */
    public OPMessage getMessage(String messageId);

    /**
     * Retrieve events associatedwith a conversation
     * 
     * @param conversation
     * @return
     */
    public List<OPConversationEvent> getConversationEvents(OPConversation conversation);

    /**
     * Retrieve message change events
     * 
     * @param messageId
     * @return
     */
    public List<MessageEvent> getMessageEvents(String messageId);

    /**
     * Retrieve call state change history
     * 
     * @param messageId
     * @return
     */
    public List<CallEvent> getCallEvents(String messageId);
    public OPConversation getConversation(GroupChatMode type,ParticipantInfo participantInfo,String conversationId) ;

    /**
     * Save or update the account information in database
     * 
     * @param account
     * @return
     */
    public boolean saveAccount(OPAccount account);

    /**
     * Save a new event.
     * 
     * @param event
     */
    long saveConversationEvent(OPConversationEvent event);

    /**
     * Save download rolodex contacts and version
     * 
     * @param identity
     *            associated identity
     * @param contacts
     *            rolodex contacts downloaded from rolodex server
     * @param contactsVersion
     *            downloaded contacts version
     * @return TODO
     */
    List<OPRolodexContact> saveDownloadedRolodexContacts(OPIdentity identity,
            List<OPRolodexContact> contacts, String contactsVersion);

    public void saveParticipants(long windowId, List<OPUser> userList);

    /**
     * Save a new conversation object into database.
     * 
     * @param conversation
     */
    long saveConversation(OPConversation conversation);

    /**
     * Update an exting conversation
     * 
     * @param conversation
     * @return
     */
    long updateConversation(OPConversation conversation);

    /**
     * Save a new event for message
     * 
     * @param event
     */
    long saveMessageEvent(MessageEvent event);

    /**
     * Save a new message received or sent
     * 
     * @param message
     * @param conversation
     *            Associated conversation TODO
     * @return
     */
    public Uri saveMessage(OPMessage message, OPConversation conversation);

    /**
     * Update or replace a message. This function should be called when editing/deleting a message or received a editing/deleting message
     * 
     * @param message
     * @param conversation
     *            TODO
     * @return
     */
    int updateMessage(OPMessage message, OPConversation conversation);

    /**
     * In contacts based group chat mode, mark all messages belonging to same group as read. In Context based group chat mode, mark all
     * messages belonging to same context as read.
     * 
     * @param conversation
     *            TODO
     */
    public void markMessagesRead(OPConversation conversation);

    /**
     * Update delivery status of a message being sent.
     * 
     * @param messageId
     *            TODO
     * @param deliveryStatus
     * @return
     */
    boolean updateMessageDeliveryStatus(String messageId,
            MessageDeliveryStates deliveryStatus);

    /**
     * Save a new call event, e.g. call answered, hangup,hold,etc
     * 
     * @param callId
     *            string id of the call
     * @param event
     *            new CallEvent object
     * @return
     */
    long saveCallEvent(String callId, CallEvent event);

    /**
     * Save a new call created or received.
     * 
     * @param call
     *            OPCall object
     * @param conversation
     *            associated conversation TODO
     * @return
     */
    long saveCall(OPCall call, OPConversation conversation);

    /**
     * Delete an associated identity.This is not supported now because we don't support multiple identity yet.
     * 
     * @param identityUri
     *            TODO
     * @return
     */
    public boolean deleteIdentity(String identityUri);

    /**
     * Save or update identity contacts associated with an identity. Call this after identity lookup complete
     * 
     * @param iContacts
     * @param identityId
     *            Associated Identity id.
     */
    public void saveIdentityContact(List<OPIdentityContact> iContacts,
            long identityId);

    /**
     * Perform data cleanup when signing out
     */
    public void onSignOut();
}
