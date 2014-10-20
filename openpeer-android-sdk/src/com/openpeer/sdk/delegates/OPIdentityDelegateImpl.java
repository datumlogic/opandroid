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
package com.openpeer.sdk.delegates;

import java.util.Hashtable;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.openpeer.javaapi.AccountStates;
import com.openpeer.javaapi.IdentityStates;
import com.openpeer.javaapi.OPIdentity;
import com.openpeer.javaapi.OPIdentityDelegate;
import com.openpeer.javaapi.OPLogLevel;
import com.openpeer.javaapi.OPLogger;
import com.openpeer.sdk.app.IntentData;
import com.openpeer.sdk.app.LoginUIListener;
import com.openpeer.sdk.app.OPDataManager;
import com.openpeer.sdk.app.OPHelper;
import com.openpeer.sdk.app.OPIdentityLoginWebview;
import com.openpeer.sdk.app.OPSdkConfig;

public class OPIdentityDelegateImpl extends OPIdentityDelegate {
    OPIdentityLoginWebview mLoginView;
    LoginUIListener mListener;

    private static Hashtable<Long, OPIdentityDelegateImpl> instances = new Hashtable<Long, OPIdentityDelegateImpl>();

    public static OPIdentityDelegateImpl getInstance(OPIdentity identity) {
        Long id = 0L;
        if (identity != null) {
            id = identity.getID();
        }
        OPIdentityDelegateImpl instance = instances.get(id);
        if (instance == null) {
            instance = new OPIdentityDelegateImpl();
            instances.put(id, instance);
        }
        return instance;
    }

    public void bindListener(LoginUIListener listener) {
        mListener = listener;
    }

    public void associateIdentity(OPIdentity identity) {
        instances.remove(0L);
        instances.put(identity.getID(), this);
    }

    public void setWebview(OPIdentityLoginWebview webview) {
        this.mLoginView = webview;

    }

    private OPIdentityDelegateImpl() {
    }

    @Override
    public void onIdentityStateChanged(OPIdentity identity, IdentityStates state) {
        // TODO Auto-generated method stub
        Log.d("state", "identity state " + state);
        // why isn' this working? Weird!!
        // mLoginView = mListener.getIdentityWebview(identity);
        // mLoginView.getClient().setIdentity(identity);
        switch (state) {
        case IdentityState_WaitingForBrowserWindowToBeLoaded:

            Log.d("login", "loading identity webview");
            mListener.onStartIdentityLogin();
            mLoginView
                    .loadUrl(OPSdkConfig.getInstance().getOuterFrameUrl());

            break;
        case IdentityState_WaitingForBrowserWindowToBeMadeVisible:

            mListener.onIdentityLoginWebViewMadeVisible();

            identity.notifyBrowserWindowVisible();
            break;
        case IdentityState_WaitingForBrowserWindowToClose:

            mListener.onIdentityLoginWebViewClose();

            identity.notifyBrowserWindowClosed();
            break;
        case IdentityState_Ready:
            if (OPDataManager.getInstance().getSharedAccount().getState() == AccountStates.AccountState_Ready) {
                // OPDataManager.getInstance().setIdentities(OPDataManager.getInstance().getSharedAccount().getAssociatedIdentities());
                if (mListener != null) {
                    mListener.onLoginComplete();
                    mListener = null;
                    mLoginView = null;
                }

            }
            String version = OPDataManager
                    .getDatastoreDelegate()
                    .getDownloadedContactsVersion(identity.getIdentityURI());
            if (TextUtils.isEmpty(version)) {
                OPLogger.debug(OPLogLevel.LogLevel_Detail,
                        "start download initial contacts");
                identity.startRolodexDownload("");
            } else {
                // check for new contacts
                OPLogger.debug(OPLogLevel.LogLevel_Detail,
                        "start download initial contacts");
                identity.startRolodexDownload(version);
            }
            break;
        case IdentityState_Shutdown:
            // Temporary defensive code. Proper logic will be put in place soon.
            if (mListener != null) {
                mListener.onLoginError();
                mListener = null;
                mLoginView = null;

            }
            Intent intent = new Intent(IntentData.ACTION_IDENTITY_SHUTDOWN);
            intent.putExtra(IntentData.PARAM_IDENTITY_URI,
                    identity.getIdentityURI());
            OPHelper.getInstance().sendBroadcast(intent);
            break;
        }
    }

    @Override
    public void onIdentityPendingMessageForInnerBrowserWindowFrame(
            OPIdentity identity) {
        // TODO Auto-generated method stub
        String msg = identity.getNextMessageForInnerBrowerWindowFrame();
        Log.d("login", "identity pendingMessageForInnerFrame " + msg);

        passMessageToJS(msg);
    }

    @Override
    public void onIdentityRolodexContactsDownloaded(OPIdentity identity) {
        OPDataManager.getInstance().onDownloadedRolodexContacts(identity);
    }

    public void passMessageToJS(final String msg) {

        String cmd = String.format("javascript:sendBundleToJS(\'%s\')", msg);
        Log.w("login", "Identity webview Pass to JS: " + cmd);
        mLoginView.loadUrl(cmd);

    }
}
