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
package com.openpeer.sdk.delegates;

import java.util.Hashtable;

import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;

import com.openpeer.javaapi.AccountStates;
import com.openpeer.javaapi.IdentityStates;
import com.openpeer.javaapi.OPIdentity;
import com.openpeer.javaapi.OPIdentityDelegate;
import com.openpeer.sdk.app.LoginUIListener;
import com.openpeer.sdk.app.OPDataManager;
import com.openpeer.sdk.app.OPIdentityLoginWebview;

public class OPIdentityDelegateImplementation extends OPIdentityDelegate {
	OPIdentityLoginWebview mLoginView;
	LoginUIListener mListener;

	static Hashtable<Long, OPIdentityDelegateImplementation> instances = new Hashtable<Long, OPIdentityDelegateImplementation>();

	public static OPIdentityDelegateImplementation getInstance(OPIdentity identity) {
		Long id = 0L;
		if (identity != null) {
			id = identity.getID();
		}
		OPIdentityDelegateImplementation instance = instances.get(id);
		if (instance == null) {
			instance = new OPIdentityDelegateImplementation();
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

	public OPIdentityDelegateImplementation() {
	}

	@Override
	public void onIdentityStateChanged(OPIdentity identity, IdentityStates state) {
		// TODO Auto-generated method stub
		Log.d("state", "identity state " + state);
		// why isn' this working? Weird!!
//		mLoginView = mListener.getIdentityWebview(identity);
//		mLoginView.getClient().setIdentity(identity);
		switch (state) {
		case IdentityState_WaitingForBrowserWindowToBeLoaded:
			// LoginManager.loadOuterFrame();//load identity.html
			mLoginView.post(new Runnable() {
				public void run() {
					Log.d("login", "loading identity webview");
					mListener.onStartIdentityLogin();
					mLoginView.loadUrl("http://jsouter-v1-rel-lespaul-i.hcs.io/identity.html?view=choose&federated=false");
				}
			});
			break;
		case IdentityState_WaitingForBrowserWindowToBeMadeVisible:
			mLoginView.post(new Runnable() {
				public void run() {
					mListener.onIdentityLoginWebViewMadeVisible();
				}
			});
			identity.notifyBrowserWindowVisible();
			break;
		case IdentityState_WaitingForBrowserWindowToClose:
			mLoginView.post(new Runnable() {
				public void run() {
					mListener.onIdentityLoginWebViewClose();
				}
			});
			identity.notifyBrowserWindowClosed();
			break;
		case IdentityState_Ready:
			// LoginManager.mIdentity.;
			if (OPDataManager.getInstance().getSharedAccount().getState(0, "") == AccountStates.AccountState_Ready) {
				OPDataManager.getInstance().setIdentities(OPDataManager.getInstance().getSharedAccount().getAssociatedIdentities());

				// mListener.onLoginComplete();

				String version = OPDataManager.getDatastoreDelegate().getDownloadedContactsVersion(identity.getStableID());
				if (TextUtils.isEmpty(version)) {
					Log.d("login", "start download initial contacts");
					identity.startRolodexDownload("");
				} else {
					// check for new contacts
					Log.d("login", "start download  contacts since version " + version);
					identity.startRolodexDownload(version);
				}

			}
			break;
		case IdentityState_Shutdown:
			// Temporary defensive code. Proper logic will be put in place soon.
			if (mListener != null) {
				mLoginView.post(new Runnable() {
					public void run() {
						mListener.onLoginError();
						mLoginView = null;
					}
				});
			}
			break;
		}
	}

	@Override
	public void onIdentityPendingMessageForInnerBrowserWindowFrame(OPIdentity identity) {
		// TODO Auto-generated method stub
		String msg = identity.getNextMessageForInnerBrowerWindowFrame();
		Log.d("login", "identity pendingMessageForInnerFrame " + msg);

		passMessageToJS(msg);
	}

	@Override
	public void onIdentityRolodexContactsDownloaded(OPIdentity identity) {
		OPDataManager.getInstance().onDownloadedRolodexContacts(identity);
		mListener.onLoginComplete();
		// destroy();
	}

	public void passMessageToJS(final String msg) {
		mLoginView.post(new Runnable() {
			public void run() {
				String cmd = String.format("javascript:sendBundleToJS(\'%s\')", msg);
				Log.w("login", "Identity webview Pass to JS: " + cmd);
				mLoginView.loadUrl(cmd);
			}
		});
	}
}
