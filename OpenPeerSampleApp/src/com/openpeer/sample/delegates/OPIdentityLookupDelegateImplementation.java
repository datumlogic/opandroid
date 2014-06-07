package com.openpeer.sample.delegates;

import java.util.List;



import android.util.Log;

import com.openpeer.javaapi.OPIdentityContact;
import com.openpeer.javaapi.OPIdentityLookup;
import com.openpeer.javaapi.OPIdentityLookupDelegate;
import com.openpeer.javaapi.OPIdentityLookupInfo;
import com.openpeer.sample.login.LoginManager;

public class OPIdentityLookupDelegateImplementation extends
		OPIdentityLookupDelegate {

	@Override
	public void onIdentityLookupCompleted(OPIdentityLookup lookup) {
		// TODO Auto-generated method stub
		Log.d("output", "Identity lookup test PASSED");
		LoginManager.onIdentityLookupCompleted(lookup);

	}

}
