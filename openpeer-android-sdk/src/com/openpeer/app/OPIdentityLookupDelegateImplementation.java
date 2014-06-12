package com.openpeer.app;

import java.util.List;

import android.util.Log;

import com.openpeer.javaapi.OPIdentity;
import com.openpeer.javaapi.OPIdentityContact;
import com.openpeer.javaapi.OPIdentityLookup;
import com.openpeer.javaapi.OPIdentityLookupDelegate;
import com.openpeer.javaapi.OPIdentityLookupInfo;

public class OPIdentityLookupDelegateImplementation extends
		OPIdentityLookupDelegate {

	private OPIdentity mIdentity;

	public OPIdentityLookupDelegateImplementation(OPIdentity identity) {
		mIdentity = identity;
	}

	@Override
	public void onIdentityLookupCompleted(OPIdentityLookup lookup) {
		// TODO Auto-generated method stub
		Log.d("output", "Identity lookup completed " + lookup);
		List<OPIdentityContact> iContacts = lookup.getUpdatedIdentities();
		if (iContacts != null) {
			OPDataManager.getInstance().updateIdentityContacts(mIdentity,iContacts);
		}

	}

}
