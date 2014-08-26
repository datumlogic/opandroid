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

import com.openpeer.javaapi.OPAccount;
import com.openpeer.javaapi.OPIdentity;
import com.openpeer.javaapi.OPIdentityContact;
import com.openpeer.javaapi.OPMessage;
import com.openpeer.javaapi.OPRolodexContact;
import com.openpeer.javaapi.OPRolodexContact.OPAvatar;
import com.openpeer.sdk.model.OPUser;

public interface OPDatastoreDelegate {
    public String getReloginInfo();

    /**
     * Retrieve stored OpenPeer contacts for identity
     * 
     * @param identityId
     *            The stableId of the identity. If null, all contacts will be returned
     * @return list of OpenPeer contacts for specific identity
     */
    public List<OPRolodexContact> getOPContacts(String identityId);

    /**
     * Save or update the account information in database
     * 
     * @param account
     * @return
     */
    public boolean saveOrUpdateAccount(OPAccount account);

    /**
     * Save or update the associated identities of account. Call this function after login or relogin
     * 
     * @param identies
     * @param accountId
     * @return
     */
    public boolean saveOrUpdateIdentities(List<OPIdentity> identies, long accountId);

    /**
     * Save or update the Rolodex contacts related to identity. Call this fucntion after RolodexContacts download and identity lookup
     * completion.
     * 
     * @param contacts
     * @param identityId
     * @return
     */
    public boolean saveOrUpdateContacts(List<? extends OPRolodexContact> contacts, long identityId);

    public boolean saveOrUpdateIdentity(OPIdentity identy, long accountId);

    public boolean saveOrUpdateContact(OPRolodexContact contact, long identityId);

    public boolean deleteIdentity(long id);

    public boolean deleteContact(String identityUri);

    /**
     * Retrieve the saved contacts version.
     * 
     * @param identityId
     * @return
     */
    public String getDownloadedContactsVersion(long identityId);

    /**
     * Save the downloaded contacts version
     * 
     * @param identityId
     * @param version
     */
    public void setDownloadedContactsVersion(long identityId, String version);

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
     * @return
     */
    public Uri saveMessage(OPMessage message, long sessionId, String threadId);

    void saveWindow(long windowId, List<OPUser> userList);

    /**
     * Save or update identity contacts associated with an identity. Call this after identity lookup complete
     * 
     * @param iContacts
     * @param identityId Associated Identity id.
     */
    public void saveOrUpdateUsers(List<OPIdentityContact> iContacts, long identityId);

    /**
     * Return user with updated userId
     * 
     * @param user
     * @return
     */
    public OPUser saveUser(OPUser user);

    public List<OPAvatar> getAvatars(String identityUri);

    /**
     * In contacts based group chat mode, mark all messages belonging to to same group as read.
     * 
     * @param mCurrentWindowId
     *            windowId calculated based particpants
     */
    public void markMessagesRead(long mCurrentWindowId);

    public List<OPUser> getUsers(long[] userIDs);

    public OPIdentityContact getIdentityContact(String identityContactId);

    boolean updateMessageDeliveryStatus(String messageId, int deliveryStatus, long updateTime);

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

}
