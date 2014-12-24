package com.openpeer.sdk.model;

public class ThreadMetaData {
    Data metaData;

    public static ThreadMetaData newMetaData(String conversationType) {
        ThreadMetaData result = new ThreadMetaData();
        result.metaData = new Data();
        result.metaData.conversationType = conversationType;
        return result;
    }

    public String toString(){
        return null;
    }
    static class Data {
        String conversationType;
    }

}
