package com.openpeer.delegates;

import java.util.List;

import android.util.Log;

import com.openpeer.javaapi.OPIdentityContact;
import com.openpeer.javaapi.OPIdentityLookup;
import com.openpeer.javaapi.OPIdentityLookupDelegate;
import com.openpeer.javaapi.OPIdentityLookupInfo;

public class OPIdentityLookupDelegateImplementation extends
		OPIdentityLookupDelegate {

	@Override
	public void onIdentityLookupCompleted(OPIdentityLookup lookup) {
		// TODO Auto-generated method stub
		Log.d("output", "Identity lookup test PASSED");
		List<OPIdentityContact> updated = lookup.getUpdatedIdentities();
		List<OPIdentityLookupInfo> invalid = lookup.getInvalidIdentities();
		List<OPIdentityLookupInfo> unchanged = lookup.getUnchangedIdentities();

	}

}
