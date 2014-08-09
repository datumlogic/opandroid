package com.openpeer.sdk.delegates;

import android.util.Log;

import com.openpeer.javaapi.OPStackDelegate;

public class OPStackDelegateImpl extends OPStackDelegate {
	private static final String TAG = OPStackDelegateImpl.class.getSimpleName();

	@Override
	public void onStackShutdown() {
		Log.d(TAG, "onStackShutdown");
	}

}
