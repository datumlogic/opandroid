package com.openpeer.javaapi;


import android.text.format.Time;


public class OPCall {

	public static native String toString(CallStates state);
	
	public static native String toString(CallClosedReasons reason);

    public static native String toDebugString(OPCall call, Boolean includeCommaPrefix);

    public static native OPCall placeCall(
                              OPConversationThread conversationThread,
                              OPContact toContact,
                              Boolean includeAudio,
                              Boolean includeVideo
                              );

    public native String getStableID();

    public native String getCallID();

    public native OPConversationThread getConversationThread();

    public native OPContact getCaller();
    
    public native OPContact getCallee();

    public native Boolean hasAudio();
    public native Boolean hasVideo();

    public native CallStates getState();  // state is always relative to "this" location, be is caller or callee
    
    public native CallClosedReasons getClosedReason();

    public native Time getCreationTime();
    
    public native Time getRingTime();
    
    public native Time getAnswerTime();
    
    public native Time getClosedTime();

    public native void ring();         // tell the caller that the call is ringing
    
    public native void answer();        // answer the call
    
    public native void hold(Boolean hold); // place the call on hold (or remove from hold)
    
    public native void hangup(CallClosedReasons reason);        // end the call

}
