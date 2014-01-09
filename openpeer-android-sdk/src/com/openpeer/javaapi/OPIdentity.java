package com.openpeer.javaapi;


public class OPIdentity {

	public static native String toString(IdentityStates state);

	public static native String toDebugString(OPIdentity identity, Boolean includeCommaPrefix);

	public static native OPIdentity login(
                              OPAccount account,
                              OPIdentityDelegate delegate,
                              String outerFrameURLUponReload,
                              String identityURI_or_identityBaseURI,
                              String identityProviderDomain // used when identity URI is of legacy or oauth-type
                              );

	public native IdentityStates getState(
                                   int outLastErrorCode,
                                   String outLastErrorReason
                                   );

	public native long getID();

	public native Boolean isDelegateAttached();
	public native void attachDelegate(
                                OPIdentityDelegate delegate,
                                String outerFrameURLUponReload
                                );

	public native String getIdentityURI();
	
	public native String getIdentityProviderDomain();
	
	public native OPElement getSignedIdentityBundle();

	public native String getInnerBrowserWindowFrameURL();

	public native void notifyBrowserWindowVisible();
	
	public native void notifyBrowserWindowClosed();
	public native OPElement getNextMessageForInnerBrowerWindowFrame();
	
	public native void handleMessageFromInnerBrowserWindowFrame(OPElement message);

	public native void cancel();
}
