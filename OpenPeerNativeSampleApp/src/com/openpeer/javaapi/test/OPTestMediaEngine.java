package com.openpeer.javaapi.test;

import com.openpeer.javaapi.OPMediaEngine;

public class OPTestMediaEngine extends OPMediaEngine {

    public static OPTestMediaEngine getTestInstance()
    {
        if (_instance == null)
        {
            _instance = OPMediaEngine.singleton();
        }
		return (OPTestMediaEngine) _instance;
    }
    
	public native void startVoice();
	public native void stopVoice();
    
	public native void startVideoChannel();
	public native void stopVideoChannel();
	
	public native void setReceiverAddress(String receiverAddress);
	public native String getReceiverAddress();
}
