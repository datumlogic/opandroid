package com.openpeer.sdk.model;

import com.google.gson.reflect.TypeToken;

/**
 * {"system":{"callStatus":{"$id":"adf","status":"placed","mediaType":"audio",
 * "callee":"peer://opp.me/kadjfadkfj","error":{"$id":404}}}
 */
public class SystemMessage<T> {

    T system;

    public SystemMessage() {
    }

    public SystemMessage(T t) {
        system = t;
    }

    public static SystemMessage<CallSystemMessage> parseCallSystemMessage(String jsonBlob) {
        return GsonFactory.getGson().fromJson(jsonBlob,
                                              new TypeToken<SystemMessage<CallSystemMessage>>() {
                                              }.getType());
    }

    public String toString(){
        return GsonFactory.getGson().toJson(this);
    }
}
