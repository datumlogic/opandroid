package com.openpeer.sdk.delegates;

import java.util.Hashtable;
import java.util.List;

import android.util.Log;

import com.openpeer.javaapi.OPIdentity;
import com.openpeer.javaapi.OPIdentityContact;
import com.openpeer.javaapi.OPIdentityLookup;
import com.openpeer.javaapi.OPIdentityLookupDelegate;
import com.openpeer.sdk.app.OPDataManager;

/**
 * Default implementatiion of OPIdentityLookupDelegate. Handles identity lookup result and save it to datastore.
 */
public class OPIdentityLookupDelegateImpl extends OPIdentityLookupDelegate {
	private final static String TAG = OPIdentityLookupDelegateImpl.class.getSimpleName();

	private static Hashtable<String, OPIdentityLookupDelegateImpl> instances = new Hashtable<String, OPIdentityLookupDelegateImpl>();

	public static OPIdentityLookupDelegateImpl getInstance(OPIdentity identity) {
		String url = identity.getIdentityURI();
		OPIdentityLookupDelegateImpl instance = instances.get(url);
		if (instance == null) {
			instance = new OPIdentityLookupDelegateImpl();
			instance.mIdentity = identity;
		}
		instances.put(url, instance);
		return instance;
	}

	private OPIdentity mIdentity;

	private OPIdentityLookupDelegateImpl() {
	}

	public OPIdentityLookupDelegateImpl(OPIdentity identity) {
		mIdentity = identity;
	}

	@Override
	public void onIdentityLookupCompleted(OPIdentityLookup lookup) {
		String url = mIdentity.getIdentityURI();
		Log.d(TAG, "onIdentityLookupCompleted " + lookup);

		OPDataManager.getInstance().onIdentityLookupCompleted(url, lookup);
		instances.remove(url);

	}

}
