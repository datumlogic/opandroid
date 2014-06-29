package com.openpeer.javaapi;

import com.openpeer.datastore.DatabaseContracts.IdentityContactEntry;

import android.database.Cursor;
import android.text.format.Time;

public class OPIdentityContact extends OPRolodexContact {

	private String mStableID;
	private long userId;

	private OPPeerFilePublic mPeerFilePublic;
	private String mIdentityProofBundleEl;

	private int mPriority;
	private int mWeight;

	private Time mLastUpdated;
	private Time mExpires;

	public OPIdentityContact setIdentityParams(String mStableID,
			String peerFileString, String mIdentityProofBundleEl,
			int mPriority, int mWeight, long lastUpdateTime, long expireTime,
			long id) {

		this.mStableID = mStableID;
		this.mPeerFilePublic = new OPPeerFilePublic(peerFileString);
		this.mIdentityProofBundleEl = mIdentityProofBundleEl;
		this.mPriority = mPriority;
		this.mWeight = mWeight;
		this.mLastUpdated = new Time();
		mLastUpdated.set(lastUpdateTime);
		this.mExpires = new Time();
		mExpires.set(expireTime);
		userId = id;
		return this;
	}

	public OPIdentityContact() {

	}

	public OPIdentityContact(OPRolodexContact rolodexContact) {
		copy(rolodexContact);
	}

	public boolean hasData() {
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

	public String toString() {
		return super.toString() + " identityProofBundle "
				+ mIdentityProofBundleEl + " mStableID " + mStableID
				+ " mExpires " + mExpires;
	}

	/**
	 * A local ID used to identity a user, it's simply the "_ID" field in
	 * database. A user is deemed the same if any of the follow meet: -- peerURI
	 * -- stableID -- identityURI( TO BE DETERMINED)
	 * 
	 * @return
	 */
	public long getUserId() {
		// TODO Auto-generated method stub
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public static OPIdentityContact fromCursor(Cursor cursor) {
		OPIdentityContact contact = new OPIdentityContact(OPRolodexContact.contactFromCursor(cursor));
		return contact
				.setIdentityParams(
						cursor.getString(cursor
								.getColumnIndex(IdentityContactEntry.COLUMN_NAME_STABLE_ID)),
						cursor.getString(cursor
								.getColumnIndex(IdentityContactEntry.COLUMN_NAME_PEERFILE_PUBLIC)),
						cursor.getString(cursor
								.getColumnIndex(IdentityContactEntry.COLUMN_NAME_IDENTITY_PROOF_BUNDLE)),
						cursor.getInt(cursor
								.getColumnIndex(IdentityContactEntry.COLUMN_NAME_PRORITY)),
						cursor.getInt(cursor
								.getColumnIndex(IdentityContactEntry.COLUMN_NAME_WEIGHT)),
						cursor.getLong(cursor
								.getColumnIndex(IdentityContactEntry.COLUMN_NAME_LAST_UPDATE_TIME)),
						cursor.getLong(cursor
								.getColumnIndex(IdentityContactEntry.COLUMN_NAME_EXPIRE)),
						cursor.getLong(cursor
								.getColumnIndex(IdentityContactEntry.COLUMN_NAME_USER_ID)));
	}
}
