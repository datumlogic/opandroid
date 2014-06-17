package com.openpeer.javaapi;

import android.text.format.Time;

public class OPIdentityContact extends OPRolodexContact {

	private String mStableID;

    private OPPeerFilePublic mPeerFilePublic;
    private String mIdentityProofBundleEl;

    private int mPriority;
    private int mWeight;

    private Time mLastUpdated;
    private Time mExpires;
    
    public OPIdentityContact()
    {
    	
    }

    public OPIdentityContact( OPRolodexContact rolodexContact)
    {
    	
    }
    
    public boolean hasData()
    {
    	return false;
    }

	public String getStableID() {
		return mStableID;
	}

	public void setStableID(String mStableID) {
		this.mStableID = mStableID;
	}

	public OPPeerFilePublic getPeerFilePublic() {
		return mPeerFilePublic;
	}

	public void setPeerFilePublic(OPPeerFilePublic mPeerFilePublic) {
		this.mPeerFilePublic = mPeerFilePublic;
	}

	public String getIdentityProofBundle() {
		return mIdentityProofBundleEl;
	}

	public void setIdentityProofBundle(String mIdentityProofBundleEl) {
		this.mIdentityProofBundleEl = mIdentityProofBundleEl;
	}

	public int getPriority() {
		return mPriority;
	}

	public void setPriority(int mPriority) {
		this.mPriority = mPriority;
	}

	public int getWeight() {
		return mWeight;
	}

	public void setWeight(int mWeight) {
		this.mWeight = mWeight;
	}

	public Time getLastUpdated() {
		return mLastUpdated;
	}

	public void setLastUpdated(Time mLastUpdated) {
		this.mLastUpdated = mLastUpdated;
	}

	public Time getExpires() {
		return mExpires;
	}

	public void setExpires(Time mExpires) {
		this.mExpires = mExpires;
	}
}
