package com.openpeer.sample.delegates;

import android.util.Log;

import com.openpeer.javaapi.IdentityStates;
import com.openpeer.javaapi.OPIdentity;
import com.openpeer.javaapi.OPIdentityDelegate;
import com.openpeer.sample.login.LoginManager;

public class OPIdentityDelegateImplementation extends OPIdentityDelegate {
    private static String TAG = "login";

    @Override
    public void onIdentityStateChanged(OPIdentity identity, IdentityStates state) {
        // TODO Auto-generated method stub
        switch (state) {
            case IdentityState_WaitingForBrowserWindowToBeLoaded:
                Log.d(TAG, "onIdentityStateChanged " + "IdentityState_WaitingForBrowserWindowToBeLoaded");
                LoginManager.loadOuterFrame();
                break;
            case IdentityState_WaitingForBrowserWindowToBeMadeVisible:
                Log.d(TAG, "onIdentityStateChanged " + "IdentityState_WaitingForBrowserWindowToBeMadeVisible");

                LoginManager.mIdentity.notifyBrowserWindowVisible();
                break;
            case IdentityState_WaitingForBrowserWindowToClose:
                Log.d(TAG, "onIdentityStateChanged " + "IdentityState_WaitingForBrowserWindowToClose");

                LoginManager.mIdentity.notifyBrowserWindowClosed();
                break;
            case IdentityState_Ready:
                Log.d(TAG, "onIdentityStateChanged " + "IdentityState_Ready");
//                Log.d(TAG, "onIdentityStateChanged " + identity);
                //LoginManager.mIdentity.;
                break;
        }

    }

    @Override
    public void onIdentityPendingMessageForInnerBrowserWindowFrame(
            OPIdentity identity) {
        // TODO Auto-generated method stub
        LoginManager.pendingMessageForInnerFrame();

    }

    @Override
    public void onIdentityRolodexContactsDownloaded(OPIdentity identity) {
        // TODO Auto-generated method stub
        LoginManager.onDownloadedRolodexContacts(identity);


    }

}
