package com.openpeer.app;

import android.content.Context;

/**
 * 
 * Global SDK configurations. TODO: put them in a file like
 * openpeersdk.properties
 * 
 * All configurable items should have a default value and the value will be
 * overwritten if the configuration file exists
 */
public class OPSdkConfig {
	// TODO add configuration items in
	private static final String PATH_CONFIG_FILE = "openpeersdk.properties";
	public static boolean debug = true;

	public static void init(Context context) {
		// if(config exists){
		// }
	}

}
