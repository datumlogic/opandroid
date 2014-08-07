package com.openpeer.javaapi;

import android.util.Log;

public class OPConversationThreadComposingStatus {

	private long nativeClassPointer;

    public static native String toString(ComposingStates state);
    public static native ComposingStates toComposingState(String state);

    //-----------------------------------------------------------------------
    // PURPOSE: Creates or updates a "contactStatus" in conversation thread
    //          JSON information blob.
    // RETURNS: ioContactStatusInThreadEl- the existing contact status JSON
    //                                     blob is edited in place and/or a
    //                                     new JSON blob is created
    // NOTES:   If ioContactStatusInThreadEl is empty and the state passed
    //          in is "ComposingState_None" or "ComposingState_Inactive" the
    //          ioContactStatusInThreadEl will continue to be set to
    //          ElementPtr() without a new JSON blob being constructed.
    public static native void updateComposingStatus(
                                      String ioContactStatusInThreadEl, // value can start as ElementPtr() and will be automatically filled in
                                      ComposingStates composing              // the new composing state
                                      );

    //-----------------------------------------------------------------------
    // PURPOSE: Given a "contactStatus" in conversation thread JSON
    //          information blob, extract out the composing state (if any)
    // RETURNS: "ComposingState_None" when there is no composing state or the
    //          current composing state.
    public static native ComposingStates getComposingStatus(String contactStatusInThreadEl);
	
	private native void releaseCoreObjects(); 

	protected void finalize() throws Throwable {

		if (nativeClassPointer != 0)
		{
			Log.d("output", "Cleaning conversation thread composing status core objects");
			releaseCoreObjects();
		}

		super.finalize();
	}
}
