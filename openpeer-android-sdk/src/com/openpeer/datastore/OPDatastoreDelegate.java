package com.openpeer.datastore;

import java.util.List;

import com.openpeer.javaapi.OPAccount;
import com.openpeer.javaapi.OPContact;
import com.openpeer.javaapi.OPIdentity;
import com.openpeer.javaapi.OPRolodexContact;
import com.openpeer.model.OPHomeUser;

import android.content.Context;

public interface OPDatastoreDelegate {
	public OPAccount getAccount();
	public OPHomeUser getHomeUser();
	public boolean saveHomeUser(OPHomeUser user);

	public List<OPIdentity> getIdentities();

	/**
	 * Retrieve stored OpenPeer contacts for identity
	 * 
	 * @param identityId
	 *            The stableId of the identity. If null, all contacts will be
	 *            returned
	 * @return list of OpenPeer contacts for specific identity
	 */
	public List<OPContact> getOPContacts(String identityId);

	/**
	 * Retrieve stored rolodex contacts for identity
	 * 
	 * @param identityId
	 *            The stableId of the identity. If null, all contacts will be
	 *            returned
	 * @return list of Rolodex contacts for specific identity
	 */
	public List<OPRolodexContact> getRolodexContacts(String identityId);

	public boolean saveOrUpdateAccount(OPAccount account);

	public boolean saveOrUpdateIdentities(List<OPIdentity> identies);

	public boolean saveOrUpdateContacst(List<OPContact> contacts);

	public boolean saveOrUpdateIdentity(OPIdentity identy);

	public boolean saveOrUpdateContact(OPContact contact);

	public boolean deleteIdentity(String id);

	public boolean deleteContact(String id);
	/*
	 * public boolean saveConversationRecord(OPConversationRecord record);
	 * public boolean saveCallRecord(OPConversationRecord record);
	 * public List<OPConversationRecord> getConversationRecords(String contactId, int pageNumber, int numberofRecords);
	 * public List<OPCallRecord> getConversationRecords(String contactId, int pageNumber, int numberofRecords);
	 */
}
