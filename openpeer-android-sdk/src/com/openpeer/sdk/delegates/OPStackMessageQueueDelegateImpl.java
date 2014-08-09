package com.openpeer.sdk.delegates;

import android.util.Log;

import com.openpeer.javaapi.OPStackMessageQueueDelegate;

public class OPStackMessageQueueDelegateImpl extends OPStackMessageQueueDelegate {
	private static final String TAG = OPStackMessageQueueDelegateImpl.class.getSimpleName();

	@Override
	public void onStackMessageQueueWakeUpCustomThreadAndProcessOnCustomThread() {
		Log.d(TAG, "onStackMessageQueueWakeUpCustomThreadAndProcessOnCustomThread");
	}

}
