package com.openpeer.javaapi;

public enum CallSystemMessageTypes {
    CallSystemMessageType_None,     // not a call system message

    CallSystemMessageType_Placed,   // call was placed
    CallSystemMessageType_Answered, // call was answered
    CallSystemMessageType_Hungup;   // call was hung-up
}
