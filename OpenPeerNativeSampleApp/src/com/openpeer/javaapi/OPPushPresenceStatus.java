package com.openpeer.javaapi;

import java.util.List;

import android.text.format.Time;
import android.util.Log;

public class OPPushPresenceStatus {

	private long nativeClassPointer;
	String mStatusID;                         // system will fill in this value

	private OPElement mPresenceEl;

	private Time mSent;                               // when was the status was sent, system will assign a value if not specified
	private Time mExpires;                            // optional, system will assign a long life time if not specified

	private OPContact mFrom;                        // what peer sent the status (system will fill in if sending a status out)

	private List<OPPushPresencePushInfo> mPushInfos;                  // each service has its own push information

	public OPElement getPresenceEl() {
		return mPresenceEl;
	}

	public void setPresenceEl(OPElement mPresenceEl) {
		this.mPresenceEl = mPresenceEl;
	}

	public Time getSent() {
		return mSent;
	}

	public void setSent(Time mSent) {
		this.mSent = mSent;
	}

	public Time getExpires() {
		return mExpires;
	}

	public void setExpires(Time mExpires) {
		this.mExpires = mExpires;
	}

	public OPContact getFrom() {
		return mFrom;
	}

	public void setFrom(OPContact mFrom) {
		this.mFrom = mFrom;
	}

	public List<OPPushPresencePushInfo> getPushInfos() {
		return mPushInfos;
	}

	public void setPushInfos(List<OPPushPresencePushInfo> mPushInfos) {
		this.mPushInfos = mPushInfos;
	}

	public static native OPElement createEmptyPresence();  // create an emty status JSON object ready to be filled with presence data

	public native boolean hasData();
	public native OPElement toDebug();
	
    private native void releaseCoreObjects(); 
    
    protected void finalize() throws Throwable {
    	
    	if (nativeClassPointer != 0)
    	{
    		Log.d("output", "Cleaning push presence status core objects");
    		releaseCoreObjects();
    	}
    		
    	super.finalize();
    }
}
