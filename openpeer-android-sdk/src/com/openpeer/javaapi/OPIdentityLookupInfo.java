package com.openpeer.javaapi;

import android.text.format.Time;


public class OPIdentityLookupInfo {

	private String mIdentityURI;
    private Time mLastUpdated;    // if already have information about this identity, copy the "mLastUpdated" from the IdentityInfo structure, otherwise leave value as Time() if information about this identity is not previously known

	public native void initWithIdentityContact(OPIdentityContact inIdentityContact);
	public native void initWithRolodexContact(OPRolodexContact inRolodexContact);
	
	public String getIdentityURI() {
		return mIdentityURI;
	}
	public void setIdentityURI(String mIdentityURI) {
		this.mIdentityURI = mIdentityURI;
	}
	public Time getLastUpdated() {
		return mLastUpdated;
	}
	public void setLastUpdated(Time mLastUpdated) {
		this.mLastUpdated = mLastUpdated;
	}
}
