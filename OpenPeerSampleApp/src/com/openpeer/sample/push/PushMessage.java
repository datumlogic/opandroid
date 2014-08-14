/*******************************************************************************
 *
 *  Copyright (c) 2014 , Hookflash Inc.
 *  All rights reserved.
 *  
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *  
 *  1. Redistributions of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 *  
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 *  ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *  
 *  The views and conclusions contained in the software and documentation are those
 *  of the authors and should not be interpreted as representing official policies,
 *  either expressed or implied, of the FreeBSD Project.
 *******************************************************************************/
package com.openpeer.sample.push;

import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.openpeer.javaapi.OPMessage;
import com.openpeer.sdk.app.OPDataManager;


public class PushMessage {
    //"{\"audience\" : {\"device_token\" : \"%@\"}, \"device_types\" : [ \"ios\" ], \"notification\" : {\"ios\" : {\"sound\":\"message-received\",\"alert\": \"%@\",\"content-available\": true,\"priority\": 10}}, \"message\" : {\"title\" : \"%@\", \"body\" : \"%@\", \"content_type\" : \"text/html\"} }"
    final static String extraFormatStr = "{\"peerURI\":\"%s\",\"messageId\":\"%s\",\"message\":\"%s\",\"location\":\"%s\",\"date\":\"%d\"}";
    Object audience;
    Notification notification;
    RichMessage message;
    String device_types[] = new String[]{"android"};

    public static PushMessage fromOPMessage(OPMessage opMessage, PushToken token) {

        PushMessage pushMessage = new PushMessage();
        Log.d("test", "pushing message " + opMessage);
        if (token.getType().equals(PushToken.TYPE_APID)) {
            AndroidNotification notification = new AndroidNotification();
            notification.alert = opMessage.getMessage();
            notification.android = new AndroidOverride();
            notification.android.extra = new AndroidExtra(OPDataManager.getInstance().getSharedAccount().getPeerUri(),
                    opMessage.getMessageId(),
                    OPDataManager.getInstance().getSharedAccount().getLocationID(),
                    opMessage.getTime().toMillis(false)/1000 + "");
            pushMessage.notification = notification;

        } else {
            IosNotification notification = new IosNotification();
            notification.ios = new IosOverride(opMessage.getMessage());
            pushMessage.notification = notification;

            RichMessage msg = new RichMessage();
            msg.title = "";
            msg.body = String.format(extraFormatStr,
                    OPDataManager.getInstance().getSharedAccount().getPeerUri(),
                    opMessage.getMessageId(),
                    opMessage.getMessage(),
                    OPDataManager.getInstance().getSharedAccount().getLocationID(),
                    opMessage.getTime().toMillis(false)/1000);

            msg.content_type = RichMessage.DEFAULT_CONTENT_TYPE;
            pushMessage.setMessage(msg);
        }
        return pushMessage.setAudience(token);

    }

    public RichMessage getMessage() {
        return message;
    }

    public PushMessage setMessage(RichMessage message) {
        this.message = message;
        return this;
    }

    public PushMessage setAudience(PushToken token) {
        if (token.getType().equals(PushToken.TYPE_APID)) {
            audience = new Audience(token.getToken());
            device_types = new String[]{"android"};
        } else {
            audience = new IosAudience(token.getToken());
            device_types = new String[]{"ios"};
        }
        return this;
    }

    public static class RichMessage {
        public static final String DEFAULT_CONTENT_TYPE = "text/html";
        String title;
        String body;
        String content_type;
    }

    static class Audience {
        String apid;

        public Audience() {
        }

        public Audience(String apid) {
            this.apid = apid;
        }
    }

    static class IosAudience {
        String device_token;

        public IosAudience(String device_token) {
            this.device_token = device_token;
        }
    }

    static class Notification {
        String alert;
    }

    static class AndroidNotification extends Notification {
        AndroidOverride android;
    }

    static class IosNotification extends Notification {
        IosOverride ios;
    }

    static class AndroidOverride {
        AndroidExtra extra;
    }

    static class IosOverride {
        String sound = "message-received";
        String alert;
        @SerializedName("content-available")
        boolean contentAvailable = true;
        int priority = 10;

        public IosOverride() {
        }

        public IosOverride(String alert) {
            this.alert = alert;
        }
    }

    static class AndroidExtra {
        String peerURI;
        //        String avatar;
        String location;
        String messageId;
        String date;

        AndroidExtra() {
        }

        AndroidExtra(String peerUri, String messageId, String location, String timeInMillis) {
            this.peerURI = peerUri;
//            this.avatar = senderAvatar;
            this.messageId = messageId;
            this.date = timeInMillis;
            this.location = location;
        }
    }
}
