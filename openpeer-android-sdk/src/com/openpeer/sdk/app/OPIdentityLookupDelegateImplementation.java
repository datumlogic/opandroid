package com.openpeer.sdk.app;

import java.util.Hashtable;
import java.util.List;

import android.util.Log;

import com.openpeer.javaapi.OPIdentity;
import com.openpeer.javaapi.OPIdentityContact;
import com.openpeer.javaapi.OPIdentityLookup;
import com.openpeer.javaapi.OPIdentityLookupDelegate;

public class OPIdentityLookupDelegateImplementation extends OPIdentityLookupDelegate {

	static Hashtable<String, OPIdentityLookupDelegateImplementation> instances = new Hashtable<String, OPIdentityLookupDelegateImplementation>();

	public static OPIdentityLookupDelegateImplementation getInstance(OPIdentity identity) {
		String url = identity.getIdentityURI();
		OPIdentityLookupDelegateImplementation instance = instances.get(url);
		if (instance == null) {
			instance = new OPIdentityLookupDelegateImplementation();
			instance.mIdentity = identity;
		}
		instances.put(url, instance);
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
		String url = mIdentity.getIdentityURI();		Log.d("OPIdentityLookupDelegateImplementation", "onIdentityLookupCompleted " + lookup);

		OPDataManager.getInstance().onIdentityLookupCompleted(url,lookup);
		instances.remove(url);

	}

}
