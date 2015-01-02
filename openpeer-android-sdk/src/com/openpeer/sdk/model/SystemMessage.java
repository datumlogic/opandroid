package com.openpeer.sdk.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * {"system":{"callStatus":{"$id":"adf","status":"placed","mediaType":"audio",
 * "callee":"peer://opp.me/kadjfadkfj","error":{"$id":404}}}
 */
public class SystemMessage<T> {

    T system;

    public T getSystemObject(){
        return system;
    }

    public SystemMessage() {
    }

    public SystemMessage(T t) {
        system = t;
    }

    public static SystemMessage parseSystemMessage(String jsonBlob) {
        return getGson().fromJson(jsonBlob,
                                  SystemMessage.class);
    }

    public String toString() {
        return GsonFactory.getGson().toJson(this);
    }

    static class SystemMessageDeserializer implements JsonDeserializer<SystemMessage> {
        @Override
        public SystemMessage deserialize(JsonElement json, Type typeOfT,
                                         JsonDeserializationContext context) throws
            JsonParseException {
            SystemMessage message = new SystemMessage();
            JsonObject object = json.getAsJsonObject().getAsJsonObject("system");
            System.out.println("object " + object.toString());
            if (object.has("callStatus")) {

                CallSystemMessage callSystemMessage = context.deserialize(object, CallSystemMessage.class);
                message.system = callSystemMessage;
            }
            return message;
        }
    }

    private static Gson gson;

    public static Gson getGson() {
        if (gson == null) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(SystemMessage.class, new SystemMessageDeserializer());
            gson = gsonBuilder.create();
        }
        return gson;
    }
}
