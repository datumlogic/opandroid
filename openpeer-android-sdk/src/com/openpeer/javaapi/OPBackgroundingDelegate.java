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

public abstract class OPBackgroundingDelegate {

    //-----------------------------------------------------------------------
    // PURPOSE: This is notification from the system that it's going into the
    //          background. If the subscriber needs some time to do it's work
    //          it can keep a reference to the "notifier" object and only
    //          release the object when the backgrounding is ready.
    public abstract void onBackgroundingGoingToBackground(
                                                  OPBackgroundingSubscription subscription,
                                                  OPBackgroundingNotifier notifier
                                                  );

    //-----------------------------------------------------------------------
    // PURPOSE: This notification tells the subscriber it must abandon its
    //          backgrounding efforts as it must go to the background
    //          immediately.
    public abstract void onBackgroundingGoingToBackgroundNow(
    		OPBackgroundingSubscription subscription
                                                     );

    //-----------------------------------------------------------------------
    // PURPOSE: This notification tells the subscriber it is returning from
    //          the background.
    public abstract void onBackgroundingReturningFromBackground(
    		OPBackgroundingSubscription subscription
                                                        );

    //-----------------------------------------------------------------------
    // PURPOSE: This notification tells the subscriber the application will
    //          quit.
    public abstract void onBackgroundingApplicationWillQuit(
    		OPBackgroundingSubscription subscription
                                                    );
}
