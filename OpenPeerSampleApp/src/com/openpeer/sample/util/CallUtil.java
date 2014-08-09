package com.openpeer.sample.util;

import com.openpeer.javaapi.CallStates;
import com.openpeer.sample.OPApplication;
import com.openpeer.sample.R;

/**
 * Copyright (c) 2013, SMB Phone Inc. / Hookflash Inc.
 * All rights reserved.
 * <p/>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * <p/>
 * 1. Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * <p/>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * <p/>
 * The views and conclusions contained in the software and documentation are those
 * of the authors and should not be interpreted as representing official policies,
 * either expressed or implied, of the FreeBSD Project.
 */
public class CallUtil {
    final static int STATE_STRINGS[] = {0,// CallState_None, // call has no state yet
            R.string.CallState_Preparing,// CallState_Preparing, // call is negotiating in the background - do not present this call to a user yet...
            R.string.CallState_Incoming, // call is incoming from a remote party
            R.string.CallState_Placed, // call has been placed to the remote party
            0,// CallState_Early, // call is outgoing to a remote party and is receiving early media (media before being answered)
            R.string.CallState_Ringing,// CallState_Ringing, // call is incoming from a remote party and is ringing
            R.string.CallState_Ringback, // call is outgoing to a remote party and remote party is ringing
            R.string.CallState_Open,// R.string.CallState_Open, // call is open
            R.string.CallState_Active, // call is open, and participant is actively communicating
            R.string.CallState_Inactive, // call is open, and participant is inactive
            R.string.CallState_Hold, // call is open but on hold
            R.string.CallState_Closing, // call is hanging up
            R.string.CallState_Closed}; // call has ended};

    public static String getCallStateStringResId(CallStates state) {
        int resId = STATE_STRINGS[state.ordinal()];
        if (resId != 0) {
            return OPApplication.getInstance().getString(resId);
        }
        return null;
    }
}
