package com.openpeer.javaapi;

import java.util.List;


public class OPIdentityLookup {

	public static native String toDebugString(OPIdentityLookup lookup, Boolean includeCommaPrefix);

	public static native OPIdentityLookup create(
                                     OPAccount account,
                                     OPIdentityLookupDelegate delegate,
                                     List<OPIdentityLookupInfo> identityURIs,
                                     String identityServiceDomain
                                     );

	public native String getStableID();

	public native Boolean isComplete();
	
	public native Boolean wasSuccessful(
                               int outErrorCode,
                               String outErrorReason
                               );

	public native void cancel();

	public native List<OPIdentityContact> getUpdatedIdentities();
	public native List<OPIdentityLookupInfo> getUnchangedIdentities();
	public native List<OPIdentityLookupInfo> getInvalidIdentities();
}
