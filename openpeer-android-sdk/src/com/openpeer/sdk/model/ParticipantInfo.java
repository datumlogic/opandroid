package com.openpeer.sdk.model;

import java.util.List;

public class ParticipantInfo {
    long cbcId;
    List<OPUser> user;

    public long getCbcId() {
        return cbcId;
    }

    public void setCbcId(long cbcId) {
        this.cbcId = cbcId;
    }

    public List<OPUser> getUser() {
        return user;
    }

    public void setUser(List<OPUser> user) {
        this.user = user;
    }
}
