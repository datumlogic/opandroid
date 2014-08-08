package com.openpeer.sample.push;

import android.util.Base64;

import com.openpeer.javaapi.OPMessage;
import com.urbanairship.AirshipConfigOptions;
import com.urbanairship.UAirship;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

public class UAPushProviderImpl {
    private static final String TAG = UAPushProviderImpl.class.getSimpleName();
    PushApiService service;

    public UAPushProviderImpl() {
        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestInterceptor.RequestFacade request) {
                request.addHeader("Authorization", getUAAuthHeader());
            }
        };
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(PushApiService.UA_EP)
                .setRequestInterceptor(requestInterceptor)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        service = restAdapter.create(PushApiService.class);

    }

    public void pushMessage(OPMessage message, PushToken token, Callback<PushResult> callback) {
        PushMessage msg = PushMessage.fromOPMessage(message, token);
        service.push(msg, callback);
    }

    private String getUAAuthHeader() {
        AirshipConfigOptions options = UAirship.shared().getAirshipConfigOptions();

        return "Basic " + Base64.encodeToString((options.getAppKey() + ":" + options.getAppSecret()).getBytes(), Base64.DEFAULT);
    }

}
