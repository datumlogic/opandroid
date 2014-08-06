package com.openpeer.javaapi;

import java.util.List;

import android.util.Log;


public class OPIdentityLookup {

	private long nativeClassPointer;
	
	private long nativeDelegatePointer;
	
	public static native String toDebugString(OPIdentityLookup lookup, boolean includeCommaPrefix);

	public static native OPIdentityLookup create(
                                     OPAccount account,
                                     OPIdentityLookupDelegate delegate,
                                     List<OPIdentityLookupInfo> identityLookupInfos,
                                     String identityServiceDomain
                                     );

	public native long getStableID();

	public native boolean isComplete();
	
	public native boolean wasSuccessful(
                               int outErrorCode,
                               String outErrorReason
                               );

	public native void cancel();

	public native List<OPIdentityContact> getUpdatedIdentities();
	public native List<OPIdentityLookupInfo> getUnchangedIdentities();
	public native List<OPIdentityLookupInfo> getInvalidIdentities();
	
    private native void releaseCoreObjects(); 
    
    protected void finalize() throws Throwable {
    	
    	if (nativeClassPointer != 0 || nativeDelegatePointer != 0)
    	{
    		Log.d("output", "Cleaning identity lookup core objects");
    		releaseCoreObjects();
    	}
    		
    	super.finalize();
    }
}
