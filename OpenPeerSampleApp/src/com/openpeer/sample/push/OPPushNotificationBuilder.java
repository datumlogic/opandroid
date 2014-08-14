/**
 * Copyright (c) 2013, SMB Phone Inc. / Hookflash Inc.
 * All rights reserved.
 * <p/>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * <p/>
 * 1. Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * <p/>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * <p/>
 * The views and conclusions contained in the software and documentation are those
 * of the authors and should not be interpreted as representing official policies,
 * either expressed or implied, of the FreeBSD Project.
 */

package com.openpeer.sample.push;

import android.app.Notification;
import android.util.Log;

import com.openpeer.javaapi.AccountStates;
import com.openpeer.javaapi.MessageDeliveryStates;
import com.openpeer.javaapi.OPMessage;
import com.openpeer.sample.OPNotificationBuilder;
import com.openpeer.sdk.app.OPDataManager;
import com.openpeer.sdk.model.OPUser;
import com.openpeer.sdk.utils.OPModelUtils;
import com.urbanairship.push.PushNotificationBuilder;

import java.util.Map;

public class OPPushNotificationBuilder implements PushNotificationBuilder {
    static final String TAG = OPPushNotificationBuilder.class.getSimpleName();
    public static final String KEY_MESSAGE_ID_KEY = "_uamid";

    static final String KEY_PEER_URI = "peerURI";
    static final String KEY_MESSAGE_ID = "messageId";
    static final String KEY_SEND_TIME = "date";

    @Override
    public Notification buildNotification(String alert, Map<String, String> extras) {
        Log.d("test", "build push notification for " + alert);

        String senderUri = extras.get(KEY_PEER_URI);
        String messageId = extras.get(KEY_MESSAGE_ID);
        //If message is already received, ignore notification
        if (null != OPDataManager.getDatastoreDelegate().getMessage(messageId)) {
            Log.e(TAG, "received push for message that is already received " + messageId);
            return null;
        }
        OPUser sender = OPDataManager.getDatastoreDelegate().getUserByPeerUri(senderUri);
        if (sender == null) {
            Log.e("test", "Couldn't find user for peer " + senderUri);
            return null;
        }
        OPMessage message = new OPMessage(sender.getUserId(), OPMessage.OPMessageType.TYPE_TEXT,
                alert,
                Long.parseLong(extras.get(KEY_SEND_TIME)),
                messageId,
                false,
                MessageDeliveryStates.MessageDeliveryState_Delivered.ordinal());
        return OPNotificationBuilder.buildNotificationForMessage(new long[]{sender.getUserId()}, message);
    }

    @Override
    public int getNextId(String alert, Map<String, String> extras) {
        String senderUri = extras.get(KEY_PEER_URI);
        OPUser sender = OPDataManager.getDatastoreDelegate().getUserByPeerUri(senderUri);
        if (sender != null) {
            return (int) OPModelUtils.getWindowId(new long[]{sender.getUserId()});
        }
        return 0;
    }
}
