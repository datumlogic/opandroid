package com.openpeer.database;

import java.util.List;

import com.openpeer.javaapi.DatabaseInterface;
import com.openpeer.javaapi.OPAccount;
import com.openpeer.javaapi.OPContact;
import com.openpeer.javaapi.OPIdentity;

public class DatabaseImplementation implements DatabaseInterface {

	@Override
	public DatabaseInterface getInstance() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OPAccount getAccount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<OPIdentity> getIdentities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<OPContact> getContacts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean saveOrUpdateAccount(OPAccount account) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveOrUpdateIdentities(List<OPIdentity> identies) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveOrUpdateContacst(List<OPContact> contacts) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveOrUpdateIdentity(OPIdentity identy) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveOrUpdateContact(OPContact contact) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteIdentity(String id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteContact(String id) {
		// TODO Auto-generated method stub
		return false;
	}

}
