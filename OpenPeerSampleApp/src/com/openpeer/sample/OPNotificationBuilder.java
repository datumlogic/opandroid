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
import com.openpeer.javaapi.OPMessage;
import com.openpeer.sample.conversation.ConversationActivity;
import com.openpeer.sdk.app.OPSession;

public class OPNotificationBuilder {
	private static String TAG = OPNotificationBuilder.class.getSimpleName();

	private static final int NOTIFICATION_ID_BASE = 1000;

	public static void showNotificationForCall(OPCall call) {
		Intent launchIntent = null;
		Context context = OPApplication.getInstance();
		CharSequence appName = "";
		int appIcon = android.R.drawable.sym_def_app_icon;
		// TODO build proper strings
		String message = "call";

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

		// notification.setLatestEventInfo(context, appName, message, contentIntent);

		builder.setContentIntent(contentIntent);

		int notificationId = (int) (2 + NOTIFICATION_ID_BASE);
		Notification notification = builder.build();// new Notification(appIcon, message, System.currentTimeMillis());

		// Show the notification
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(notificationId, notification);
	}

	public static void showNotificationForMessage(OPSession session, OPMessage message) {
		long participantIds[] = session.getParticipantIDs();
		Intent launchIntent = null;
		Context context = OPApplication.getInstance();
		// TODO build proper strings
		String title = OPApplication.getInstance().getString(R.string.label_new_message_received);

		Notification.Builder builder = new Notification.Builder(context)
				.setAutoCancel(true)
				.setContentTitle(title)
				.setContentText(message.getMessage())
				.setSmallIcon(R.drawable.ic_launcher);
		// Create the notification
		launchIntent = new Intent(context, ConversationActivity.class);
		launchIntent.putExtra(IntentData.ARG_CONVERSATION_ACTION, IntentData.ACTION_CHAT);
		launchIntent.putExtra(IntentData.ARG_PEER_USER_IDS, participantIds);
		// Set the intent to perform when tapped
		launchIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, launchIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		// notification.setLatestEventInfo(context, appName, message, contentIntent);

		builder.setContentIntent(contentIntent);

		// int notificationId = (int) (session.getCurrentWindowId() + NOTIFICATION_ID_BASE);
		int notificationId = (int) (1 + NOTIFICATION_ID_BASE);

		Notification notification = builder.build();
		// Show the notification
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(notificationId, notification);
	}

	public static void cancelNotificationForChat(int windowId) {
		NotificationManager notificationManager = (NotificationManager) OPApplication.getInstance().getSystemService(
				Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(NOTIFICATION_ID_BASE + 1);
	}

	public static void cancelNotificationForCall(String callId) {

	}

	static int getNotificationId(int windowId) {
		return 1 + NOTIFICATION_ID_BASE;
	}
}
