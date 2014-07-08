package com.openpeer.sample;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import com.openpeer.javaapi.OPCall;
import com.openpeer.sample.conversation.ConversationActivity;

public class OPNotificationBuilder {
	private static String TAG = OPNotificationBuilder.class.getSimpleName();

	public static void showNotificationForCall(OPCall call) {
		Intent launchIntent = null;
		Context context = OPApplication.getInstance();
		CharSequence appName = "";
		int appIcon = android.R.drawable.sym_def_app_icon;
		// TODO build proper strings
		String message = "call";
		try
		{

			ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
			appIcon = applicationInfo.icon;
			appName = context.getPackageManager().getApplicationLabel(applicationInfo);
			//			launchIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
		} catch (NameNotFoundException e) {
			Log.e(TAG, "Failed to get application name, icon, or launch intent"); //$NON-NLS-1$
		}
		Notification.Builder builder = new Notification.Builder(context)
				.setAutoCancel(true)
				.setContentText(message)
				.setSmallIcon(R.drawable.ic_action_call_light);
		// Create the notification
		launchIntent = new Intent(context, ConversationActivity.class);
		String peerUri = call.getPeerUser().getPeerUri();
		launchIntent.putExtra(IntentData.ARG_CONVERSATION_ACTION, IntentData.ACTION_CALL);
		launchIntent.putExtra(IntentData.ARG_PEER_URI, peerUri);
		// Set the intent to perform when tapped

		launchIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, launchIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		//		notification.setLatestEventInfo(context, appName, message, contentIntent);

		builder.setContentIntent(contentIntent);

		int notificationId = 0;
		Notification notification = builder.build();//new Notification(appIcon, message, System.currentTimeMillis());

		// Show the notification
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(notificationId, notification);
	}

	public static void showNotificationForMessage(OPCall call) {

	}
}
