package com.openpeer.javaapi;

import android.util.Log;

public class OPBackgroundingQuery {
	private long nativeClassPointer;
	
	public native long getID();
    public native boolean isReady();
    public native long totalBackgroundingSubscribersStillPending();
    
    private native void releaseCoreObjects(); 
    
    protected void finalize() throws Throwable {
    	
    	if (nativeClassPointer != 0)
    	{
    		Log.d("output", "Cleaning OPBackgroundingQuery core objects");
    		releaseCoreObjects();
    	}
    		
    	super.finalize();
    }
}
