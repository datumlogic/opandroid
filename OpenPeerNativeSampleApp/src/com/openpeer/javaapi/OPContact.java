package com.openpeer.javaapi;


public class OPContact {

	public static native String toDebugString(OPContact contact, boolean includeCommaPrefix);

    public static native OPContact createFromPeerFilePublic(
                                                OPAccount account,
                                                String peerFilePublicEl
                                                );

    public static native OPContact getForSelf(OPAccount account);

    public native long getStableID();

    public native boolean isSelf();
    public native String getPeerURI();
    
    public native String getPeerFilePublic();

    public native OPAccount getAssociatedAccount();

    public native void hintAboutLocation(String contactsLocationID);
}
