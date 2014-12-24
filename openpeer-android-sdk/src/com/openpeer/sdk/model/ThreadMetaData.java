package com.openpeer.sdk.model;

/**
 * {"metaData" : { "conversationType" : "contact" } }
 */
public class ThreadMetaData {
    Data metaData;

    public static ThreadMetaData fromJsonBlob(String jsonBlob) {
        return GsonFactory.getGson().fromJson(jsonBlob, ThreadMetaData.class);
    }

    public static ThreadMetaData newMetaData(String conversationType) {
        ThreadMetaData result = new ThreadMetaData();
        result.metaData = new Data();
        result.metaData.conversationType = conversationType;
        return result;
    }

    public String getConversationType() {
        return metaData.conversationType;
    }

    public String toJsonBlob() {
        return GsonFactory.getGson().toJson(this);
    }

    static class Data {
        String conversationType;
    }

}
