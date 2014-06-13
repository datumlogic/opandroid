package com.openpeer.datastore;

import java.util.Hashtable;
import java.util.List;

import com.openpeer.javaapi.OPAccount;
import com.openpeer.javaapi.OPIdentity;
import com.openpeer.javaapi.OPIdentityContact;
import com.openpeer.javaapi.OPRolodexContact;
import com.openpeer.model.OPHomeUser;

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

	Hashtable<Long, OPIdentityContact> getSelfIdentityContacts();

	/*
	 * public boolean saveConversationRecord(OPConversationRecord record);
	 * public boolean saveCallRecord(OPConversationRecord record); public
	 * List<OPConversationRecord> getConversationRecords(String contactId, int
	 * pageNumber, int numberofRecords); public List<OPCallRecord>
	 * getConversationRecords(String contactId, int pageNumber, int
	 * numberofRecords);
	 */
}
