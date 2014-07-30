package com.openpeer.javaapi;



public class OPContact {

	private long nativeClassPointer;
	
	public long getNativeClassPointer() {
		return nativeClassPointer;
	}

	public void setNativeClassPointer(long nativeClassPointer) {
		this.nativeClassPointer = nativeClassPointer;
	}

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
    
    private native void releaseCoreObjects();
    
    protected void finalize() throws Throwable {
    	
    	if (nativeClassPointer != 0)
    	{
    		releaseCoreObjects();
    	}
    		
    	super.finalize();
    }
}
