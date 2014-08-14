package com.openpeer.sample;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Auto-relogin upon boot complete or connection restore
 * 
 * @author brucexia
 * 
 */
public class OPService extends Service {
	public static final String ACTION_RELOGIN = "com.openpeer.sample.RELOGIN";

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		if (intent == null || intent.getAction() == null)
			return;

		if (ACTION_RELOGIN.equals(intent.getAction())) {

		}

	}
}
