package com.openpeer.javaapi;
/**
 * @ExcludeFromJavadoc
 *
 */
public class OPPeerFilePublic {

	private String mPeerFileString;

	public OPPeerFilePublic(String str) {
		mPeerFileString = str;
	}

	public OPPeerFilePublic() {
	}

	public String getPeerFileString() {
		return mPeerFileString;
	}

	public void setPeerFileString(String peerFile) {
		mPeerFileString = peerFile;
	}

}
