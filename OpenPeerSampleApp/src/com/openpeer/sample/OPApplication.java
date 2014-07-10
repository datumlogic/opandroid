package com.openpeer.sample;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.openpeer.app.OPHelper;
import com.openpeer.javaapi.OPSettings;

public class OPApplication extends Application {
	private static OPApplication instance;
	static {
		try {
			System.loadLibrary("z_shared");
			System.loadLibrary("openpeer");

		} catch (UnsatisfiedLinkError use) {
			use.printStackTrace();
		}
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		instance = this;
		OPHelper.getInstance().init(this, null);
		OPHelper.getInstance().setChatGroupMode(OPHelper.MODE_CONTACTS_BASED);
		OPSessionManager.getInstance().init();
		new Thread() {
			public void run() {
				OPSettings.redirectLog();
			}
		}.start();
	}

	public static void notify(int notificationId, int notificationDrawableRes, String title, String contentText, Intent intent) {
		NotificationManager nMan = (NotificationManager) instance.getSystemService(Context.NOTIFICATION_SERVICE);

		Notification.Builder builder = new Notification.Builder(instance).setSmallIcon(notificationDrawableRes).setContentTitle(title)
				.setContentText(contentText).setAutoCancel(true)
				.setContentIntent(PendingIntent.getActivity(instance, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT));

		nMan.notify(notificationId, builder.build());
	}

	public static Context getInstance() {
		// TODO Auto-generated method stub
		return instance;
	}

}
