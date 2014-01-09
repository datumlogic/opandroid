package com.openpeer.javaapi;

import java.util.List;


public class OPIdentityLookup {

	public static native String toDebugString(OPIdentityLookup lookup, Boolean includeCommaPrefix);

	public static native OPIdentityLookup create(
                                     OPAccount account,
                                     OPIdentityLookupDelegate delegate,
                                     List<String> identityURIs,
                                     String identityServiceDomain,
                                     Boolean checkForUpdatesOnly   // "true" is a "cheap" server operation; "false" is "expensive" server operation
                                     );

	public native long getID();

	public native Boolean isComplete();
	
	public native Boolean wasSuccessful(
                               int outErrorCode,
                               String outErrorReason
                               );

	public native void cancel();

	public native List<OPIdentityLookupInfo> getIdentities();
}
