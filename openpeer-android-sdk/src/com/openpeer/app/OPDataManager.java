package com.openpeer.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import android.util.Log;

import com.openpeer.datastore.OPDatastoreDelegate;
import com.openpeer.datastore.OPDatastoreDelegateImplementation;
import com.openpeer.delegates.CallbackHandler;
import com.openpeer.javaapi.OPAccount;
import com.openpeer.javaapi.OPDownloadedRolodexContacts;
import com.openpeer.javaapi.OPIdentity;
import com.openpeer.javaapi.OPIdentityContact;
import com.openpeer.javaapi.OPIdentityLookup;
import com.openpeer.javaapi.OPIdentityLookupInfo;
import com.openpeer.javaapi.OPRolodexContact;
import com.openpeer.openpeer_android_sdk.BuildConfig;

public class OPDataManager {
	private static OPDataManager instance;
	private OPAccount mAccount;
	private OPDatastoreDelegate mDatastoreDelegate;
	private List<OPIdentity> mIdentities;
	private Hashtable<Long, OPIdentityContact> mSelfContacts;

	public Hashtable<Long, OPIdentityContact> getSelfContacts() {
		return mSelfContacts;
	}

	public void setSelfContacts(Hashtable<Long, OPIdentityContact> selfContacts) {
		this.mSelfContacts = selfContacts;
	}

	public List<OPIdentity> getIdentities() {
		return mIdentities;
	}

	private Hashtable<Long, List<OPRolodexContact>> mContacts;
	private Hashtable<Long, String> downloadedIdentityContactVersions;
	private String mReloginInfo;

	public String getReloginInfo() {
		return null;// mReloginInfo;
	}

	public static OPDataManager getInstance() {
		if (instance == null) {
			instance = new OPDataManager();
		}
		return instance;
	}

	public void init(OPDatastoreDelegate delegate) {
		assert (delegate != null);
		mDatastoreDelegate = delegate;
		mReloginInfo = delegate.getReloginInfo();
		downloadedIdentityContactVersions = new Hashtable<Long, String>();
		mContacts = new Hashtable<Long, List<OPRolodexContact>>();
		if (mReloginInfo != null) {
			// Read idenities and contacts

		}
	}

	public List<OPRolodexContact> getRolodexContactsForIdentity(long identityId) {
		// Lazy instantiation and loading of contacts
		if (mContacts == null) {
			mContacts = new Hashtable<Long, List<OPRolodexContact>>();
		}
		if (mContacts.get(identityId) == null) {
			mContacts.put(identityId,
					mDatastoreDelegate.getContacts(identityId));
		}

		if (identityId == 0) {
			List<OPRolodexContact> contacts = new ArrayList<OPRolodexContact>();
			for (List<OPRolodexContact> lc : mContacts.values()) {
				contacts.addAll(lc);
			}
			return contacts;
		}
		return mContacts.get(identityId);
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

		mDatastoreDelegate.saveOrUpdateContacts(contacts, identityId);
	}

	public void registerDatastoreDelegate(OPDatastoreDelegate delegate) {
		mDatastoreDelegate = delegate;
	}

	public void onDownloadedRolodexContacts(OPIdentity identity) {
		OPDownloadedRolodexContacts downloaded = identity
				.getDownloadedRolodexContacts();
		// if (BuildConfig.DEBUG) {
		// Log.d("login", "OPDataManager onDownloadedRolodexContacts version" +
		// downloaded.getVersionDownloaded());
		// }
		setIdentityContacts(identity.getStableID(), downloaded);

		identityLookup(identity,
				this.getRolodexContactsForIdentity(identity.getStableID()));

		// mLoginHandler.onDownloadedRolodexContacts(identity);
	}

	public void identityLookup(OPIdentity identity,
			List<OPRolodexContact> contacts) {
		if (BuildConfig.DEBUG) {
			Log.d("login", "start identity lookup");
		}
		OPIdentityLookupDelegateImplementation mIdentityLookupDelegate = new OPIdentityLookupDelegateImplementation(
				identity);
		OPIdentityLookup mIdentityLookup = new OPIdentityLookup();
		CallbackHandler.getInstance().registerIdentityLookupDelegate(
				mIdentityLookup, mIdentityLookupDelegate);

		List<OPIdentityLookupInfo> inputLookupList = new ArrayList<OPIdentityLookupInfo>();

		for (OPRolodexContact contact : contacts) {
			Log.d("output", "contact " + contact.toString());
			OPIdentityLookupInfo ilInfo = new OPIdentityLookupInfo();
			ilInfo.initWithRolodexContact(contact);
			inputLookupList.add(ilInfo);
		}

		mIdentityLookup = OPIdentityLookup.create(OPDataManager.getInstance()
				.getSharedAccount(), mIdentityLookupDelegate, inputLookupList,
				OPSdkConfig.getInstance().getIdentityProviderDomain());// "identity-v1-rel-lespaul-i.hcs.io");
	}

	public String getContactsVersionForIdentity(long id) {
		return downloadedIdentityContactVersions.get(id);
	}

	public void updateIdentityContacts(OPIdentity mIdentity,
			List<OPIdentityContact> iContacts) {

		Log.d("TODO",
				"OPDataManager updateIdentityContacts "
						+ Arrays.deepToString(iContacts.toArray()));
		mDatastoreDelegate.saveOrUpdateContacts(iContacts,
				mIdentity.getStableID());
		// TODO: optimize this
		mContacts.put(mIdentity.getStableID(),
				mDatastoreDelegate.getContacts(mIdentity.getStableID()));
	}

}
