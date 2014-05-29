package com.openpeer.javaapi;

import java.util.List;


public class OPIdentityLookup {

	private long nativeClassPointer;
	
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
}
