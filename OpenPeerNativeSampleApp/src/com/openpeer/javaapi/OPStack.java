package com.openpeer.javaapi;

import android.text.format.Time;


public class OPStack {

	private long nativeClassPointer;
	
	public static native OPStack singleton();
	
	public native void setup(
            OPStackDelegate stackDelegate,
            OPMediaEngineDelegate mediaEngineDelegate
            );
	
	public native void shutdown();
	
	public static native String createAuthorizedApplicationID(
			String applicationID,
			String applicationIDSharedSecret,
            Time expires
            );
	
	public static native Time getAuthorizedApplicationIDExpiry(
            String authorizedApplicationID,
            Time outRemainingDurationAvailable
            );
	
	public static native boolean isAuthorizedApplicationIDExpiryWindowStillValid(
            String authorizedApplicationID,
            Time minimumValidityWindowRequired
            );
}
