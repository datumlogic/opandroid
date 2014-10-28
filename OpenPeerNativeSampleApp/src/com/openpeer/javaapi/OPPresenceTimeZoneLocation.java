package com.openpeer.javaapi;

import android.text.format.Time;
import android.util.Log;

public class OPPresenceTimeZoneLocation {

	private long nativeClassPointer;
	
	public Time mOffset;           // +/- offset from GMT / UTC to calculate local time from UTC time
	public String mAbbreviation;       // time zone abbreviation for active time zone
	public String mName;               // current time zone full name for active time zone

	public String mCity;               // basing time zone off this city's location
	public String mCountry;            // basing time zone within this country

    public static native OPPresenceTimeZoneLocation create();

    public static native OPPresenceTimeZoneLocation extract(OPElement dataEl);
    public native void insert(OPElement dataEl);

    public native boolean hasData();
    public native OPElement toDebug();
    
    private native void releaseCoreObjects();
    
	protected void finalize() throws Throwable {

		if (nativeClassPointer != 0)
		{
			Log.d("output", "Cleaning OPPresenceTimeZoneLocation core objects");
			releaseCoreObjects();
		}

		super.finalize();
	}
}
