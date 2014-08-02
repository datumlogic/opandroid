package com.openpeer.sample.push;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.openpeer.javaapi.AccountStates;
import com.openpeer.javaapi.MessageDeliveryStates;
import com.openpeer.javaapi.OPMessage;
import com.openpeer.sample.OPNotificationBuilder;
import com.openpeer.sdk.app.OPDataManager;
import com.openpeer.sdk.model.OPUser;
import com.openpeer.sdk.utils.OPModelUtils;
import com.urbanairship.actions.DeepLinkAction;
import com.urbanairship.actions.LandingPageAction;
import com.urbanairship.actions.OpenExternalUrlAction;
import com.urbanairship.push.GCMMessageHandler;
import com.urbanairship.push.PushManager;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PushIntentReceiver extends BroadcastReceiver {
    private static final String logTag = "PushIntentReceiver";
    public static final String EXTRA_MESSAGE_ID_KEY = "_uamid";

    static final String KEY_PEER_URI = "peerURI";
    static final String KEY_SENDER_NAME = "senderName";
    static final String KEY_MESSAGE_ID = "messageID";
    static final String KEY_SEND_TIME = "date";

    // A set of actions that launch activities when a push is opened.  Update
    // with any custom actions that also start activities when a push is opened.
    private static String[] ACTIVITY_ACTIONS = new String[]{
            DeepLinkAction.DEFAULT_REGISTRY_NAME,
            OpenExternalUrlAction.DEFAULT_REGISTRY_NAME,
            LandingPageAction.DEFAULT_REGISTRY_NAME
    };

    @Override

    public void onReceive(Context context, Intent intent) {
        Log.i(logTag, "Received intent: " + intent.toString());

        String action = intent.getAction();

        if (action == null) {
            return;
        }


        if (action.equals(PushManager.ACTION_PUSH_RECEIVED)) {

            int id = intent.getIntExtra(PushManager.EXTRA_NOTIFICATION_ID, 0);

            Log.i(logTag, "Received push notification. Alert: "
                    + intent.getStringExtra(PushManager.EXTRA_ALERT)
                    + " [NotificationID=" + id + "]");

            logPushExtras(intent);

            String senderUri = intent.getStringExtra(KEY_PEER_URI);
            String messageId = intent.getStringExtra(KEY_MESSAGE_ID);
            OPUser sender = OPDataManager.getDatastoreDelegate().getUserByPeerUri(senderUri);
            if (sender == null) {
                Log.e("test", "Couldn't find user for peer " + senderUri);
                return;
            }

            OPMessage opMessage = new OPMessage(sender.getUserId(),
                    OPMessage.OPMessageType.TYPE_TEXT,
                    intent.getStringExtra(PushManager.EXTRA_ALERT),
                    Long.parseLong(intent.getStringExtra(KEY_SEND_TIME)),
                    messageId,
                    false,
                    MessageDeliveryStates.MessageDeliveryState_Delivered.ordinal());
            long windowId = OPModelUtils.getWindowId(new long[]{sender.getUserId()});

            OPDataManager.getDatastoreDelegate().saveMessage(opMessage, windowId, "");

        } else if (action.equals(PushManager.ACTION_NOTIFICATION_OPENED)) {

        } else if (action.equals(PushManager.ACTION_REGISTRATION_FINISHED)) {

            Log.i(logTag, "Registration complete. APID:" + intent.getStringExtra(PushManager.EXTRA_APID)
                    + ". Valid: " + intent.getBooleanExtra(PushManager.EXTRA_REGISTRATION_VALID, false));
        } else if (action.equals(GCMMessageHandler.ACTION_GCM_DELETED_MESSAGES)) {
            Log.i(logTag, "The GCM service deleted " + intent.getStringExtra(GCMMessageHandler.EXTRA_GCM_TOTAL_DELETED) + " messages.");
        } else if (action.equals(PushManager.ACTION_REGISTRATION_FINISHED)) {
            String apid = PushManager.shared().getAPID();
            Log.i(logTag, "Push registration finished " + apid);
            if (apid != null && OPDataManager.getInstance().getSharedAccount().getState(0, null) == AccountStates.AccountState_Ready) {
                PushRegistrationManager.getInstance().associateDeviceToken(OPDataManager.getInstance().getSharedAccount().getPeerUri(), apid, new Callback<HackApiService.HackAssociateResult>() {
                    @Override
                    public void success(HackApiService.HackAssociateResult hackAssociateResult, Response response) {

                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
            }
        }

        // Notify any app-specific listeners using the local broadcast receiver to avoid
        // leaking any sensitive information.  This sends out all push and location intents
        // to the rest of the application.
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }


    /**
     * Log the values sent in the payload's "extra" dictionary.
     *
     * @param intent A PushManager.ACTION_NOTIFICATION_OPENED or ACTION_PUSH_RECEIVED intent.
     */
    private void logPushExtras(Intent intent) {
        Set<String> keys = intent.getExtras().keySet();
        for (String key : keys) {

            //ignore standard C2DM extra keys
            List<String> ignoredKeys = (List<String>) Arrays.asList(
                    "collapse_key",//c2dm collapse key
                    "from",//c2dm sender
                    PushManager.EXTRA_NOTIFICATION_ID,//int id of generated notification (ACTION_PUSH_RECEIVED only)
                    PushManager.EXTRA_PUSH_ID,//internal UA push id
                    PushManager.EXTRA_ALERT);//ignore alert
            if (ignoredKeys.contains(key)) {
                continue;
            }
            Log.i(logTag, "Push Notification Extra: [" + key + " : " + intent.getStringExtra(key) + "]");
        }
    }


    public Notification buildNotification(String alert, Map<String, String> extras) {
        Log.d("test", "build push notification for " + alert);
        String senderUri = extras.get(KEY_PEER_URI);
        String senderName = extras.get(KEY_SENDER_NAME);
        String messageId = extras.get(KEY_MESSAGE_ID);
        Log.d("test", "peerURI " + senderUri + " sender name " + senderName + " message id " + messageId);
        OPUser sender = OPDataManager.getDatastoreDelegate().getUserByPeerUri(senderUri);
        if (sender == null) {
            Log.e("test", "Couldn't find user for peer " + senderUri);
        }
        OPMessage message = new OPMessage(sender.getUserId(), OPMessage.OPMessageType.TYPE_TEXT,
                alert,
                Long.parseLong(extras.get(KEY_SEND_TIME)),
                messageId,
                false,
                MessageDeliveryStates.MessageDeliveryState_Delivered.ordinal());
        return OPNotificationBuilder.buildNotificationForMessage(new long[]{sender.getUserId()}, message);
    }

    public int getNextId(String alert, Map<String, String> extras) {
        String senderUri = extras.get(KEY_PEER_URI);

        return senderUri.hashCode();
    }
}
