package com.openpeer.sdk.app;

import java.util.Hashtable;
import java.util.List;


import com.openpeer.javaapi.CallStates;
import com.openpeer.javaapi.OPCall;
import com.openpeer.javaapi.OPCallDelegate;

public class OPCallManager {
	OPCall mActiveCall;
	Hashtable<Long, OPCall> mStandbyCalls;

	private static OPCallManager instance;

	private OPCallDelegate mBackgroundCallHandler;


	public void onEnteringBackground() {
	}

	public void onEnteringForeground() {
	}

	public static OPCallManager getInstance() {
		if (instance == null) {
			instance = new OPCallManager();
		}
		return instance;
	}

	public void onCallStateChanged(OPCall call, CallStates state) {
		if (OPHelper.getInstance().isAppInBackground()) {
			mBackgroundCallHandler.onCallStateChanged(call, state);
		}
	}

	/**
	 * Application should provide a background call handler to show an notification for incoming call, and all other fany stuff
	 * 
	 * @param delegate
	 */
	public void setBackgroundCallDelegate(OPCallDelegate delegate) {
		mBackgroundCallHandler = delegate;
	}

}
