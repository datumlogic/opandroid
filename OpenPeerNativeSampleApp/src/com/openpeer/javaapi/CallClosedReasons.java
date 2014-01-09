package com.openpeer.javaapi;


public enum CallClosedReasons {

	CallClosedReason_None(0),

	CallClosedReason_User(200),

	CallClosedReason_RequestTimeout(408),
	CallClosedReason_TemporarilyUnavailable(480),
	CallClosedReason_Busy(486),
	CallClosedReason_RequestTerminated(487),
	CallClosedReason_NotAcceptableHere(488),

	CallClosedReason_ServerInternalError(500),

	CallClosedReason_Decline(603);
	
	CallClosedReasons (int value)
    {
        this.type = value;
    }

    private int type;

    public int getNumericType()
    {
        return type;
    }
}
