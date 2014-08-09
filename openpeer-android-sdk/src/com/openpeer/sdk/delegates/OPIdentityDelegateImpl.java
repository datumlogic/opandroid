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
package com.openpeer.sdk.delegates;

import java.util.Hashtable;

import android.text.TextUtils;
import android.util.Log;

import com.openpeer.javaapi.AccountStates;
import com.openpeer.javaapi.IdentityStates;
import com.openpeer.javaapi.OPIdentity;
import com.openpeer.javaapi.OPIdentityDelegate;
import com.openpeer.sdk.app.LoginManager;
import com.openpeer.sdk.app.LoginUIListener;
import com.openpeer.sdk.app.OPDataManager;
import com.openpeer.sdk.app.OPIdentityLoginWebViewClient;
import com.openpeer.sdk.app.OPIdentityLoginWebview;

public class OPIdentityDelegateImpl extends OPIdentityDelegate {
	protected LoginUIListener mListener;

	static Hashtable<Long, OPIdentityDelegateImpl> instances = new Hashtable<Long, OPIdentityDelegateImpl>();

	public OPIdentityDelegateImpl() {
	}

	public static OPIdentityDelegateImpl getInstance(OPIdentity identity) {
		OPIdentityDelegateImpl instance = instances.get(identity.getID());
		if (instance == null) {
			instance = new OPIdentityDelegateImpl();
			instances.put(identity.getID(), instance);
		}
		return instance;
	}

	public void bindLoginListener(LoginUIListener listener) {
		mListener = listener;
	}

	public void unbindLoginListener() {
		mListener = null;
	}

	public void bind(OPIdentity identity) {
		instances.put(identity.getID(), this);
	}

	public void unbind(OPIdentity identity) {
		instances.remove(identity.getID());
	}

	OPIdentityLoginWebview getWebview(OPIdentity identity) {
		if (mListener != null) {
			 OPIdentityLoginWebview webview = mListener.getIdentityWebview(identity);
			 webview.setClient(new OPIdentityLoginWebViewClient(identity));
			if (webview != null) {
				webview.getClient().setIdentity(identity);
			}
			return webview;
		}
		return null;
	}

	@Override
	public void onIdentityStateChanged(final OPIdentity identity, IdentityStates state) {
		// TODO Auto-generated method stub
		Log.d("state", "identity state " + state);
		// OPIdentityLoginWebview webview=null;

		switch (state) {
		case IdentityState_WaitingForBrowserWindowToBeLoaded:
			// LoginManager.loadOuterFrame();//load identity.html
			getWebview(identity).post(new Runnable() {
				public void run() {
					Log.d("login", "loading identity webview");

					mListener.onStartIdentityLogin();
					getWebview(identity).loadUrl("http://jsouter-v1-rel-lespaul-i.hcs.io/identity.html?view=choose&federated=false");
				}
			});
			break;
		case IdentityState_WaitingForBrowserWindowToBeMadeVisible:
			mListener.getIdentityWebview(identity).post(new Runnable() {
				public void run() {
					mListener.onIdentityLoginWebViewMadeVisible();
				}
			});
			identity.notifyBrowserWindowVisible();
			break;
		case IdentityState_WaitingForBrowserWindowToClose:
			mListener.getIdentityWebview(identity).post(new Runnable() {
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
				mListener.getIdentityWebview(identity).post(new Runnable() {
					public void run() {
						mListener.onLoginError();
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

		passMessageToJS(identity, msg);
	}

	@Override
	public void onIdentityRolodexContactsDownloaded(OPIdentity identity) {
		OPDataManager.getInstance().onDownloadedRolodexContacts(identity);
		mListener.onLoginComplete();
		LoginManager.onLoginComplete();
	}

	public void passMessageToJS(final OPIdentity identity, final String msg) {
		mListener.getIdentityWebview(identity).post(new Runnable() {
			public void run() {
				String cmd = String.format("javascript:sendBundleToJS(\'%s\')", msg);
				Log.w("login", "Identity webview Pass to JS: " + cmd);
				mListener.getIdentityWebview(identity).loadUrl(cmd);
			}
		});
	}
}