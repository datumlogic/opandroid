package com.openpeer.delegates;

import android.util.Log;

import com.openpeer.javaapi.OPIdentityLookup;
import com.openpeer.javaapi.OPIdentityLookupDelegate;

public class OPIdentityLookupDelegateImplementation extends
		OPIdentityLookupDelegate {

	@Override
	public void onIdentityLookupCompleted(OPIdentityLookup lookup) {
		// TODO Auto-generated method stub
		Log.d("output", "Identity lookup test PASSED");

	}

}
