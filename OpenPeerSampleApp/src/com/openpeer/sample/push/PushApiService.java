package com.openpeer.sample.push;

import com.openpeer.javaapi.OPMessage;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.Body;
import retrofit.http.Headers;
import retrofit.http.POST;

/**
 * Created by brucexia on 2014-07-29.
 */
public interface PushApiService {
    static final String UA_EP="https://go.urbanairship.com";
    static final String UA_PUSH_API = "/api/push";
    static final String HEADER_VERSION = "accept: application/vnd.urbanairship+json; version=3;";
    static final String HEADER_CONTENT_TYPE = "Content-Type: application/json";

    @Headers({HEADER_VERSION, HEADER_CONTENT_TYPE})
    @POST(UA_PUSH_API)
    void push(@Body PushMessage message, Callback<PushResult> cb);

}
