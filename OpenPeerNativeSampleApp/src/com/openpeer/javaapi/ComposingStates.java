package com.openpeer.javaapi;

public enum ComposingStates {
    ComposingState_None,      // contact has no composing status

    ComposingState_Inactive,  // contact is not actively participating in conversation (assumed default if "none")
    ComposingState_Active,    // contact is active in the conversation
    ComposingState_Gone,      // contact is effectively gone from conversation
    ComposingState_Composing, // contact is composing a message
    ComposingState_Paused;    // contact was composing a message but is no longer composing
}
