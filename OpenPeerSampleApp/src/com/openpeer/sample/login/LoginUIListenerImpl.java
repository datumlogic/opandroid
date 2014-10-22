/**
 * Copyright (c) 2014, SMB Phone Inc. / Hookflash Inc.
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
package com.openpeer.sample.login;

import java.util.Hashtable;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.openpeer.javaapi.OPIdentity;
import com.openpeer.sample.R;
import com.openpeer.sample.push.HackApiService;
import com.openpeer.sample.push.OPPushManager;
import com.openpeer.sdk.app.LoginUIListener;
import com.openpeer.sdk.app.OPDataManager;
import com.openpeer.sdk.app.OPIdentityLoginWebViewClient;
import com.openpeer.sdk.app.OPIdentityLoginWebview;
import com.urbanairship.push.PushManager;

/**
 *
 */
public class LoginUIListenerImpl implements LoginUIListener {
    Activity mActivity;
    WebView mAccountLoginWebView;
    ViewGroup rootView;

    View progressView;
    Hashtable<Long, OPIdentityLoginWebview> mIdentityWebviews = new Hashtable<Long, OPIdentityLoginWebview>();

    public LoginUIListenerImpl(Activity activity) {
        mActivity = activity;
        rootView = (ViewGroup) activity.getWindow().getDecorView();
    }

    void showProgressView() {
        if (progressView == null) {
            progressView = new ProgressBar(mActivity);
            rootView.addView(progressView, new LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        }
    }

    public OPIdentityLoginWebview getIdentityWebview(OPIdentity identity) {
        OPIdentityLoginWebview view = mIdentityWebviews.get(identity.getID());
        if (view == null) {
            view = new OPIdentityLoginWebview(mActivity);
            OPIdentityLoginWebViewClient client= new OPIdentityLoginWebViewClient(identity);
            view.setClient(client);
            setupWebView(view);
            rootView.addView(view, new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT));
        }
        return view;
    }

    public WebView getAccountWebview() {
        if (mAccountLoginWebView == null) {
            mAccountLoginWebView = new WebView(mActivity);
            setupWebView(mAccountLoginWebView);
            rootView.addView(mAccountLoginWebView, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }
        return mAccountLoginWebView;
    }

    void hideProgressView() {
        if (progressView != null) {
            progressView = new ProgressBar(mActivity);
            rootView.removeView(progressView);
        }
    }

    /* START implementation of LoginUIListener */
    @Override
    public void onStartIdentityLogin(OPIdentity identity) {
        hideProgressView();
        getIdentityWebview(identity).bringToFront();
    }

    @Override
    public void onAccountLoginComplete() {
        rootView.removeView(mAccountLoginWebView);

        PushManager.enablePush();

        // TODO: move it to proper place after login refactoring.
        String apid = PushManager.shared().getAPID();
        if (!TextUtils.isEmpty(apid)) {
            OPPushManager.getInstance()
                    .associateDeviceToken(
                            OPDataManager.getInstance().getSharedAccount()
                                    .getPeerUri(),
                            PushManager.shared().getAPID(),
                            new Callback<HackApiService.HackAssociateResult>() {
                                @Override
                                public void success(
                                        HackApiService.HackAssociateResult hackAssociateResult,
                                        Response response) {

                                }

                                @Override
                                public void failure(RetrofitError error) {
                                }
                            }
                    );
        }
    }

    @Override
    public void onLoginError() {
        if (!mActivity.hasWindowFocus() && mAccountLoginWebView != null) {
            rootView.removeView(mAccountLoginWebView);

            Toast.makeText(mActivity, R.string.msg_failed_login,
                    Toast.LENGTH_LONG).show();

            // ((BaseFragmentActivity) getActivity()).hideLoginFragment();
        }
    }

    @Override
    public void onIdentityLoginWebViewMadeVisible(OPIdentity identity) {
        OPIdentityLoginWebview view = getIdentityWebview(identity);
        view.setVisibility(View.VISIBLE);
        view.bringToFront();
    }

    @Override
    public void onAccountLoginWebViewMadeVisible() {
        hideProgressView();
        if (mAccountLoginWebView == null) {
            mAccountLoginWebView = new WebView(mActivity);
            setupWebView(mAccountLoginWebView);
            rootView.addView(mAccountLoginWebView, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        } else {
            rootView.bringToFront();
        }
    }

    @Override
    public void onIdentityLoginWebViewClose(OPIdentity identity) {
        OPIdentityLoginWebview view = getIdentityWebview(identity);
        if (view != null) {
            view.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAccountLoginWebViewMadeClose() {
        mAccountLoginWebView.setVisibility(View.GONE);
    }

    /* END implementation of LoginUIListener */

    void setupWebView(WebView view) {
        WebSettings webSettings = view.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

    }
}
