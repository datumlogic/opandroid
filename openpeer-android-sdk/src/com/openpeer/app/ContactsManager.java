package com.openpeer.app;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.openpeer.datastore.OPDatastoreDelegateImplementation;
import com.openpeer.delegates.CallbackHandler;
import com.openpeer.delegates.OPIdentityLookupDelegateImplementation;
import com.openpeer.javaapi.OPDownloadedRolodexContacts;
import com.openpeer.javaapi.OPIdentity;
import com.openpeer.javaapi.OPIdentityLookup;
import com.openpeer.javaapi.OPIdentityLookupInfo;
import com.openpeer.javaapi.OPRolodexContact;

public class ContactsManager {
	private static ContactsManager instance;

	public static ContactsManager getInstance() {
		if (instance == null) {
			instance = new ContactsManager();
		}
		return instance;
	}

	public void onDownloadedRolodexContacts(OPIdentity identity) {
		OPDatastoreDelegateImplementation.getInstance().saveOrUpdateContacts(identity.getDownloadedRolodexContacts().getRolodexContacts(), identity.getStableID());
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
