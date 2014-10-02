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
import com.openpeer.javaapi.OPConversationThread;
import com.openpeer.javaapi.OPIdentity;
import com.openpeer.javaapi.OPIdentityContact;
import com.openpeer.javaapi.OPMessage;
import com.openpeer.javaapi.OPRolodexContact;
import com.openpeer.javaapi.OPRolodexContact.OPAvatar;
import com.openpeer.sdk.model.CallEvent;
import com.openpeer.sdk.model.MessageEvent;
import com.openpeer.sdk.model.OPConversation;
import com.openpeer.sdk.model.OPConversationEvent;
import com.openpeer.sdk.model.OPUser;

public interface OPDatastoreDelegate {
    public String getReloginInfo();

    /**
     * Save or update the account information in database
     * 
     * @param account
     * @return
     */
    public boolean saveAccount(OPAccount account);

    /**
     * Save or update the associated identities of account. Call this function after login or relogin
     * 
     * @param identies
     * @param accountId
     * @param opId
     *            TODO
     * @return
     */
    public boolean saveOrUpdateIdentities(List<OPIdentity> identies,
            long accountId, long opId);

    /**
     * Save or update the Rolodex contacts related to identity. Call this fucntion after RolodexContacts download and identity lookup
     * completion.
     * 
     * @param contacts
     * @param identityId
     * @return
     */
    public boolean saveOrUpdateContacts(
            List<? extends OPRolodexContact> contacts, long identityId);

    public boolean saveOrUpdateIdentity(OPIdentity identy, long accountId);

    public boolean saveOrUpdateContact(OPRolodexContact contact, long identityId);

    public boolean deleteIdentity(long id);

    public boolean deleteContact(String identityUri);

    /**
     * Retrieve the saved contacts version.
     * 
     * @param identityUri
     * @return
     */
    public String getDownloadedContactsVersion(String identityUri);

    /**
     * Save the downloaded contacts version
     * 
     * @param identityUri
     * @param version
     */
    public void setDownloadedContactsVersion(String identityUri, String version);

    /**
     * Flush contacts associated with identity
     * 
     * @param id
     * @return
     */
    public boolean flushContactsForIdentity(long id);

    /**
     * Save a new message received or sent
     * 
     * @param message
     * @param sessionId
     * @param threadId
     * @param conversationEventId TODO
     * @return
     */
    public Uri saveMessage(OPMessage message, long sessionId, String threadId, long conversationEventId);

    void saveParticipants(long windowId, List<OPUser> userList);

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
     * In contacts based group chat mode, mark all messages belonging to to same group as read.
     * 
     * @param mCurrentWindowId
     *            windowId calculated based particpants
     */
    public void markMessagesRead(long mCurrentWindowId);

    public List<OPUser> getUsers(long[] userIDs);

    // public OPIdentityContact getIdentityContact(String identityContactId);

    boolean updateMessageDeliveryStatus(String messageId,
            MessageDeliveryStates deliveryStatus, long updateTime);

    public OPUser getUserByPeerUri(String uri);

    /**
     * Query stored data and return message constructed from stored data
     * 
     * @param messageId
     * @return
     */
    public OPMessage getMessage(String messageId);

    /**
     * @param id
     * @return
     */
    public OPUser getUserById(long id);

    /**
     * Perform data cleanup when signout
     */
    public void onSignOut();

    /**
	 * 
	 */
    public void notifyContactsChanged();

    /**
     * @param message
     * @param windowId
     * @param threadId
     * @return
     */
    int updateMessage(OPMessage message, long windowId, String threadId);

    /**
     * @param contact
     * @param identityContacts
     * @return
     */
    OPUser getUser(OPContact contact, List<OPIdentityContact> identityContacts);

    /**
     * @param rolodexId
     * @param width
     * @param height TODO
     * @return
     */
    String getAvatar(long rolodexId, int width, int height);

    /**
     * @param identity
     * @param contacts
     * @param contactsVersion
     * @return TODO
     */
    List<OPRolodexContact> saveDownloadedRolodexContacts(OPIdentity identity,
            List<OPRolodexContact> contacts, String contactsVersion);

    /**
     * @param conversation
     */
    long saveConversation(OPConversation conversation);

    /**
     * @param conversationId
     * @param event
     */
    long saveConversationEvent(long conversationId, OPConversationEvent event);

    /**
     * @param conversationId
     * @param thread
     */
    void saveThread(long conversationId, OPConversationThread thread);

    /**
     * @param message
     * @param event
     */
    long saveMessageEvent(String message, MessageEvent event);

    /**
     * @param callId
     * @param event
     * @return
     */
    long saveCallEvent(String callId, CallEvent event);

    /**
     * @param call
     * @param cbcId TODO
     * @param contextId TODO
     * @param conversationEventId TODO
     * @return
     */
    long saveCall(OPCall call, long cbcId, String contextId, long conversationEventId);

    /**
     * @return
     */
    OPUser getLoggedinUser();

}
