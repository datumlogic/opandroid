package com.openpeer.javaapi;

import java.util.List;


public class OPAccount {

	public static native String toString(AccountStates state);

	public static native String toDebugString(OPAccount account, Boolean includeCommaPrefix);

    public static native OPAccount login(
                             OPAccountDelegate delegate,
                             OPConversationThreadDelegate conversationThreadDelegate,
                             OPCallDelegate callDelegate,
                             String namespaceGrantOuterFrameURLUponReload,
                             String namespaceGrantServiceDomain,
                             String grantID,
                             String grantSecret,
                             String lockboxServiceDomain,
                             Boolean forceCreateNewLockboxAccount
                             );

    public static native OPAccount relogin(
    						   OPAccountDelegate delegate,
    						   OPConversationThreadDelegate conversationThreadDelegate,
    						   OPCallDelegate callDelegate,
    						   String namespaceGrantOuterFrameURLUponReload,
                               OPElement reloginInformation
                               );

    public native long getID();

    public native AccountStates getState(
                                   int outErrorCode,
                                   String outErrorReason
                                   );

    public native OPElement getReloginInformation();   // NOTE: will return ElementPtr() is relogin information is not available yet
    
    public native String getLocationID();

    public native void shutdown();

    public native OPElement savePeerFilePrivate();
    //public native SecureByteBlockPtr getPeerFilePrivateSecret();

    public native List<OPIdentity> getAssociatedIdentities();
    public native void removeIdentities (List<OPIdentity> identitiesToRemove);

    public native String getInnerBrowserWindowFrameURL();

    public native void notifyBrowserWindowVisible();
    
    public native void notifyBrowserWindowClosed();

    public native OPElement getNextMessageForInnerBrowerWindowFrame();
    public native void handleMessageFromInnerBrowserWindowFrame(OPElement unparsedMessage);
 
}
