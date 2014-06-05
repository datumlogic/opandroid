package com.openpeer.sample.app.delegates;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.format.Time;
import android.util.Log;

import com.openpeer.javaapi.OPCacheDelegate;
import com.openpeer.sample.app.OpenPeerApplication;
import com.openpeer.sample.app.R;

import javax.inject.Inject;

public class OPCacheDelegateImplementation extends OPCacheDelegate {

    @Inject
    OpenPeerApplication appContext;

    @Override
    public String fetch(String cookieNamePath) {
        // TODO connect with shared preferences
        SharedPreferences sharedPref = appContext.getSharedPreferences(
                appContext.getString(R.string.cache_file_name), Context.MODE_PRIVATE);

        return sharedPref.getString(cookieNamePath, "");
    }

    @Override
    public void store(String cookieNamePath, Time expires, String str) {
        Log.d("OPCacheDelegateImplementation", "cookieNamePath " + cookieNamePath + " expires " + expires + " value " + str);
        // TODO connect with shared preferences
        SharedPreferences sharedPref = appContext.getSharedPreferences(
                appContext.getString(R.string.cache_file_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(cookieNamePath, str);
        editor.commit();

    }

    @Override
    public void clear(String cookieNamePath) {
        // TODO connect with shared preferences
        SharedPreferences sharedPref = appContext.getSharedPreferences(
                appContext.getString(R.string.cache_file_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(cookieNamePath);
        editor.commit();
    }

}
