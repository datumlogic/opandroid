package com.openpeer.javaapi;

import android.util.Log;

public class OPPushMessagingRegisterQuery {
	
	private long nativeClassPointer;

	public native long getID();

	//todo: create structure to return error code and error reason
    public native boolean isComplete();
    
    private native void releaseCoreObjects(); 
    
    protected void finalize() throws Throwable {
    	
    	if (nativeClassPointer != 0)
    	{
    		Log.d("output", "Cleaning push messaging register query core objects");
    		releaseCoreObjects();
    	}
    		
    	super.finalize();
    }
}
