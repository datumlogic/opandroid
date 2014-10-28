package com.openpeer.javaapi;

import android.util.Log;

public class OPPresenceStatus {

	private long nativeClassPointer;
	
    public PresenceStatuses mStatus = PresenceStatuses.PresenceStatus_None;   // basic status
    public String mExtendedStatus;                           // extended status property related to status

    public String mStatusMessage;                            // human readable message to display about status

    public int mPriority;                                 // relative priority of this status; higher value is a greater priority;

    public static native OPPresenceStatus create();

    public static native OPPresenceStatus extract(OPElement dataEl);
    public native void insert(OPElement dataEl);

    public native boolean hasData();
    public native OPElement toDebug();
    
	private native void releaseCoreObjects(); 

	protected void finalize() throws Throwable {

		if (nativeClassPointer != 0)
		{
			Log.d("output", "Cleaning OPPresenceStatus core objects");
			releaseCoreObjects();
		}

		super.finalize();
	}
}
