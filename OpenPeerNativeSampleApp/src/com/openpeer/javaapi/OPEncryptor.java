package com.openpeer.javaapi;

import android.util.Log;

public class OPEncryptor {

	
	private long nativeClassPointer; 
    //-----------------------------------------------------------------------
    // PURPOSE: create an encryptor that will encrypt a given file
    // RETURNS: null IEncryptorPtr() if file cannot be open otherwise
    //          valid encryptor
    public static native OPEncryptor create(
                                String inSourceFileName,
                                String inEncodingServiceName
                                );

    //-----------------------------------------------------------------------
    // PURPOSE: encrypt the next block of data and return result data
    // RETURN:  next block of encrypted data or null SecureByteBlockPtr()
    //          when no more data is available (or error occured).
    public native byte[] encrypt();

    //-----------------------------------------------------------------------
    // PURPOSE: finalize and return the encoding string
    // RETURN:  the encoding string otherwise String() when the encryption
    //          failed
    public native String coreFinalize();
    
    private native void releaseCoreObjects(); 
    
    protected void finalize() throws Throwable {
    	
    	if (nativeClassPointer != 0)
    	{
    		Log.d("output", "Cleaning OPEncryptor core objects");
    		releaseCoreObjects();
    	}
    		
    	super.finalize();
    }
}
