/*
 
 Copyright (c) 2014, SMB Phone Inc.
 All rights reserved.
 
 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:
 
 1. Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution.
 
 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 
 The views and conclusions contained in the software and documentation are those
 of the authors and should not be interpreted as representing official policies,
 either expressed or implied, of the FreeBSD Project.
 
 */

package com.openpeer.javaapi;

import android.util.Log;

public class OPStackMessageQueue {

	private long nativeClassPointer;
	
	private long nativeDelegatePointer;
	
	public static native OPStackMessageQueue singleton();

    //-----------------------------------------------------------------------
    // PURPOSE: Intercept the processing of event messages from within the
    //          default message queue so they can be processed/executed from
    //          within the context of a custom thread.
    // NOTE:    Can only be called once. Once override, everytime
    //          "IStackMessageQueueDelegate::onStackMessageQueueWakeUpCustomThreadAndProcess"
    //          is called on the delegate the delegate must wake up the main
    //          thread then call "IStackMessageQueue::notifyProcessMessageFromCustomThread"
    //          from the main thread.
    //
    //          MUST be called BEFORE called "IStack::setup"
    public native void interceptProcessing(OPStackMessageQueueDelegate delegate);

    //-----------------------------------------------------------------------
    // PURPOSE: Notify that a message can be processed from the custom thread.
    // NOTE:    Only call this routine from within the context of running from
    //          the custom thread.
    public native void notifyProcessMessageFromCustomThread();
    
    private native void releaseCoreObjects(); 
    
    protected void finalize() throws Throwable {
    	
    	if (nativeClassPointer != 0 || nativeDelegatePointer != 0)
    	{
    		Log.d("output", "Cleaning stack message queue core objects");
    		releaseCoreObjects();
    	}
    		
    	super.finalize();
    }
}
