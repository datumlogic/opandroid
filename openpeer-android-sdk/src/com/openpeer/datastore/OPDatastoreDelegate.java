package com.openpeer.datastore;

import java.util.List;

import com.openpeer.javaapi.OPAccount;
import com.openpeer.javaapi.OPIdentity;
import com.openpeer.javaapi.OPIdentityContact;
import com.openpeer.javaapi.OPMessage;
import com.openpeer.javaapi.OPRolodexContact;
import com.openpeer.model.OPHomeUser;
import com.openpeer.app.OPSession;
import com.openpeer.app.OPUser;

import android.content.Context;

public interface OPDatastoreDelegate {
	public String getReloginInfo();

	public OPHomeUser getHomeUser();

	public boolean saveHomeUser(OPHomeUser user);

	public List<OPIdentity> getIdentities();

	public OPIdentity getIdentity();

	/**
	 * Retrieve stored OpenPeer contacts for identity
	 * 
	 * @param identityId
	 *            The stableId of the identity. If null, all contacts will be
	 *            returned
	 * @return list of OpenPeer contacts for specific identity
	 */
	public List<OPRolodexContact> getOPContacts(String identityId);

	/**
	 * Retrieve stored rolodex contacts for identity
	 * 
	 * @param identityId
	 *            The stableId of the identity. If null, all contacts will be
	 *            returned
	 * @return list of Rolodex contacts for specific identity
	 */
	public List<OPRolodexContact> getContacts(long identityId);

	public boolean saveOrUpdateAccount(OPAccount account);

	public boolean saveOrUpdateIdentities(List<OPIdentity> identies,
			long accountId);

	public boolean saveOrUpdateContacts(
			List<? extends OPRolodexContact> contacts, long identityId);

	public boolean saveOrUpdateIdentity(OPIdentity identy, long accountId);

	public boolean saveOrUpdateContact(OPRolodexContact contact, long identityId);

	public boolean deleteIdentity(long id);

	public boolean deleteContact(long id);

	public String getDownloadedContactsVersion(long identityId);

	public void setDownloadedContactsVersion(long identityId, String version);

	boolean flushContactsForIdentity(long id);

	public List<OPIdentityContact> getSelfIdentityContacts();

	public OPIdentityContact getIdentityContact(String identityContactId);

	public List<OPMessage> getMessagesWithContact(long contactId, int max,
			String lastMessageId);

	public boolean saveMessage(OPMessage message, long sessionId, String threadId);

	public boolean saveSession(OPSession session);

	int getNumberofUnreadMessages(String contactId);

	List<OPMessage> getMessagesWithSession(long sessionId, int max,
			String lastMessageId);

	List<OPSession> getRecentSessions();

	void saveWindow(long windowId, List<OPUser> userList);

	public void saveOrUpdateUsers(List<OPIdentityContact> iContacts, long stableID);

	OPUser saveUser(OPUser user);

	public List<OPUser> getUsers(long[] ids);

	/*
	 * public boolean saveConversationRecord(OPConversationRecord record);
	 * public boolean saveCallRecord(OPConversationRecord record); public
	 * List<OPConversationRecord> getConversationRecords(String contactId, int
	 * pageNumber, int numberofRecords); public List<OPCallRecord>
	 * getConversationRecords(String contactId, int pageNumber, int
	 * numberofRecords);
	 */
}
