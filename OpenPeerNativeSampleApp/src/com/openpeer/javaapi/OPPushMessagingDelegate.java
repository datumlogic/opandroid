package com.openpeer.javaapi;

public abstract class OPPushMessagingDelegate {

    public abstract void onPushMessagingStateChanged(
            OPPushMessaging messaging,
            PushMessagingStates state
            );

    public abstract void onPushMessagingNewMessages(OPPushMessaging messaging);
}
