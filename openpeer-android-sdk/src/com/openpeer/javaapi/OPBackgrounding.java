/*******************************************************************************
 *
 *  Copyright (c) 2014 , Hookflash Inc.
 *  All rights reserved.
 *  
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *  
 *  1. Redistributions of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 *  
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 *  ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *  
 *  The views and conclusions contained in the software and documentation are those
 *  of the authors and should not be interpreted as representing official policies,
 *  either expressed or implied, of the FreeBSD Project.
 *******************************************************************************/
package com.openpeer.javaapi;

public class OPBackgrounding {

    public static native String toDebug();

    /**
     * Notifies the application is about to go into the background PARAMS: readyDelegate - pass in a delegate which will get a callback when
     * all backgrounding subscribers are ready to go into the background
     * 
     * @param readyDelegate
     *            Call back to handle the completion of backgrounding
     * @return a query interface about the current backgrounding state
     */

    public static native OPBackgroundingQuery notifyGoingToBackground(OPBackgroundingCompletionDelegate readyDelegate);

    /**
     * Notifies the application is goinging to the background immediately. Call this function when the app is being shutdown or the device
     * is being shutdown
     */
    public static native void notifyGoingToBackgroundNow();

    /**
     * Notifies the application is returning from to the background
     */
    public static native void notifyReturningFromBackground();

    /**
     * Subscribe to the backgrounding state PARAMS: phase - Each subscription is assigned a phase number and more than one subscriber can
     * share the same phase. Phases are done in order (lowest to highest) where every subscriber must complete backgrounding before the next
     * phase of backgrounding is activiated.
     * 
     * @param delegate
     * @param phase
     * @return
     */

    public static native OPBackgroundingSubscription subscribe(
            OPBackgroundingDelegate delegate,
            long phase
            );
}
