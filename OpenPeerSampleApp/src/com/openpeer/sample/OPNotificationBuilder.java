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
import com.openpeer.sample.conversation.CallActivity;
import com.openpeer.sample.conversation.ConversationActivity;
import com.openpeer.sample.util.CallUtil;
import com.openpeer.sample.util.SettingsHelper;
import com.openpeer.sdk.app.OPDataManager;
import com.openpeer.sdk.model.OPSession;
import com.openpeer.sdk.model.OPUser;

public class OPNotificationBuilder {
	private static String TAG = OPNotificationBuilder.class.getSimpleName();

	private static final int NOTIFICATION_ID_BASE_CALL = 100000;
	private static final int NOTIFICATION_ID_BASE_MESSAGE = 200000;

	public static void showNotificationForCall(OPCall call) {
		Intent launchIntent = null;
		Context context = OPApplication.getInstance();
		// TODO build proper strings

		String message = CallUtil.getCallStateStringResId(call.getState());

		Notification.Builder builder = new Notification.Builder(context)
				.setAutoCancel(true)
				.setContentTitle(call.getPeerUser().getName())
				.setContentText(message)
				.setSmallIcon(R.drawable.ic_action_call_light);
		// Create the notification
		launchIntent = new Intent(context, CallActivity.class);
		String peerUri = call.getPeer().getPeerURI();
		launchIntent.putExtra(IntentData.ARG_CONVERSATION_ACTION, IntentData.ACTION_CALL);
		launchIntent.putExtra(IntentData.ARG_PEER_URI, peerUri);
		// Set the intent to perform when tapped

		launchIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, launchIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		builder.setContentIntent(contentIntent);

		int notificationId = (int) (call.getStableID() + NOTIFICATION_ID_BASE_CALL);
		Notification notification = builder.build();

		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(notificationId, notification);
	}

	public static void showNotificationForMessage(OPSession session, OPMessage message) {
		Context context = OPApplication.getInstance();
		Notification notification = buildNotificationForMessage(session.getParticipantIDs(), message);
		int notificationId = (int) session.getCurrentWindowId() + NOTIFICATION_ID_BASE_MESSAGE;
		// Show the notification
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(notificationId, notification);
	}

	public static Notification buildNotificationForMessage(long participantIds[], OPMessage message) {
		Context context = OPApplication.getInstance();
		Intent launchIntent = null;
		// TODO build proper strings
		String title = OPApplication.getInstance().getString(R.string.label_new_message_received, message.getFromUser().getName());

		Notification.Builder builder = new Notification.Builder(context)
				.setAutoCancel(true)
				.setContentTitle(title)
				.setContentText(message.getMessage())
				.setSmallIcon(R.drawable.ic_launcher);
		if (SettingsHelper.isSoundNotificationOnForNewMessage()) {
			builder.setSound(SettingsHelper.getNotificationSound());
		}
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

		return builder.build();
	}

	public static void cancelNotificationForChat(int windowId) {
		NotificationManager notificationManager = (NotificationManager) OPApplication.getInstance().getSystemService(
				Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(NOTIFICATION_ID_BASE_MESSAGE + windowId);
	}

	public static void cancelNotificationForCall(OPCall call) {
		NotificationManager notificationManager = (NotificationManager) OPApplication.getInstance().getSystemService(
				Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(NOTIFICATION_ID_BASE_CALL + (int) call.getStableID());
	}
}
