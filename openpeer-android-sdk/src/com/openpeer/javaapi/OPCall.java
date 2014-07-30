package com.openpeer.javaapi;

import com.openpeer.sdk.model.OPUser;
import com.openpeer.sdk.utils.OPModelUtils;

import android.text.format.Time;

public class OPCall {
	public OPCall() {
	}

	public OPCall(long nativeClassPtr) {
		this.nativeClassPointer = nativeClassPtr;
	}

	private long nativeClassPointer;
	private OPUser mUser;

	public static native String toString(CallStates state);

	public static native String toString(CallClosedReasons reason);

	public static native String toDebugString(OPCall call, boolean includeCommaPrefix);

	public static native OPCall placeCall(OPConversationThread conversationThread, OPContact toContact, boolean includeAudio,
			boolean includeVideo);

	public native long getStableID();

	public native String getCallID();

	public native OPConversationThread getConversationThread();

	public native OPContact getCaller();

	public native OPContact getCallee();

	public native boolean hasAudio();

	public native boolean hasVideo();

	public native CallStates getState(); // state is always relative to "this" location, be is caller or callee

	public native CallClosedReasons getClosedReason();

	public native Time getCreationTime();

	public native Time getRingTime();

	public native Time getAnswerTime();

	public native Time getClosedTime();

	public native void ring(); // tell the caller that the call is ringing

	public native void answer(); // answer the call

	public native void hold(boolean hold); // place the call on hold (or remove from hold)

	public native void hangup(CallClosedReasons reason); // end the call
	// End of JNI

	public OPContact getPeer() {
		OPContact peer = getCaller();
		if (peer.isSelf()) {
			return getCallee();
		} else {
			return peer;
		}
	}

	public OPUser getPeerUser() {
		if (mUser == null) {
			OPContact contact = getPeer();
			return new OPUser(contact, getConversationThread().getIdentityContactList(contact));
		}
		return mUser;
	}

	public void setPeerUser(OPUser user) {
		this.mUser = user;
	}

	public Long getNativeClsPtr() {
		// TODO Auto-generated method stub
		return nativeClassPointer;
	}
    private native void releaseCoreObjects(); 

    protected void finalize() throws Throwable {
    	
    	if (nativeClassPointer != 0)
    	{
    		releaseCoreObjects();
    	}
    		
    	super.finalize();
    }

}
