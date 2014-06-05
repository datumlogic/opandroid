package com.openpeer.javaapi;

import java.util.List;


public class OPContactProfileInfo {

	private OPContact mContact;
	private List<OPIdentityContact> mIdentityContacts;
	
	public OPContact getContact() {
		return mContact;
	}
	public void setContact(OPContact mContact) {
		this.mContact = mContact;
	}
	public List<OPIdentityContact> getIdentityContacts() {
		return mIdentityContacts;
	}
	public void setIdentityContacts(List<OPIdentityContact> contacts) {
		this.mIdentityContacts = contacts;
	}
}
