package com.openpeer.javaapi;

public class OPContact {

	private long nativeClassPointer;

	/**
	 * @ExcludeFromJavadoc
	 * @param contact
	 * @param includeCommaPrefix
	 * @return
	 */
	public static native String toDebugString(OPContact contact, boolean includeCommaPrefix);

	public static native OPContact createFromPeerFilePublic(
			OPAccount account,
			String peerFilePublicEl
			);

	/**
	 * Construct OPContact instance for the account
	 * 
	 * @param account
	 * @return
	 */
	public static native OPContact getForSelf(OPAccount account);

	public native long getStableID();

	/**
	 * If the contact is the logged in user self.
	 * 
	 * @return
	 */
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
