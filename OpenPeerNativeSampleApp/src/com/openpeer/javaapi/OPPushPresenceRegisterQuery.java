package com.openpeer.javaapi;

import android.util.Log;

public class OPPushPresenceRegisterQuery {

	private long nativeClassPointer;
	
    public native long getID();

    public native boolean isComplete();
    
    private native void releaseCoreObjects(); 
    
    protected void finalize() throws Throwable {
    	
    	if (nativeClassPointer != 0)
    	{
    		Log.d("output", "Cleaning push presence register query core objects");
    		releaseCoreObjects();
    	}
    		
    	super.finalize();
    }
}
