package com.openpeer.javaapi;

public class OPSettings {

	public static native void setup(OPSettingsDelegate delegate);
	public static native Boolean apply(String jsonSettings);
	public static native void applyDefaults();
}
