/*******************************************************************************
 *
 *  Copyright (c) 2014 , Hookflash Inc.
 *  All rights reserved.
 *  
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *  
 *  1. Redistributions of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 *  
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 *  ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *  
 *  The views and conclusions contained in the software and documentation are those
 *  of the authors and should not be interpreted as representing official policies,
 *  either expressed or implied, of the FreeBSD Project.
 *******************************************************************************/
package com.openpeer.javaapi;

import java.util.Arrays;
import java.util.List;

import android.database.Cursor;
import android.util.Log;

import com.openpeer.sdk.app.OPDataManager;
import com.openpeer.sdk.datastore.DatabaseContracts;
import com.openpeer.sdk.datastore.DatabaseContracts.ContactEntry;
import com.openpeer.sdk.datastore.DatabaseContracts.ContactsViewEntry;
import com.openpeer.sdk.datastore.DatabaseContracts.IdentityContactEntry;

public class OPRolodexContact {

	public static class OPAvatar {
		private String mName;
		private String mURL;
		private int mWidth;
		private int mHeight;

		public OPAvatar(String mName, String mURL, int mWidth, int mHeight) {
			super();
			this.mName = mName;
			this.mURL = mURL;
			this.mWidth = mWidth;
			this.mHeight = mHeight;
		}

		OPAvatar() {

		}

		public String getName() {
			return mName;
		}

		public void setName(String mName) {
			this.mName = mName;
		}

		public String getURL() {
			return mURL;
		}

		public void setURL(String mURL) {
			this.mURL = mURL;
		}

		public int getWidth() {
			return mWidth;
		}

		public void setWidth(int mWidth) {
			this.mWidth = mWidth;
		}

		public int getHeight() {
			return mHeight;
		}

		public void setHeight(int mHeight) {
			this.mHeight = mHeight;
		}

		public String toString() {
			return super.toString() + " Name " + mName + " URL " + mURL
					+ " Width " + mWidth + " Height " + mHeight;
		}

	};

	public enum Dispositions {
		Disposition_NA, Disposition_Update, Disposition_Remove;

		Dispositions() {
		}

	};

	private Dispositions mDisposition;
	private String mIdentityURI;
	private String mIdentityProvider;

	private String mName;
	private String mProfileURL;
	private String mVProfileURL;

	private List<OPAvatar> mAvatars;
	private long mAssociatedIdentityId;

	public long getAssociatedIdentityId() {
		return mAssociatedIdentityId;
	}

	public void setmAssociatedIdentityId(long mAssociatedIdentityId) {
		this.mAssociatedIdentityId = mAssociatedIdentityId;
	}

	public OPRolodexContact(String mIdentityURI, String mIdentityProvider,
			String mName, String mProfileURL, String mVProfileURL,
			List<OPAvatar> mAvatars, long associatedIdentityId) {
		super();
		this.mIdentityURI = mIdentityURI;
		this.mIdentityProvider = mIdentityProvider;
		this.mName = mName;
		this.mProfileURL = mProfileURL;
		this.mVProfileURL = mVProfileURL;
		this.mAvatars = mAvatars;
		this.mAssociatedIdentityId = associatedIdentityId;
	}

	public long getId() {
		// for now just use the profileURL, may need better algorithm
		return mIdentityURI.hashCode();
	}

	public OPRolodexContact(String name, String profileURL) {
		super();
		this.mName = name;
		this.mProfileURL = profileURL;
	}

	public OPRolodexContact() {

	}

	public Dispositions getDisposition() {
		return mDisposition;
	}

	public void setDisposition(Dispositions mDisposition) {
		this.mDisposition = mDisposition;
	}

	public String getIdentityURI() {
		return mIdentityURI;
	}

	public void setIdentityURI(String mIdentityURI) {
		this.mIdentityURI = mIdentityURI;
	}

	public String getIdentityProvider() {
		return mIdentityProvider;
	}

	public void setIdentityProvider(String mIdentityProvider) {
		this.mIdentityProvider = mIdentityProvider;
	}

	public String getName() {
		return mName;
	}

	public void setName(String mName) {
		this.mName = mName;
	}

	public String getProfileURL() {
		return mProfileURL;
	}

	public void setProfileURL(String mProfileURL) {
		this.mProfileURL = mProfileURL;
	}

	public String getVProfileURL() {
		return mVProfileURL;
	}

	public void setVProfileURL(String mVProfileURL) {
		this.mVProfileURL = mVProfileURL;
	}

	public List<OPAvatar> getAvatars() {
		return mAvatars;
	}

	public void setAvatars(List<OPAvatar> mAvatars) {
		this.mAvatars = mAvatars;
	}

	public String getDefaultAvatarUrl() {
		if (mAvatars != null && mAvatars.size() != 0) {
			return mAvatars.get(0).mURL;
		} else {
			return null;
		}
	}

	OPRolodexContact copy(OPRolodexContact contact) {
		this.mAvatars = contact.mAvatars;
		this.mDisposition = contact.mDisposition;
		this.mIdentityProvider = contact.mIdentityProvider;
		this.mIdentityURI = contact.mIdentityURI;
		this.mName = contact.mName;
		this.mProfileURL = contact.mProfileURL;
		this.mVProfileURL = contact.mVProfileURL;
		this.mAssociatedIdentityId = contact.mAssociatedIdentityId;
		return this;
	}

	public String toString() {
		return super.toString()
				+ " ProfileURL "
				+ mProfileURL
				+ " Name "
				+ mName
				+ " identityUrl "
				+ mIdentityURI
				+ " IdentityProvider "
				+ mIdentityProvider
				+ " Disposition "
				+ mDisposition
				+ " avatars "
				+ (mAvatars != null ? Arrays.deepToString(mAvatars.toArray())
						: "null");
	}

	/**
	 * Do NOT use this method if you wish to implement your own data store. This function is bound the default datastore implementation
	 * 
	 * @param cursor
	 * @return
	 */
	public static OPRolodexContact contactFromCursor(Cursor cursor) {
		int identityUrlIndex = cursor
				.getColumnIndex(DatabaseContracts.COLUMN_NAME_IDENTITY_URI);
		int identityProviderIndex = cursor
				.getColumnIndex(ContactEntry.COLUMN_NAME_IDENTITY_PROVIDER);
		int nameIndex = cursor
				.getColumnIndex(ContactEntry.COLUMN_NAME_CONTACT_NAME);
		int profileURLIndex = cursor
				.getColumnIndex(ContactEntry.COLUMN_NAME_URL);
		int vprofileURLIndex = cursor
				.getColumnIndex(ContactEntry.COLUMN_NAME_VPROFILE_URL);
		int assoiatedIdentityIdIndex = cursor
				.getColumnIndex(ContactEntry.COLUMN_NAME_ASSOCIATED_IDENTITY_ID);

		OPRolodexContact contact = new OPRolodexContact(
				cursor.getString(identityUrlIndex),
				cursor.getString(identityProviderIndex),
				cursor.getString(nameIndex), cursor.getString(profileURLIndex),
				cursor.getString(vprofileURLIndex), null,
				cursor.getLong(assoiatedIdentityIdIndex));
		List<OPAvatar> avatars = OPDataManager.getDatastoreDelegate().getAvatars(contact.getIdentityURI());

		contact.setAvatars(avatars);

		return contact;
	}
}
