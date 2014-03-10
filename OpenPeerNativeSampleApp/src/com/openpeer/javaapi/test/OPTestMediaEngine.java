package com.openpeer.javaapi.test;

import com.openpeer.javaapi.OPMediaEngine;

public class OPTestMediaEngine extends OPMediaEngine {
    
	public native void startVoice();
	public native void stopVoice();
    
	public native void startVideoChannel();
	public native void stopVideoChannel();

}
