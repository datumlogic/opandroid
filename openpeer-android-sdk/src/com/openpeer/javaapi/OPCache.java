package com.openpeer.javaapi;

import android.text.format.Time;


public class OPCache {

	public static native void setup(OPCacheDelegate delegate);

	public static native String fetch(String cookieNamePath);
	
	public static native void store(
					   String  cookieNamePath,
                       Time expires,
                       String str
                       );
	
	public static native void clear(String cookieNamePath);
}
