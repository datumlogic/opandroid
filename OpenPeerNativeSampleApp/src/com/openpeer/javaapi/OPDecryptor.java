package com.openpeer.javaapi;

import android.util.Log;

public class OPDecryptor {

	
	private long nativeClassPointer; 
    //-----------------------------------------------------------------------
    // PURPOSE: create an decryptor that will decrypt a given file
    public static native OPDecryptor create(
                                String inSourceFileName,
                                String inEncoding
                                );

    //-----------------------------------------------------------------------
    // PURPOSE: decrypt the next block of data and return result data
    // RETURN:  next block of decrypted data or null SecureByteBlockPtr()
    //          when no more data is available (or error occured).
    public native byte[] decrypt();

    //-----------------------------------------------------------------------
    // PURPOSE: finalize and return success/fail status
    // RETURN:  true if successful otherwise false
    public native boolean coreFinalize();
    
    private native void releaseCoreObjects(); 
    
    protected void finalize() throws Throwable {
    	
    	if (nativeClassPointer != 0)
    	{
    		Log.d("output", "Cleaning OPDecryptor core objects");
    		releaseCoreObjects();
    	}
    		
    	super.finalize();
    }
}
