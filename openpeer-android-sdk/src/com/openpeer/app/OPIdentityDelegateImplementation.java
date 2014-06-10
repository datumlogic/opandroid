package com.openpeer.app;

import android.util.Log;
import android.webkit.WebView;

import com.openpeer.javaapi.IdentityStates;
import com.openpeer.javaapi.OPIdentity;
import com.openpeer.javaapi.OPIdentityDelegate;

public class OPIdentityDelegateImplementation extends OPIdentityDelegate {
	WebView mLoginView;
	OPIdentity mIdentity;// somehow the identity passed in the callback function
							// isn't same as the one created.

	public OPIdentityDelegateImplementation(WebView loginView,
			OPIdentity identity) {
		this.mLoginView = loginView;
		mIdentity = identity;
	}

	@Override
	public void onIdentityStateChanged(OPIdentity identity, IdentityStates state) {
		// TODO Auto-generated method stub
		Log.d("state", "identity state" + state);
		Log.d("output", "onIdentityStateChanged local identity " + identity
				+ "LoginManager.mIdentity " + mIdentity);

		switch (state) {
		case IdentityState_WaitingForBrowserWindowToBeLoaded:
			// LoginManager.loadOuterFrame();//load identity.html
			mLoginView.post(new Runnable() {
				public void run() {
					mLoginView
							.loadUrl("http://jsouter-v1-rel-lespaul-i.hcs.io/identity.html?view=choose");
				}
			});
			break;
		case IdentityState_WaitingForBrowserWindowToBeMadeVisible:

			mIdentity.notifyBrowserWindowVisible();
			break;
		case IdentityState_WaitingForBrowserWindowToClose:
			mIdentity.notifyBrowserWindowClosed();
			break;
		case IdentityState_Ready:
			// LoginManager.mIdentity.;
			break;
		}

	}

	@Override
	public void onIdentityPendingMessageForInnerBrowserWindowFrame(
			OPIdentity identity) {
		// TODO Auto-generated method stub
		String msg = mIdentity.getNextMessageForInnerBrowerWindowFrame();
		Log.d("output", "pendingMessageForInnerFrame " + msg);

		passMessageToJS(msg);
	}

	@Override
	public void onIdentityRolodexContactsDownloaded(OPIdentity identity) {
		// TODO Auto-generated method stub
		ContactsManager.getInstance().onDownloadedRolodexContacts(identity);
	}

	public void passMessageToJS(final String msg) {
		mLoginView.post(new Runnable() {
			public void run() {
				String cmd = String.format("javascript:sendBundleToJS(\'%s\')",
						msg);
				Log.w("JNI", "Pass to JS: " + cmd);
				mLoginView.loadUrl(cmd);
			}
		});
	}

}
