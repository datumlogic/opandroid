package com.openpeer.javaapi;

import android.util.Log;

public class OPPresenceStreetLocation {
	
	private long nativeClassPointer;

	String mFriendlyName;               // friendly name representing residence/business, e.g. "Nat's Pup"

	String mSuiteNumber;                // a suite number within a building
	String mBuildingFloor;              // the current floor of a building
	String mBuilding;                   // a building name/number when at an address with multiple buildings

	String mStreetNumber;               // the designated street number
	String mStreetNumberSuffix;         // the street number suffix

	String mStreetDirectionPrefix;      // N S E W NE NW SE SW if applicable
	String mStreetName;                 // name of the street
	String mStreetSuffix;               // E.g. "Ave" or "Dr"
	String mStreetDirectionSuffix;      // N S E W NE NW SE SW if applicable

	String mPostalCommunity;            // residence community, town, or city   (e.g. "Nepean")
	String mServiceCommunity;           // serviced by (typically greater) community, town, or city (e.g. "Ottawa")

	String mProvince;                   // state, province, or territory, (e.g. "Ontario")
	String mCountry;                    // e.g. "Canada"

	public static native OPPresenceStreetLocation create();

	public static native OPPresenceStreetLocation extract(OPElement dataEl);
	public native void insert(OPElement dataEl);

	public native boolean hasData();
	public native OPElement toDebug();

	private native void releaseCoreObjects();

	protected void finalize() throws Throwable {

		if (nativeClassPointer != 0)
		{
			Log.d("output", "Cleaning OPPresenceStreetLocation core objects");
			releaseCoreObjects();
		}

		super.finalize();
	}
}
