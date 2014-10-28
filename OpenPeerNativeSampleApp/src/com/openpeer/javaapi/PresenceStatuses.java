package com.openpeer.javaapi;

public enum PresenceStatuses {

	PresenceStatus_None,

    PresenceStatus_Busy,        // user is busy and wishes not to be contacted at this time
    PresenceStatus_Away,        // user is not available
    PresenceStatus_Idle,        // user is around but not active
    PresenceStatus_Available   // user is available
}
