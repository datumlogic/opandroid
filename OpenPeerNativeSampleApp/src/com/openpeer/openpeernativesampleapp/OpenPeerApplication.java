package com.openpeer.openpeernativesampleapp;

import android.app.Application;
import android.content.Context;

public class OpenPeerApplication extends Application {

	private static Context context;

    public void onCreate(){
        super.onCreate();
        OpenPeerApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return OpenPeerApplication.context;
    }
}
