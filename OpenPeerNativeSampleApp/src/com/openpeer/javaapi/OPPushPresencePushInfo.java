package com.openpeer.javaapi;

public class OPPushPresencePushInfo {

	private String mServiceType;  // e.g. "apns", "gcm", or all
	private OPElement mValues;   // "values" data associated with push messages (use "getValues(...)" to extract data
	private OPElement mCustom;   // extended push related custom push data

	public String getServiceType() {
		return mServiceType;
	}
	public void setServiceType(String mServiceType) {
		this.mServiceType = mServiceType;
	}
	public OPElement getValues() {
		return mValues;
	}
	public void setValues(OPElement mValues) {
		this.mValues = mValues;
	}
	public OPElement getCustom() {
		return mCustom;
	}
	public void setCustom(OPElement mCustom) {
		this.mCustom = mCustom;
	} 
}
