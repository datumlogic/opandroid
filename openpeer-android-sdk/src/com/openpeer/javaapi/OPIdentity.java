package com.openpeer.javaapi;

import java.util.List;

import android.text.format.Time;


public class OPIdentity {

	private long nativeClassPointer;
	
	public static native String toString(IdentityStates state);

	public static native String toDebugString(OPIdentity identity, Boolean includeCommaPrefix);
	
	public native OPIdentity login(
                              OPAccount account,
                              OPIdentityDelegate delegate,
                              String identityProviderDomain, // used when identity URI is of legacy or oauth-type
                              String identityURI_or_identityBaseURI,
                              String outerFrameURLUponReload
                              );
	
	public static native OPIdentity loginWithIdentityPreauthorized(
			OPAccount account,
			OPIdentityDelegate delegate,
			String identityProviderDomain,          // used when identity URI is of legacy or oauth-type
			String identityURI,
			String identityAccessToken,
			String identityAccessSecret,
			Time identityAccessSecretExpires
			);

	public native IdentityStates getState(
                                   int outLastErrorCode,
                                   String outLastErrorReason
                                   );

	public native long getStableID();

	public native boolean isDelegateAttached();
	public native void attachDelegate(
                                OPIdentityDelegate delegate,
                                String outerFrameURLUponReload
                                );
	
	public native void attachDelegateAndPreauthorizedLogin(
			OPIdentityDelegate delegate,
			String identityAccessToken,
			String identityAccessSecret,
            Time identityAccessSecretExpires
            );

	public native String getIdentityURI();
	
	public native String getIdentityProviderDomain();
	
	public native OPIdentityContact getSelfIdentityContact();

	public native String getInnerBrowserWindowFrameURL();

	public native void notifyBrowserWindowVisible();
	
	public native void notifyBrowserWindowClosed();
	public native String getNextMessageForInnerBrowerWindowFrame();
	
	public native void handleMessageFromInnerBrowserWindowFrame(String message);
	
	public native void startRolodexDownload(String inLastDownloadedVersion);  // if a previous version of the rolodex was downloaded/stored, pass in the version of the last information downloaded to prevent redownloading infomration again
	public native void refreshRolodexContacts();                                          // force a refresh of the contact list
	public native OPDownloadedRolodexContacts getDownloadedRolodexContacts();
	
	public native void cancel();
//	public String toString(){
//		return super.toString()+ " identityUri " + getIdentityURI()+" innerBrowserWindowFrameURL " + 
//				getInnerBrowserWindowFrameURL() + " identityProviderDomain "+getIdentityProviderDomain()
//				+ " state " + getState(0,"");
//	}
}
