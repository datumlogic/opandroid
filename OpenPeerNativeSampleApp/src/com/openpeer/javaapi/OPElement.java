package com.openpeer.javaapi;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class OPElement {
	private long nativeClassPointer;
	
	public native String convertToString();
	public JSONObject convertToJSONObject()
	{
		try {
			return new JSONObject(convertToString());
		} catch (JSONException e) {
			Log.e("output", "Could not parse malformed JSON: \"" + convertToString() + "\"");
		}
		return new JSONObject();
	}
	
    private native void releaseCoreObjects(); 
    
    protected void finalize() throws Throwable {
    	
    	if (nativeClassPointer != 0)
    	{
    		Log.d("output", "Cleaning OPElement core objects");
    		releaseCoreObjects();
    	}
    		
    	super.finalize();
    }
}
