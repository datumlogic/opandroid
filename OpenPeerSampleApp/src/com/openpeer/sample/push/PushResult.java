package com.openpeer.sample.push;

import java.util.Arrays;

public class PushResult {
    boolean ok;
    String operation_id;
    String push_ids[];

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public String getOperationId() {
        return operation_id;
    }

    public void setOperationId(String operation_id) {
        this.operation_id = operation_id;
    }

    public String[] getPushIds() {
        return push_ids;
    }

    public void setPushIds(String[] push_ids) {
        this.push_ids = push_ids;
    }

    public String toString() {
        return "ok " + ok + " OperationId " + operation_id + " push_ids " + Arrays.deepToString(push_ids);
    }
}
