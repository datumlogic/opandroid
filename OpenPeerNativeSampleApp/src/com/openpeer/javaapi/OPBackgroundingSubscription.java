package com.openpeer.javaapi;

import android.util.Log;

public class OPBackgroundingSubscription {
    
	private long nativeClassPointer;
	
	public native long getID();
    public native void cancel();
    
    private native void releaseCoreObjects(); 
    
    protected void finalize() throws Throwable {
    	
    	if (nativeClassPointer != 0)
    	{
    		Log.d("output", "Cleaning OPBackgroundingSubscription core objects");
    		releaseCoreObjects();
    	}
    		
    	super.finalize();
    }
}
