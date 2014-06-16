package com.openpeer.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import android.content.Intent;
import android.util.Log;

import com.openpeer.datastore.OPDatastoreDelegate;
import com.openpeer.delegates.CallbackHandler;
import com.openpeer.javaapi.OPAccount;
import com.openpeer.javaapi.OPDownloadedRolodexContacts;
import com.openpeer.javaapi.OPIdentity;
import com.openpeer.javaapi.OPIdentityContact;
import com.openpeer.javaapi.OPIdentityLookup;
import com.openpeer.javaapi.OPIdentityLookupInfo;
import com.openpeer.javaapi.OPRolodexContact;
import com.openpeer.openpeer_android_sdk.BuildConfig;

/**
 * Hold reference to objects that cannot be constructed from database, and
 * manages contacts data change. This class is probably unneccessary -- at the
 * least I don't want it to be a simple wrapper of
 * OPDatastoreDelegateImplementation. Might end up merging this with OPHelper,
 * but for now let's keep it so OPHelper doesn't grow weird.
 * 
 */
public class OPDataManager {
	public static String INTENT_CONTACTS_CHANGED = "com.openpeer.contacts_changed";

	private static OPDataManager instance;
	private OPDatastoreDelegate mDatastoreDelegate;

	private OPAccount mAccount;
	private List<OPIdentity> mIdentities;
	private Hashtable<Long, OPIdentityContact> mSelfContacts;
	private Hashtable<Long, String> downloadedIdentityContactVersions;
	private String mReloginInfo;

	public static OPDatastoreDelegate getDatastoreDelegate() {
		return getInstance().mDatastoreDelegate;
	}

	public List<OPIdentity> getIdentities() {
		return mIdentities;
	}

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
		// mContacts = new Hashtable<Long, List<OPRolodexContact>>();
		if (mReloginInfo != null) {
			// Read idenities contacts and contacts
			mSelfContacts = mDatastoreDelegate.getSelfIdentityContacts();
		}
	}

	public List<OPRolodexContact> getRolodexContactsForIdentity(long identityId) {
		return mDatastoreDelegate.getContacts(identityId);
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
		mSelfContacts = new Hashtable<Long, OPIdentityContact>();
		for (OPIdentity identity : identities) {
			mSelfContacts.put(identity.getStableID(),
					identity.getSelfIdentityContact());
		}
		mDatastoreDelegate.saveOrUpdateIdentities(mIdentities,
				mAccount.getStableID());
	}

	public Hashtable<Long, OPIdentityContact> getSelfContacts() {
		return mSelfContacts;
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
			mDatastoreDelegate.flushContactsForIdentity(identityId);
			mDatastoreDelegate.saveOrUpdateContacts(contacts, identityId);
			// mContacts.put(identityId, contacts);
		} else {
			for (OPRolodexContact contact : contacts) {
				switch (contact.getDisposition()) {
				case Disposition_Remove:
					mDatastoreDelegate.deleteContact(contact.getId());
					break;
				default:
					mDatastoreDelegate.saveOrUpdateContact(contact, identityId);
				}
			}
		}

		mDatastoreDelegate.saveOrUpdateContacts(contacts, identityId);
	}

	public void registerDatastoreDelegate(OPDatastoreDelegate delegate) {
		mDatastoreDelegate = delegate;
	}

	public void onDownloadedRolodexContacts(OPIdentity identity) {
		OPDownloadedRolodexContacts downloaded = identity
				.getDownloadedRolodexContacts();
		long identityId = identity.getStableID();
		setIdentityContacts(identityId, downloaded);

		identityLookup(identity, mDatastoreDelegate.getContacts(identityId));
		notifyContactsChanged();
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

		notifyContactsChanged();
	}

	private void notifyContactsChanged() {

		Intent intent = new Intent();
		intent.setAction(INTENT_CONTACTS_CHANGED);
		OPHelper.getInstance().sendBroadcast(intent);
	}

	public void refreshContacts() {
		// TODO Auto-generated method stub
		
	}

}
