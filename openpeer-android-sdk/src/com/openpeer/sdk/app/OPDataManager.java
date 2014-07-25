package com.openpeer.sdk.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import android.content.Intent;
import android.util.Log;

import com.openpeer.delegates.CallbackHandler;
import com.openpeer.javaapi.IdentityStates;
import com.openpeer.javaapi.OPAccount;
import com.openpeer.javaapi.OPContact;
import com.openpeer.javaapi.OPDownloadedRolodexContacts;
import com.openpeer.javaapi.OPIdentity;
import com.openpeer.javaapi.OPIdentityContact;
import com.openpeer.javaapi.OPIdentityDelegate;
import com.openpeer.javaapi.OPIdentityLookup;
import com.openpeer.javaapi.OPIdentityLookupInfo;
import com.openpeer.javaapi.OPRolodexContact;
import com.openpeer.sdk.datastore.OPDatastoreDelegate;

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
	private List<OPIdentityContact> mSelfContacts;
	private Hashtable<Long, String> downloadedIdentityContactVersions;
	private String mReloginInfo;

	private boolean mAccountReady;

	public static OPDatastoreDelegate getDatastoreDelegate() {
		return getInstance().mDatastoreDelegate;
	}

	public List<OPIdentity> getIdentities() {
		return mIdentities;
	}

	public String getReloginInfo() {
		return mReloginInfo;
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
		Log.d("test", "LoginManager.init relogin info " + mReloginInfo);
		downloadedIdentityContactVersions = new Hashtable<Long, String>();
		// mContacts = new Hashtable<Long, List<OPRolodexContact>>();
		// if (mReloginInfo != null) {
		// // Read idenities contacts and contacts
		// mSelfContacts = mDatastoreDelegate.getSelfIdentityContacts();
		// }
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
	}

	public void saveAccount() {
		mDatastoreDelegate.saveOrUpdateAccount(mAccount);
	}

	public OPAccount getSharedAccount() {
		return mAccount;
	}

	public void setIdentities(List<OPIdentity> identities) {
		mIdentities = identities;
		mSelfContacts = new ArrayList<OPIdentityContact>();
		for (OPIdentity identity : identities) {
			mSelfContacts.add(identity.getSelfIdentityContact());
		}
		mDatastoreDelegate.saveOrUpdateIdentities(mIdentities,
				mAccount.getStableID());
	}

	public List<OPIdentityContact> getSelfContacts() {
		return mSelfContacts;
	}

	public void onDownloadedRolodexContacts(OPIdentity identity) {
		OPDownloadedRolodexContacts downloaded = identity
				.getDownloadedRolodexContacts();
		long identityId = identity.getStableID();
		String contactsVersion = downloaded.getVersionDownloaded();
		downloadedIdentityContactVersions.put(identityId, contactsVersion);
		mDatastoreDelegate.setDownloadedContactsVersion(identityId,
				contactsVersion);
		List<OPRolodexContact> contacts = downloaded.getRolodexContacts();
		// if (downloaded.isFlushAllRolodexContacts()) {
		// mDatastoreDelegate.flushContactsForIdentity(identityId);
		// mDatastoreDelegate.saveOrUpdateContacts(contacts, identityId);
		// // mContacts.put(identityId, contacts);
		// } else {
		for (OPRolodexContact contact : contacts) {
			switch (contact.getDisposition()) {
			case Disposition_Remove:
				mDatastoreDelegate.deleteContact(contact.getIdentityURI());
				break;
			case Disposition_Update:
				// break;
			default:
				mDatastoreDelegate.saveOrUpdateContact(contact, identityId);
			}
		}
		identityLookup(identity, contacts);
		notifyContactsChanged();
	}

	public void identityLookup(OPIdentity identity,
			List<OPRolodexContact> contacts) {

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
		// Each IdentityContact represents a user. Update user info
		mDatastoreDelegate
				.saveOrUpdateUsers(iContacts, mIdentity.getStableID());

		notifyContactsChanged();
	}

	private void notifyContactsChanged() {

		Intent intent = new Intent();
		intent.setAction(INTENT_CONTACTS_CHANGED);
		OPHelper.getInstance().sendBroadcast(intent);
	}

	public void refreshContacts() {
		List<OPIdentity> identities = mAccount.getAssociatedIdentities();
		for (OPIdentity identity : identities) {
			CallbackHandler.getInstance().registerIdentityDelegate(identity,
					new OPIdentityDelegate() {

						@Override
						public void onIdentityStateChanged(OPIdentity identity,
								IdentityStates state) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onIdentityPendingMessageForInnerBrowserWindowFrame(
								OPIdentity identity) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onIdentityRolodexContactsDownloaded(
								OPIdentity identity) {
							onDownloadedRolodexContacts(identity);
							CallbackHandler.getInstance()
									.unregisterIdentityDelegate(this);
						}

					});
			identity.refreshRolodexContacts();
		}
	}

	public long getUserIdForContact(OPContact contact,
			OPIdentityContact iContact) {
		// TODO implement proper userId querying and gereration
		return contact.getPeerURI().hashCode();
	}

	public boolean isAccountReady() {
		return mAccountReady;
	}

	public void setAccountReady(boolean value) {
		mAccountReady = value;
	}

}
