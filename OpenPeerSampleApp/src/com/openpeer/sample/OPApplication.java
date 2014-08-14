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
package com.openpeer.sample;

import android.app.Application;
import android.util.Log;

import com.openpeer.sample.push.OPPushManager;
import com.openpeer.sample.push.OPPushNotificationBuilder;
import com.openpeer.sample.push.PushIntentReceiver;
import com.openpeer.sample.util.SettingsHelper;
import com.openpeer.sdk.app.OPDataManager;
import com.openpeer.sdk.app.OPHelper;
import com.urbanairship.AirshipConfigOptions;
import com.urbanairship.Logger;
import com.urbanairship.UAirship;
import com.urbanairship.push.PushManager;
public class OPApplication extends Application {
    private static OPApplication instance;
    private boolean mInBackground;

    static {
        try {
            System.loadLibrary("z_shared");
            System.loadLibrary("openpeer");

        } catch (UnsatisfiedLinkError use) {
            use.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        instance = this;

        AirshipConfigOptions options = AirshipConfigOptions.loadDefaultOptions(this);
        UAirship.takeOff(this, options);
        Logger.logLevel = Log.VERBOSE;

        PushManager.shared().setNotificationBuilder(new OPPushNotificationBuilder());
        PushManager.shared().setIntentReceiver(PushIntentReceiver.class);

        OPHelper.getInstance().init(this, null);
//        OPHelper.getInstance().setChatGroupMode(OPHelper.MODE_CONTACTS_BASED);
        OPSessionManager.getInstance().init();
        SettingsHelper.getInstance().initLoggers();
    }

    public static OPApplication getInstance() {
        // TODO Auto-generated method stub
        return instance;
    }

    public boolean isInBackground() {
        // TODO Auto-generated method stub
        return mInBackground;
    }

    public void onEnteringForeground() {
        this.mInBackground = false;

    }

    public void onEnteringBackground() {
        this.mInBackground = true;
    }
    public static void signout(){
        OPDataManager.shutdown();
        OPPushManager.shutdown();
//        OPHelper.shutdown();
    }

}
