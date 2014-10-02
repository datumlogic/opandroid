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
package com.openpeer.sdk.app;

import java.util.List;

import android.text.TextUtils;
import android.util.Log;

import com.openpeer.javaapi.OPAccount;
import com.openpeer.javaapi.OPCallDelegate;
import com.openpeer.javaapi.OPConversationThreadDelegate;
import com.openpeer.javaapi.OPIdentity;
import com.openpeer.javaapi.OPLogLevel;
import com.openpeer.javaapi.OPLogger;
import com.openpeer.sdk.delegates.OPAccountDelegateImpl;
import com.openpeer.sdk.delegates.OPIdentityDelegateImpl;

public class LoginManager {
	private static LoginUIListener mListener;

	private static LoginManager instance;

	public static LoginManager getInstance() {
		if (instance == null) {
			instance = new LoginManager();
		}
		return instance;
	}

	private LoginManager() {
	}

	/**
	 * 
	 * @param listener
	 *            Application need to pass the UI listener to handle state changes.
	 * @param callDelegate
	 *            Global call delegate implementation. The object MUST be kept valid throughout the app lifecycle.
	 * @param conversationThreadDelegate
	 *            Global conversation thread delegate implementation. The object MUST be kept valid throughout the app lifecycle.
	 */
	public void login(LoginUIListener listener,
			OPCallDelegate callDelegate,
			OPConversationThreadDelegate conversationThreadDelegate) {
		mListener = listener;

		OPAccountDelegateImpl accountDelegate = OPAccountDelegateImpl.getInstance();
		accountDelegate.bind(mListener);
		OPAccount account = OPAccount.login(accountDelegate, conversationThreadDelegate, callDelegate);
		OPDataManager.getInstance().setSharedAccount(account);
		startIdentityLogin();
	}

	/**
	 * 
	 * @param listener
	 *            Application need to pass the UI listener to handle state changes.
	 * @param callDelegate
	 *            Global call delegate implementation. The object MUST be kept valid throughout the app lifecycle.
	 * @param conversationThreadDelegate
	 *            Global conversation thread delegate implementation. The object MUST be kept valid throughout the app lifecycle.
	 * 
	 * @param reloginInfo
	 *            relogin jason blob stored from last login session
	 */
	public void relogin(LoginUIListener listener,
			OPCallDelegate callDelegate,
			OPConversationThreadDelegate conversationThreadDelegate,
			String reloginInfo) {
		mListener = listener;
		OPAccountDelegateImpl accountDelegate = OPAccountDelegateImpl.getInstance();
		accountDelegate.bind(mListener);
		OPAccount account = OPAccount.relogin(accountDelegate, conversationThreadDelegate, callDelegate, reloginInfo);

		OPDataManager.getInstance().setSharedAccount(account);
	}

	private void startIdentityLogin() {
		OPAccount account = OPDataManager.getInstance().getSharedAccount();

		OPIdentity identity = new OPIdentity();
		OPIdentityLoginWebview mIdentityLoginWebView = mListener.getIdentityWebview(null);

		OPIdentityDelegateImpl identityDelegate = OPIdentityDelegateImpl.getInstance(null);
		identityDelegate.bindListener(mListener);
		identityDelegate.setWebview(mIdentityLoginWebView);

		identity = OPIdentity.login(account, identityDelegate);
		mIdentityLoginWebView.getClient().setIdentity(identity);
	}

	/**
	 * Handle account state ready. If this is a login, it means the primary identity login has completed, so start downloading contacts. If
	 * this is a relogin, attach Identity delegates to associated identities and start logging in identities
	 * 
	 * @param account
	 */
	public void onAccountStateReady(OPAccount account) {

		OPDataManager.getInstance().saveAccount();

		List<OPIdentity> identities = account.getAssociatedIdentities();
		if (identities.size() == 0) {
			Log.d("TODO", "Account test FAILED identities emppty ");

			return;
		}
		//TODO: test if calling OPIdentity methods work before identity reaches ready state
//        OPDataManager.getInstance().setIdentities(identities);

		for (OPIdentity identity : identities) {
			if (!identity.isDelegateAttached()) {//This is relogin
				OPIdentityLoginWebview webview = mListener.getIdentityWebview(identity);
				webview.getClient().setIdentity(identity);

				OPIdentityDelegateImpl identityDelegate = OPIdentityDelegateImpl.getInstance(identity);// new
																										// OPIdentityDelegateImplementation(mIdentityLoginWebView,
																										// identity);
				identityDelegate.bindListener(mListener);
				identityDelegate.setWebview(webview);
				identity.attachDelegate(identityDelegate, OPSdkConfig.getInstance().getOuterFrameUrl());

			} else {

				String version = OPDataManager.getDatastoreDelegate().getDownloadedContactsVersion(identity.getIdentityURI());
				if (TextUtils.isEmpty(version)) {
					OPLogger.debug(OPLogLevel.LogLevel_Detail, "start download initial contacts");
					identity.startRolodexDownload("");
				} else {
					// check for new contacts
					OPLogger.debug(OPLogLevel.LogLevel_Detail,  "start download  contacts since version " + version);
					identity.startRolodexDownload(version);
				}
			}
		}

	}

	public static void onLoginComplete() {
		instance = null;
	}

	/**
	 * If login in progress
	 * 
	 * @return
	 */
	public static boolean isLogIn() {
		// TODO Auto-generated method stub
		return instance != null;
	}

	/**
	 * Handle account shutdown state change.
	 */
	public static void onAccountShutdown() {
		// release resources
		instance = null;
	}
}
