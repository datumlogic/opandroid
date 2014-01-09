package com.openpeer.javaapi;

import android.text.format.Time;


public class OPCache {

	public static native void setup(OPCacheDelegate delegate);

	public static native OPCache singleton();

	public native String fetch(String cookieNamePath);
	
	public native void store(
					   String  cookieNamePath,
                       Time expires,
                       String str
                       );
	
	public native void clear(String cookieNamePath);
}
