package com.openpeer.javaapi;

import android.util.Log;

public class OPCallSystemMessage {

	private long nativeClassPointer;

	public static native String toString(CallSystemMessageTypes type);
	public static native  CallSystemMessageTypes toCallSystemMessageType(String type);

	public static native OPCallSystemMessage create(CallSystemMessageTypes type,
			OPContact callee,
			int errorCode
			);

	//-----------------------------------------------------------------------
	// PURPOSE: extract a call system message type from a system message
	// RETURNS: valid call system message or null CallSystemMessagePtr()
	//          if none is present.
	public static native  OPCallSystemMessage extract(
			OPElement dataEl,
			OPAccount account // callee is in context of account thus account is needed
			);

	//-----------------------------------------------------------------------
	// PURPOSE: insert call system information into a status message
	public native void insert(OPElement dataEl);

	//-----------------------------------------------------------------------
	// PURPOSE: check to see if the structure has any data
	public native boolean hasData();

	//-----------------------------------------------------------------------
	// PURPOSE: returns debug information
	public native OPElement toDebug();

	private native void releaseCoreObjects(); 

	protected void finalize() throws Throwable {

		if (nativeClassPointer != 0)
		{
			Log.d("output", "Cleaning OPCallSystemMessage core objects");
			releaseCoreObjects();
		}

		super.finalize();
	}
}
