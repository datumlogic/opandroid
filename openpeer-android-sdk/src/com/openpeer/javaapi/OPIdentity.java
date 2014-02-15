package com.openpeer.javaapi;

import java.util.List;

import android.text.format.Time;


public class OPIdentity {

	public static native String toString(IdentityStates state);

	public static native String toDebugString(OPIdentity identity, Boolean includeCommaPrefix);

	public static native void login(
                              OPAccount account,
                              OPIdentityDelegate delegate,
                              String outerFrameURLUponReload,
                              String identityURI_or_identityBaseURI,
                              String identityProviderDomain // used when identity URI is of legacy or oauth-type
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

	public native String getStableID();

	public native Boolean isDelegateAttached();
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
	
	public native OPContact getSelfIdentityContact();

	public native String getInnerBrowserWindowFrameURL();

	public native void notifyBrowserWindowVisible();
	
	public native void notifyBrowserWindowClosed();
	public native String getNextMessageForInnerBrowerWindowFrame();
	
	public native void handleMessageFromInnerBrowserWindowFrame(String message);
	
	public native void startRolodexDownload(String inLastDownloadedVersion);  // if a previous version of the rolodex was downloaded/stored, pass in the version of the last information downloaded to prevent redownloading infomration again
	public native void refreshRolodexContacts();                                          // force a refresh of the contact list
	public native Boolean getDownloadedRolodexContacts(                                          // returns "true" if rolodex contacts were obtained, otherwise returns "false"
                                              Boolean outFlushAllRolodexContacts,         // if true, all rolodex contacts for this identity must be flushed out entirely
                                              String outVersionDownloaded,             // returns version information of downloaded rolodex contacts
                                              List<OPRolodexContact> outRolodexContacts // output list of contacts of rolodex contacts
                                              );

	public native void cancel();
}
