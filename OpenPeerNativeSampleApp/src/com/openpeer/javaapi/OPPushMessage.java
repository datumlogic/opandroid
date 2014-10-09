package com.openpeer.javaapi;

import java.util.List;
import java.util.Map;

import android.text.format.Time;
import android.util.Log;

public class OPPushMessage {
	private long nativeClassPointer;
	
    private String mMessageID;                  // system will fill in this value

    private String mMimeType;
    private String mFullMessage;                // only valid for "text/<sub-type>" mime types
    private byte[] mRawFullMessage; // raw version of the message (only supply if full message is empty and sending binary)

    private String mPushType;                   // worked with registration "mappedType" to filter out push message types
    private List<OPPushInfo> mPushInfos;            // each service has its own push information

    private Time mSent;                         // when was the message sent, system will assign a value if not specified
    private Time mExpires;                      // optional, system will assign a long life time if not specified

    private OPContact mFrom;                  // what peer sent the message (system will fill in if sending a message out)

    private Map<PushStates, List<OPPushStateContactDetail>> mPushStateDetails;

	public String getMessageID() {
		return mMessageID;
	}

	public void setMessageID(String mMessageID) {
		this.mMessageID = mMessageID;
	}

	public String getMimeType() {
		return mMimeType;
	}

	public void setMimeType(String mMimeType) {
		this.mMimeType = mMimeType;
	}

	public String getFullMessage() {
		return mFullMessage;
	}

	public void setFullMessage(String mFullMessage) {
		this.mFullMessage = mFullMessage;
	}

	public byte[] getRawFullMessage() {
		return mRawFullMessage;
	}

	public void setRawFullMessage(byte[] mRawFullMessage) {
		this.mRawFullMessage = mRawFullMessage;
	}

	public String getPushType() {
		return mPushType;
	}

	public void setPushType(String mPushType) {
		this.mPushType = mPushType;
	}

	public List<OPPushInfo> getPushInfos() {
		return mPushInfos;
	}

	public void setPushInfos(List<OPPushInfo> mPushInfos) {
		this.mPushInfos = mPushInfos;
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

	public Map<PushStates, List<OPPushStateContactDetail>> getPushStateDetails() {
		return mPushStateDetails;
	}

	public void setPushStateDetails(Map<PushStates, List<OPPushStateContactDetail>> mPushStateDetails) {
		this.mPushStateDetails = mPushStateDetails;
	}
	
    private native void releaseCoreObjects(); 
    
    protected void finalize() throws Throwable {
    	
    	if (nativeClassPointer != 0)
    	{
    		Log.d("output", "Cleaning stack core objects");
    		releaseCoreObjects();
    	}
    		
    	super.finalize();
    }
}
