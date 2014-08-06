package com.openpeer.sdk.app;

import java.util.List;

import android.util.Log;

import com.openpeer.javaapi.OPIdentity;
import com.openpeer.javaapi.OPIdentityContact;
import com.openpeer.javaapi.OPIdentityLookup;
import com.openpeer.javaapi.OPIdentityLookupDelegate;
import com.openpeer.javaapi.OPIdentityLookupInfo;

public class OPIdentityLookupDelegateImplementation extends OPIdentityLookupDelegate {

	private static OPIdentityLookupDelegateImplementation instance;

	public static OPIdentityLookupDelegateImplementation getInstance(OPIdentity identity) {
		if (instance == null) {
			instance = new OPIdentityLookupDelegateImplementation();

		}
		instance.mIdentity = identity;
		return instance;
	}

	private OPIdentity mIdentity;

	private OPIdentityLookupDelegateImplementation() {
	}

	public OPIdentityLookupDelegateImplementation(OPIdentity identity) {
		mIdentity = identity;
	}

	@Override
	public void onIdentityLookupCompleted(OPIdentityLookup lookup) {
		// TODO Auto-generated method stub
		Log.d("OPIdentityLookupDelegateImplementation", "onIdentityLookupCompleted " + lookup);
		List<OPIdentityContact> iContacts = lookup.getUpdatedIdentities();
		if (iContacts != null) {
			OPDataManager.getInstance().updateIdentityContacts(mIdentity, iContacts);
		}

	}

}
