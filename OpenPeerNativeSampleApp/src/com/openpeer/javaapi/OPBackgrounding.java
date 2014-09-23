package com.openpeer.javaapi;

public class OPBackgrounding {

    //-----------------------------------------------------------------------
    // PURPOSE: returns a debug element containing internal object state
    public static native String toDebug();

    //-----------------------------------------------------------------------
    // PURPOSE: Notifies the application is about to go into the background
    // PARAMS:  readyDelegate - pass in a delegate which will get a callback
    //                          when all backgrounding subscribers are ready
    //                          to go into the background
    // RETURNS: a query interface about the current backgrounding state
    public static native  OPBackgroundingQuery notifyGoingToBackground(OPBackgroundingCompletionDelegate readyDelegate);

    //-----------------------------------------------------------------------
    // PURPOSE: Notifies the application is goinging to the background
    //          immediately
    public static native  void notifyGoingToBackgroundNow();

    //-----------------------------------------------------------------------
    // PURPOSE: Notifies the application is returning from to the background
    public static native  void notifyReturningFromBackground();

    //-----------------------------------------------------------------------
    // PURPOSE: Subscribe to the backgrounding state
    // PARAMS:  phase - Each subscription is assigned a phase number and
    //                  more than one subscriber can share the same phase.
    //                  Phases are done in order (lowest to highest) where
    //                  every subscriber must complete backgrounding before
    //                  the next phase of backgrounding is activiated.
    public static native  OPBackgroundingSubscription subscribe(
                                                   OPBackgroundingDelegate delegate,
                                                   long phase
                                                   );
}
