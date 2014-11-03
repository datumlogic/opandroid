package com.openpeer.javaapi;

public class OPCallMessageInfo {

	private OPContact mContact;
	private int mErrorCode;
	
	public OPContact getContact() {
		return mContact;
	}
	public void setContact(OPContact mContact) {
		this.mContact = mContact;
	}
	public int getErrorCode() {
		return mErrorCode;
	}
	public void setErrorCode(int mErrorCode) {
		this.mErrorCode = mErrorCode;
	}
}
