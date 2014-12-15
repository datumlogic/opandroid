package com.openpeer.sdk.model;

import java.util.List;

public class ParticipantInfo {
    long cbcId;
    List<OPUser> particpants;

    public ParticipantInfo() {
    }

    public ParticipantInfo(long cbcId, List<OPUser> user) {
        this.cbcId = cbcId;
        this.particpants = user;
    }

    public long getCbcId() {
        return cbcId;
    }

    public void setCbcId(long cbcId) {
        this.cbcId = cbcId;
    }

    public List<OPUser> getParticipants() {
        return particpants;
    }

    public void setUsers(List<OPUser> user) {
        this.particpants = user;
    }

    public void addUsers(List<OPUser> users) {
        particpants.addAll(users);
    }

    public void removeUsers(List<OPUser> users) {
        particpants.removeAll(users);
    }
}
