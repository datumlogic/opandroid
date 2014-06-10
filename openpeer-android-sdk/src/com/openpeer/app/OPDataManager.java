package com.openpeer.app;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import android.util.Log;

import com.openpeer.datastore.OPDatastoreDelegate;
import com.openpeer.datastore.OPDatastoreDelegateImplementation;
import com.openpeer.delegates.CallbackHandler;
import com.openpeer.delegates.OPIdentityLookupDelegateImplementation;
import com.openpeer.javaapi.OPAccount;
import com.openpeer.javaapi.OPDownloadedRolodexContacts;
import com.openpeer.javaapi.OPIdentity;
import com.openpeer.javaapi.OPIdentityLookup;
import com.openpeer.javaapi.OPIdentityLookupInfo;
import com.openpeer.javaapi.OPRolodexContact;

public class OPDataManager {
	private static OPDataManager instance;
	private OPAccount mAccount;
	private OPDatastoreDelegate mDatastoreDelegate;
	private List<OPIdentity> mIdentities;
	private Hashtable<Long, List<OPRolodexContact>> mContacts;
	private Hashtable<Long, String> downloadedIdentityContactVersions;
	private String mReloginInfo;

	public static OPDataManager getInstance() {
		if (instance == null) {
			instance = new OPDataManager();
		}
		return instance;
	}

	/**
	 * This function should only be called in AccountState_Ready from
	 * OPAccountDelegate. This function update the database
	 * 
	 * @param account
	 *            the logged in account
	 */
	public void setSharedAccount(OPAccount account) {
		mAccount = account;
		mDatastoreDelegate.saveOrUpdateAccount(mAccount);
	}

	public OPAccount getSharedAccount() {
		if (mAccount == null) {
			mAccount = new OPAccount();
		}
		return mAccount;
	}

	public void setIdentities(List<OPIdentity> identities) {
		mIdentities = identities;
		mDatastoreDelegate.saveOrUpdateIdentities(mIdentities,
				mAccount.getStableID());
	}

	public void setIdentityContacts(long identityId,
			OPDownloadedRolodexContacts downloadedContacts) {
		downloadedIdentityContactVersions.put(identityId,
				downloadedContacts.getVersionDownloaded());
		List<OPRolodexContact> contacts = downloadedContacts
				.getRolodexContacts();
		if (contacts == null) {
			return;
		}
		if (downloadedContacts.isFlushAllRolodexContacts()) {
			mContacts.put(identityId, contacts);
		} else {
			List<OPRolodexContact> existingContacts = mContacts.get(identityId);
			if (existingContacts != null) {
				contacts.addAll(existingContacts);
			}
			mContacts.put(identityId, contacts);
		}
	}

	public void registerDatastoreDelegate(OPDatastoreDelegate delegate) {
		mDatastoreDelegate = delegate;
	}

	public void onDownloadedRolodexContacts(OPIdentity identity) {
		setIdentityContacts(identity.getStableID(),
				identity.getDownloadedRolodexContacts());
		OPDatastoreDelegateImplementation.getInstance().saveOrUpdateContacts(
				identity.getDownloadedRolodexContacts().getRolodexContacts(),
				identity.getStableID());
		identityLookup(identity);

		// mLoginHandler.onDownloadedRolodexContacts(identity);
	}

	public void identityLookup(OPIdentity identity) {
		OPIdentityLookupDelegateImplementation mIdentityLookupDelegate = new OPIdentityLookupDelegateImplementation();
		OPIdentityLookup mIdentityLookup = new OPIdentityLookup();
		CallbackHandler.getInstance().registerIdentityLookupDelegate(
				mIdentityLookup, mIdentityLookupDelegate);

		OPDownloadedRolodexContacts rolodexContacts = identity
				.getDownloadedRolodexContacts();

		List<OPIdentityLookupInfo> inputLookupList = new ArrayList<OPIdentityLookupInfo>();

		for (OPRolodexContact contact : rolodexContacts.getRolodexContacts()) {
			Log.d("output", "contact " + contact.toString());
			OPIdentityLookupInfo ilInfo = new OPIdentityLookupInfo();
			ilInfo.initWithRolodexContact(contact);
			inputLookupList.add(ilInfo);
		}

		mIdentityLookup = OPIdentityLookup.create(OPDataManager.getInstance()
				.getSharedAccount(), mIdentityLookupDelegate, inputLookupList,
				OPSdkConfig.getInstance().getIdentityProviderDomain());// "identity-v1-rel-lespaul-i.hcs.io");
	}

}
