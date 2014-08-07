package com.openpeer.javaapi;

public enum SystemMessageTypes {
    SystemMessageType_NA,           // not a system message
    SystemMessageType_Unknown,      // unknown system message type

    SystemMessageType_CallPlaced,   // call was placed
    SystemMessageType_CallAnswered, // call was answered
    SystemMessageType_CallHungup;   // call was hung-up
}
