package com.openpeer.sample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

/**
 * If user has logged in,start a background service to auto re-login user when booting or when connecton restored. If reloggin fails, fire
 * an error notification
 * 
 * @author brucexia
 * 
 */
public class OPReceiver extends BroadcastReceiver {
	private static String TAG = OPReceiver.class.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "onReceive");
		String actionName = intent.getAction();
		if (actionName.equals(Intent.ACTION_BOOT_COMPLETED)) {

		} else if (actionName.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {

		}
	}
}
