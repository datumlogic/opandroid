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

import com.openpeer.javaapi.OPAccount;
import com.openpeer.javaapi.OPIdentity;
import com.openpeer.javaapi.OPIdentityContact;
import com.openpeer.javaapi.OPMessage;
import com.openpeer.javaapi.OPRolodexContact;
import com.openpeer.javaapi.OPRolodexContact.OPAvatar;
import com.openpeer.sdk.model.OPHomeUser;
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

	public boolean saveOrUpdateAccount(OPAccount account);

	public boolean saveOrUpdateIdentities(List<OPIdentity> identies, long accountId);

	public boolean saveOrUpdateContacts(List<? extends OPRolodexContact> contacts, long identityId);

	public boolean saveOrUpdateIdentity(OPIdentity identy, long accountId);

	public boolean saveOrUpdateContact(OPRolodexContact contact, long identityId);

	public boolean deleteIdentity(long id);

	public boolean deleteContact(String identityUri);

	public String getDownloadedContactsVersion(long identityId);

	public void setDownloadedContactsVersion(long identityId, String version);

	boolean flushContactsForIdentity(long id);

	public boolean saveMessage(OPMessage message, long sessionId, String threadId);

	void saveWindow(long windowId, List<OPUser> userList);

	public void saveOrUpdateUsers(List<OPIdentityContact> iContacts, long stableID);

	/**
	 * Return user with updated userId
	 * 
	 * @param user
	 * @return
	 */
	OPUser saveUser(OPUser user);

	public List<OPAvatar> getAvatars(String identityUri);

	public void markMessagesRead(long mCurrentWindowId);

	List<OPUser> getUsers(long[] userIDs);

	OPIdentityContact getIdentityContact(String identityContactId);

	boolean updateMessageDeliveryStatus(long windowId, String messageId, int deliveryStatus, long updateTime);

	public OPUser getUserByPeerUri(String uri);

	/**
	 * Query stored data and return message constructed from stored data
	 * 
	 * @param messageId
	 * @return
	 */
	OPMessage getMessage(String messageId);

	/**
	 * @param id
	 * @return
	 */
	public OPUser getUserById(long id);

	/**
	 * Perform data cleanup when signout
	 */
	public void shutdown();

	/**
	 * 
	 */
	public void notifyContactsChanged();

}
