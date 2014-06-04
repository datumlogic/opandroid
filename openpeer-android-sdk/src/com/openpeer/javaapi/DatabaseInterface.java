package com.openpeer.javaapi;

import java.util.List;

public interface DatabaseInterface {
	public DatabaseInterface getInstance();
	public OPAccount getAccount();
	public List<OPIdentity> getIdentities();
	public List<OPContact> getContacts();
	public boolean saveOrUpdateAccount(OPAccount account);
	public boolean saveOrUpdateIdentities(List<OPIdentity> identies);
	public boolean saveOrUpdateContacst(List<OPContact> contacts);
	public boolean saveOrUpdateIdentity(OPIdentity identy);
	public boolean saveOrUpdateContact(OPContact contact);
	public boolean deleteIdentity(String id);
	public boolean deleteContact(String id);
}
