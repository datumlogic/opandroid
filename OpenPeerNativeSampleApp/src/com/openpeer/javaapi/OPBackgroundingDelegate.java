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
