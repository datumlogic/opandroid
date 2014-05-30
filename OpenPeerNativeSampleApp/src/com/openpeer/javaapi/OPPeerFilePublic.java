package com.openpeer.javaapi;

public class OPPeerFilePublic {

	public String peerFileString;
	
	public native String saveToString();
	public static native OPPeerFilePublic loadFromString(String peerFilePublicString);
	
}
