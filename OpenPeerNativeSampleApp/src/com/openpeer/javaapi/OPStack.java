package com.openpeer.javaapi;


public class OPStack {

	public native void setup(
            OPStackDelegate stackDelegate,
            OPMediaEngineDelegate mediaEngineDelegate,
            String appID,       // organization assigned ID for the application e.g. "com.xyz123.app1"
            String appName,     // a branded human readable application name, e.g. "Hookflash"
            String appImageURL, // an HTTPS downloadable branded image for the application
            String appURL,      // an HTTPS URL webpage / website that offers more information about application
            String userAgent,   // e.g. "hookflash/1.0.1001a (Android/Samsung S4)"
            String deviceID,    // e.g. uuid of device "7bff560b84328f161494eabcba5f8b47a316be8b"
            String os,          // e.g. "Android 4.0"
            String system       // e.g. "Samsung S4"
            );
	
	public native void shutdown();
}
