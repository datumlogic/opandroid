package com.openpeer.model;

public class OPHomeUser {
	private String reloginInfo;
	private long stableId;

	public OPHomeUser(String reloginInfo, long stableId) {
		super();
		this.reloginInfo = reloginInfo;
		this.stableId = stableId;
	}

	public String getReloginInfo() {
		return reloginInfo;
	}

	public void setReloginInfo(String reloginInfo) {
		this.reloginInfo = reloginInfo;
	}

	public long getStableId() {
		return stableId;
	}

	public void setStableId(long stableId) {
		this.stableId = stableId;
	}

}
