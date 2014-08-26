package com.openpeer.javaapi;

import android.util.Log;

public class OPBackgroundingNotifier {

	private long nativeClassPointer;
	
    public native long getID();
    public native void ready();
    
    private native void releaseCoreObjects(); 
    
    protected void finalize() throws Throwable {
    	
    	if (nativeClassPointer != 0)
    	{
    		Log.d("output", "Cleaning OPBackgroundingNotifier core objects");
    		releaseCoreObjects();
    	}
    		
    	super.finalize();
    }
}
