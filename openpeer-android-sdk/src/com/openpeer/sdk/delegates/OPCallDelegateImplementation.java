package com.openpeer.sdk.delegates;

import android.util.Log;

import com.openpeer.javaapi.CallStates;
import com.openpeer.javaapi.OPCall;
import com.openpeer.javaapi.OPCallDelegate;

public class OPCallDelegateImplementation extends OPCallDelegate {

	@Override
	public void onCallStateChanged(OPCall call, CallStates state) {
		Log.d("output", "onCallStateChanged " + state.toString() + " call "
				+ call);
	}

}
