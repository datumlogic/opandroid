package com.openpeer.sdk.model;

import java.util.List;

public class ParticipantInfo {
    long cbcId;
    List<OPUser> user;

    public ParticipantInfo(){}
    public ParticipantInfo(long cbcId, List<OPUser> user) {
        this.cbcId = cbcId;
        this.user = user;
    }

    public long getCbcId() {
        return cbcId;
    }

    public void setCbcId(long cbcId) {
        this.cbcId = cbcId;
    }

    public List<OPUser> getParticipants() {
        return user;
    }

    public void setUsers(List<OPUser> user) {
        this.user = user;
    }
}
