package com.openpeer.javaapi;

import android.text.format.Time;

/**
 * @ExcludeFromJavadoc 
 * 
 */
public class OPCache {

	private long nativeClassPointer;

	/**
	 * Setup OPCacheDelegate. The delegate instance MUST be kept valid throughout the app lifecycle
	 * 
	 * @param delegate
	 */
	public static native void setup(OPCacheDelegate delegate);

	public static native String fetch(String cookieNamePath);

	public static native void store(
			String cookieNamePath,
			Time expires,
			String str
			);

	public static native void clear(String cookieNamePath);
}
