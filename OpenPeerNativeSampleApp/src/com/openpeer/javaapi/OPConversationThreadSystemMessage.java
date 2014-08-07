package com.openpeer.javaapi;

import android.util.Log;

public class OPConversationThreadSystemMessage {

	public static native String toString(SystemMessageTypes type);
	public static native  SystemMessageTypes toSystemMessageType(String type);

	//-----------------------------------------------------------------------
	// PURPOSE: Get the system message type
	// RETURNS: The system message mime type
	// NOTES:   All system messages use the same mime type
	public static native String getMessageType();

	//-----------------------------------------------------------------------
	// PURPOSE: Creates a system message related to the "call" system message
	//          types
	// RETURNS: JSON element to send as a message
	// NOTES:   Only the caller would create these kind of messages and
	//          send into the conversation thread.
	public static native String createCallMessage(
			SystemMessageTypes type,
			OPContact callee,
			int errorCode       // optional HTTP style error code (can cast as WORD from ICall::CallClosedReasons)
			);

	//-----------------------------------------------------------------------
	// PURPOSE: Helper routine to send the system message to the conversation
	//          thread.
	// NOTES:   Wraps the IConversationThread::sendMessage routine for
	//          sending convenience. Converts the JSON to a string and then
	//          sends the message using the system message type.
	public static native void sendMessage(
			OPConversationThread inRelatedConversationThread,
			String messageID,
			String systemMessage,
			boolean signMessage
			);

	//-----------------------------------------------------------------------
	// PURPOSE: Given a message and message type, attempt to parse as a
	//          system message.
	// RETURNS: SystemMessageType_NA - if message is not a system message
	//          SystemMessageType_Unknown - if message is a system message
	//                                      but is not understood
	//          outSystemMessage - JSON structure containing system message
	//                             information that needs parsing
	public static native OPParsedSystemMessage parseAsSystemMessage(
			String inMessage,
			String inMessageType
			);

	//-----------------------------------------------------------------------
	// PURPOSE: Given a JSON system message extract the call information
	// RETURNS: outCallee - the callee contact
	//          outErrorCode - HTTP style error code
	public static native OPCallMessageInfo getCallMessageInfo(
			OPConversationThread inRelatedConversationThread,
			String inSystemMessage
			);
}
