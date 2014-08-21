package com.openpeer.javaapi;

public class OPSystemMessage {

    //-----------------------------------------------------------------------
    // PURPOSE: creates an empty system message that can be filled with data
    public static native String createEmptySystemMessage();

    //-----------------------------------------------------------------------
    // PURPOSE: get the "messageType" to pass into
    //          "IConversationThread::sendMessage(...)"
    public static native String getMessageType();
}
