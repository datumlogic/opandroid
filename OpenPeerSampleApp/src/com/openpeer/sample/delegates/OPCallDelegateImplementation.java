package com.openpeer.sample.delegates;

import android.util.Log;

import com.openpeer.javaapi.CallStates;
import com.openpeer.javaapi.OPCall;
import com.openpeer.javaapi.OPCallDelegate;
import com.openpeer.sample.OPSessionManager;

public class OPCallDelegateImplementation extends OPCallDelegate {

	@Override
	public void onCallStateChanged(OPCall call, CallStates state) {
		// TODO Auto-generated method stub
		OPSessionManager.getInstance().onCallStateChanged(call, state);

	}

}
