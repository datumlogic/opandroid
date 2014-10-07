package com.openpeer.javaapi;

public class OPPushStateContactDetail {
    private OPContact mRemotePeer;

    private int mErrorCode;
    private String mErrorReason;
	
    public OPContact getRemotePeer() {
		return mRemotePeer;
	}
	public void setRemotePeer(OPContact mRemotePeer) {
		this.mRemotePeer = mRemotePeer;
	}
	public int getErrorCode() {
		return mErrorCode;
	}
	public void setErrorCode(int mErrorCode) {
		this.mErrorCode = mErrorCode;
	}
	public String getErrorReason() {
		return mErrorReason;
	}
	public void setErrorReason(String mErrorReason) {
		this.mErrorReason = mErrorReason;
	}
}
