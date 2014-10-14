package com.openpeer.javaapi;

import android.util.Log;

public class OPPushMessagingQuery {
	
	private long nativeClassPointer;

    public native long getID();

    public native void cancel();

    public native boolean isUploaded();
    public native OPPushMessage getPushMessage();
	
	
    private native void releaseCoreObjects(); 
    
    protected void finalize() throws Throwable {
    	
    	if (nativeClassPointer != 0)
    	{
    		Log.d("output", "Cleaning push messaging query core objects");
    		releaseCoreObjects();
    	}
    		
    	super.finalize();
    }
}
