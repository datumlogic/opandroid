package com.openpeer.app;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;

import com.openpeer.datastore.DatabaseContracts.ContactsViewEntry;
import com.openpeer.javaapi.OPAccount;
import com.openpeer.javaapi.OPContact;
import com.openpeer.javaapi.OPIdentityContact;

public class OPUser {
	long mUserId;// locally maintained user id
	String mPeerUri;
	String mLockboxStableId;
	String mIdentityUri;
	List<OPIdentityContact> mIdentityContacts;
	OPContact mOPContact;
	// A bit redundant informatin but to simplify querying
	String mName;
	String mAvatarUri;

	/**
	 * If the user is a contact, or a stranger in group chat
	 * 
	 * @return
	 */

	public boolean isContact() {
		if (mIdentityContacts != null) {
			for (OPIdentityContact contact : mIdentityContacts) {
				if (contact.getAssociatedIdentityId() > 0) {
					return true;
				}
			}
		}
		return false;
	}

	public OPContact getOPContact() {
		return mOPContact;
	}

	public void setOPContact(OPContact contact) {
		this.mOPContact = contact;
	}

	public List<OPIdentityContact> getIdentityContacts() {
		return mIdentityContacts;
	}

	public void setIdentityContact(List<OPIdentityContact> mIdentityContact) {
		this.mIdentityContacts = mIdentityContact;
		OPIdentityContact contact = getPreferredContact();
		mOPContact = OPContact.createFromPeerFilePublic(OPDataManager.getInstance().getSharedAccount(), contact.getPeerFilePublic()
				.getPeerFileString());
		this.mPeerUri = mOPContact.getPeerURI();
		mAvatarUri = contact.getDefaultAvatarUrl();
		this.mIdentityUri = contact.getIdentityURI();
		this.mName = contact.getName();
		this.mLockboxStableId = contact.getStableID();
		this.mUserId = contact.getUserId();
	}

	/**
	 * Used to construct a new user from incoming thread contact
	 * 
	 * @param contact
	 * @param iContacts
	 */
	public OPUser(OPContact contact, List<OPIdentityContact> iContacts) {
		this.mOPContact = contact;
		this.mIdentityContacts = iContacts;
		mPeerUri = contact.getPeerURI();
		OPIdentityContact iContact = getPreferredContact();
		if (iContact != null) {
			mIdentityUri = iContact.getIdentityURI();
			mName = iContact.getName();
			mAvatarUri = iContact.getDefaultAvatarUrl();
			mLockboxStableId = iContact.getStableID();
		}
	}

	public OPUser(long mUserId, String mPeerUri, String mLockboxStableId, String mIdentityUri, String name, String avatarUri) {
		super();
		this.mUserId = mUserId;
		this.mPeerUri = mPeerUri;
		this.mLockboxStableId = mLockboxStableId;
		this.mIdentityUri = mIdentityUri;
		mName = name;
		mAvatarUri = avatarUri;
	}

	public OPUser() {
		// TODO Auto-generated constructor stub
	}

	public static OPUser fromDetailCursor(Cursor cursor) {

		if (cursor != null & cursor.getCount() > 0) {
			OPUser user = new OPUser();
			List<OPIdentityContact> contacts = new ArrayList<OPIdentityContact>();
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				contacts.add(OPIdentityContact.fromCursor(cursor));
				cursor.moveToNext();
			}
			user.setIdentityContact(contacts);
			cursor.close();
			return user;
		}
		return null;
	}

	public long getUserId() {
		return mUserId;
	}

	public void setUserId(long mUserId) {
		this.mUserId = mUserId;
	}

	public String getPeerUri() {
		return mPeerUri;
	}

	public void setPeerUri(String mPeerUri) {
		this.mPeerUri = mPeerUri;
	}

	public String getLockboxStableId() {
		return mLockboxStableId;
	}

	public void setLockboxStableId(String mLockboxStableId) {
		this.mLockboxStableId = mLockboxStableId;
	}

	public String getIdentityUri() {
		return mIdentityUri;
	}

	public void setIdentityUri(String mIdentityUri) {
		this.mIdentityUri = mIdentityUri;
	}

	public String getName() {
		return mName;
	}

	public void setName(String mName) {
		this.mName = mName;
	}

	public String getAvatarUri() {
		return mAvatarUri;
	}

	public void setAvatarUri(String mAvatarUri) {
		this.mAvatarUri = mAvatarUri;
	}

	public OPIdentityContact getPreferredContact() {
		return mIdentityContacts.get(0);
	}

	public static OPUser fromIdentityContact(OPAccount account, OPIdentityContact iContact) {
		OPContact contact = OPContact.createFromPeerFilePublic(account, iContact.getPeerFilePublic().getPeerFileString());
		OPUser user = new OPUser(-1, contact.getPeerURI(), iContact.getStableID(), iContact.getIdentityURI(), iContact.getName(),
				iContact.getDefaultAvatarUrl());
		return user;
	}

	public boolean isSame(OPContact contact) {
		// TODO Auto-generated method stub
		return contact.getPeerURI().equals(mPeerUri);
	}

}
