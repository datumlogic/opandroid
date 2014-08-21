package com.openpeer.javaapi;

import android.util.Log;

public class OPComposingStatus {
	
	private long nativeClassPointer; 

    public static native String toString(ComposingStates state);
    public static native ComposingStates toComposingState(String state);

    public static native  OPComposingStatus extract(String dataEl);
    public native void insert(String dataEl);

    public native boolean hasData();
    public native String toDebug();
    
    private native void releaseCoreObjects(); 
    
    protected void finalize() throws Throwable {
    	
    	if (nativeClassPointer != 0)
    	{
    		Log.d("output", "Cleaning OPComposingStatus core objects");
    		releaseCoreObjects();
    	}
    		
    	super.finalize();
    }
}
