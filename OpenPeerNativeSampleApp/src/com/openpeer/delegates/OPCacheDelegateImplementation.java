package com.openpeer.delegates;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.format.Time;
import android.util.Log;

import com.openpeer.javaapi.OPCacheDelegate;
import com.openpeer.openpeernativesampleapp.OpenPeerApplication;
import com.openpeer.openpeernativesampleapp.R;

public class OPCacheDelegateImplementation extends OPCacheDelegate{

	
	@Override
	public String fetch(String cookieNamePath) {
		// TODO connect with shared preferences
		SharedPreferences sharedPref = OpenPeerApplication.getAppContext().getSharedPreferences(
				OpenPeerApplication.getAppContext().getString( R.string.preference_file_key), Context.MODE_PRIVATE);
		
		return sharedPref.getString(cookieNamePath, "");
	}

	@Override
	public void store(String cookieNamePath, Time expires, String str) {
		Log.d("OPCacheDelegateImplementation","cookieNamePath "+cookieNamePath + " expires "+expires + " value "+str);
		// TODO connect with shared preferences
		SharedPreferences sharedPref = OpenPeerApplication.getAppContext().getSharedPreferences(
				OpenPeerApplication.getAppContext().getString( R.string.preference_file_key), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(cookieNamePath, str);
		editor.commit();
		
	}

	@Override
	public void clear(String cookieNamePath) {
		// TODO connect with shared preferences
		SharedPreferences sharedPref = OpenPeerApplication.getAppContext().getSharedPreferences(
				OpenPeerApplication.getAppContext().getString( R.string.preference_file_key), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.remove(cookieNamePath);
		editor.commit();
	}

}
