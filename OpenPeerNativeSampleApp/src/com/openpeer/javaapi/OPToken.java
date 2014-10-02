package com.openpeer.javaapi;

import android.text.format.Time;
import android.util.Log;

public class OPToken {

	private long nativeClassPointer; 
	
    public String mID;
    public String mSecret;
    public String mSecretEncrypted;
    public Time mExpires;

    public String mProof;
    public String mNonce;
    public String mResource;

    public native boolean hasData();
    public native OPElement toDebug();

    public native void mergeFrom(
                   OPToken source,
                   boolean overwriteExisting
                   );

    public static native OPToken create(
                        String masterSecret,
                        String associatedID,
                        Time validDuration
                        );


    public static native OPToken create(OPElement elem);
    public native OPToken createProof(
                      String resource,
                      Time validDuration
                      );

    public native OPElement createElement();
    public native boolean validate(OPToken proofToken);
    public native String validate(
                  String inMasterSecret
                  );
    
    private native void releaseCoreObjects(); 
    
    protected void finalize() throws Throwable {
    	
    	if (nativeClassPointer != 0)
    	{
    		Log.d("output", "Cleaning OPComposingStatus core objects");
    		releaseCoreObjects();
    	}
    		
    	super.finalize();
    }
}
