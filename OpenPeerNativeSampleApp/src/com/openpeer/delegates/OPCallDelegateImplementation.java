package com.openpeer.delegates;

import android.util.Log;

import com.openpeer.javaapi.CallStates;
import com.openpeer.javaapi.OPCall;
import com.openpeer.javaapi.OPCallDelegate;

public class OPCallDelegateImplementation extends OPCallDelegate {

	@Override
	public void onCallStateChanged(OPCall call, CallStates state) {
		// TODO Auto-generated method stub
		Log.d("output", "Call State = " + state.toString());
		
		if(state.equals(CallStates.CallState_Open))
		{
			Log.d("output", "Call ID = " + call.getCallID());
			
		}

	}

}
