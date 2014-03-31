package com.openpeer.javaapi;

import java.util.List;

public class OPDownloadedRolodexContacts {

	private boolean mIsSuccess;
	private boolean mFlushAllRolodexContacts;
	private String mVersionDownloaded;
	private List<OPRolodexContact> mRolodexContacts;
	
	public boolean isSuccess() {
		return mIsSuccess;
	}
	public void setIsSuccess(boolean mIsSuccess) {
		this.mIsSuccess = mIsSuccess;
	}
	public boolean isFlushAllRolodexContacts() {
		return mFlushAllRolodexContacts;
	}
	public void setFlushAllRolodexContacts(boolean mFlushAllRolodexContacts) {
		this.mFlushAllRolodexContacts = mFlushAllRolodexContacts;
	}
	public String getVersionDownloaded() {
		return mVersionDownloaded;
	}
	public void setVersionDownloaded(String mVersionDownloaded) {
		this.mVersionDownloaded = mVersionDownloaded;
	}
	public List<OPRolodexContact> getRolodexContacts() {
		return mRolodexContacts;
	}
	public void setRolodexContacts(List<OPRolodexContact> mRolodexContacts) {
		this.mRolodexContacts = mRolodexContacts;
	}
	
	
}
