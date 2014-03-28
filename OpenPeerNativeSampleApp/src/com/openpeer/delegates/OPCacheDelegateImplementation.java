package com.openpeer.delegates;

import android.text.format.Time;

import com.openpeer.javaapi.OPCacheDelegate;

public class OPCacheDelegateImplementation extends OPCacheDelegate{

	
	@Override
	public String fetch(String cookieNamePath) {
		// TODO connect with shared preferences
		return "Stored";
	}

	@Override
	public void store(String cookieNamePath, Time expires, String str) {
		// TODO connect with shared preferences
		
	}

	@Override
	public void clear(String cookieNamePath) {
		// TODO connect with shared preferences
		
	}

}
