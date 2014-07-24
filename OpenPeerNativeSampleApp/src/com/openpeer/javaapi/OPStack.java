package com.openpeer.javaapi;

import android.text.format.Time;
import android.util.Log;


public class OPStack {

	private long nativeClassPointer;
	
	private long nativeDelegatePointer;
	
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
	
    private native void releaseCoreObjects(); 
    
    protected void finalize() throws Throwable {
    	
    	if (nativeClassPointer != 0 || nativeDelegatePointer != 0)
    	{
    		Log.d("output", "Cleaning stack core objects");
    		releaseCoreObjects();
    	}
    		
    	super.finalize();
    }
}
