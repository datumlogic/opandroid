package com.openpeer.sdk.model;

public class OPHomeUser extends OPUser{
	private String reloginInfo;

	public OPHomeUser(String reloginInfo, String stableId) {
		super();
		this.reloginInfo = reloginInfo;
		this.mLockboxStableId = stableId;
	}

	public String getReloginInfo() {
		return reloginInfo;
	}

	public void setReloginInfo(String reloginInfo) {
		this.reloginInfo = reloginInfo;
	}
}
