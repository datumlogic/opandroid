package com.openpeer.javaapi;


public class OPContact {

	public static native String toDebugString(OPContact contact, Boolean includeCommaPrefix);

    public static native OPContact createFromPeerFilePublic(
                                                OPAccount account,
                                                OPElement peerFilePublicEl,
                                                String stableIDIfKnown // (if known)
                                                );

    public static native OPContact getForSelf(OPAccount account);

    public native long getID();

    public native Boolean isSelf();
    public native String getPeerURI();
    
    public native String getFindSecret();
    
    public native String getStableUniqueID();                                // get stable unique ID (if known - can only be known via identity lookup)
    
    public native Boolean hasPeerFilePublic();
    
    public native OPElement savePeerFilePublic();

    public native OPAccount getAssociatedAccount();

    public native void hintAboutLocation(String contactsLocationID);
}
