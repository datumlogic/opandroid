package com.openpeer.javaapi;

import java.util.List;

import android.util.Log;

public class OPPresenceResources {

	private long nativeClassPointer;
	
    public List<OPPresenceResource> mResources;

    public static native OPPresenceResources create();

    public static native OPPresenceResources extract(OPElement dataEl);
    public native void insert(OPElement dataEl);

    public native boolean hasData();
    public native OPElement toDebug();
    
    private native void releaseCoreObjects();
    
	protected void finalize() throws Throwable {

		if (nativeClassPointer != 0)
		{
			Log.d("output", "Cleaning OPPresenceResources core objects");
			releaseCoreObjects();
		}

		super.finalize();
	}
}
