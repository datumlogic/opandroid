package com.openpeer.javaapi;

public abstract class OPPushMessagingQueryDelegate {
    public abstract void onPushMessagingQueryUploaded(OPPushMessagingQuery query);
    public abstract void onPushMessagingQueryPushStatesChanged(OPPushMessagingQuery query);
}
