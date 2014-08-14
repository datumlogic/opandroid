package com.openpeer.javaapi;


public enum CallClosedReasons {

	CallClosedReason_None,

	CallClosedReason_User,

	CallClosedReason_RequestTimeout,
	CallClosedReason_TemporarilyUnavailable,
	CallClosedReason_Busy,
	CallClosedReason_RequestTerminated,
	CallClosedReason_NotAcceptableHere,

	CallClosedReason_ServerInternalError,

	CallClosedReason_Decline;
}
