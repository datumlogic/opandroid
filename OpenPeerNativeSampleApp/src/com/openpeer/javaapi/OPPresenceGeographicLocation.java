package com.openpeer.javaapi;

import android.util.Log;

public class OPPresenceGeographicLocation {

	private long nativeClassPointer;
	
	public double mLatitude;                  // degrees from equator; positive is north and negative is south
	public double mLongitude;                 // degrees from zero meridian
	public double mGeographicAccuracyRadius;  // radious of accuracy for the latitude/longitude as expressed in meters; anegative value indicates an invalid geographic coordinate

	public double mAltitude;                  // height above sea level as measured in meters
	public double mAltitudeAccuracy;          // the absolute value of the + or - altitude accuracy in meters; a negative value indicates an invalid altitude

	public double mDirection;                 // the direction being headed in degrees on a circle (0 = north, 90 = east, 180 = south, 270 = west)
	public double mSpeed;                     // speed moving in direction (meters / second); a negative value indicates the speed/direction are invalid

    public static native OPPresenceGeographicLocation create();

    public static native OPPresenceGeographicLocation extract(OPElement dataEl);
    public native void insert(OPElement dataEl);

    public native boolean hasData();
    public native OPElement toDebug();
    
    private native void releaseCoreObjects();
    
	protected void finalize() throws Throwable {

		if (nativeClassPointer != 0)
		{
			Log.d("output", "Cleaning OPPresenceGeographicLocation core objects");
			releaseCoreObjects();
		}

		super.finalize();
	}
}
