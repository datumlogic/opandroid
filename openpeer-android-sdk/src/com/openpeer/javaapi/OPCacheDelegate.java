package com.openpeer.javaapi;

import android.text.format.Time;


public abstract class OPCacheDelegate {

	public abstract String fetch(String cookieNamePath);
	public abstract void store(
                       String cookieNamePath,
                       Time expires,
                       String str
                       );
	public abstract void clear(String cookieNamePath);
}
