package com.openpeer.javaapi;

public class OPSettings {

	private long nativeClassPointer;

	public static native void setup(OPSettingsDelegate delegate);

	public static native void setString(String key, String value);

	public static native void setInt(String key, long value);

	public static native void setUInt(String key, long value);

	public static native void setBool(String key, boolean value);

	public static native void setFloat(String key, float value);

	public static native void setDouble(String key, double value);

	public static native void clear(String key);

	/**
	 * Apply settings supplied form json blob. This is recommended way for applying a bunch of settings
	 * 
	 * @param jsonSettings
	 * @return
	 */
	public static native boolean apply(String jsonSettings);

	/**
	 * Apply default settings
	 */
	public static native void applyDefaults();

	/**
	 * @ExcludeFromJavadoc
	 */
	public static native void redirectLog();

}
