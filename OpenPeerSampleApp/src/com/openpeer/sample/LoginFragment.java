package com.openpeer.sample;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.openpeer.app.LoginManager;
import com.openpeer.app.LoginUIListener;
import com.openpeer.datastore.OPDatastoreDelegateImplementation;

public class LoginFragment extends BaseFragment implements LoginUIListener {
	WebView mAccountLoginWebView;

	WebView mIdentityLoginWebView;
	View progressView;

	public static LoginFragment newInstance() {
		return new LoginFragment();
	}

	public WebView getmAccountLoginWebView() {
		return mAccountLoginWebView;
	}

	public WebView getmIdentityLoginWebView() {
		return mIdentityLoginWebView;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_login, null);
		progressView = view.findViewById(R.id.progress);
		mAccountLoginWebView = (WebView) view
				.findViewById(R.id.webview_account_login);
		mIdentityLoginWebView = (WebView) view
				.findViewById(R.id.webview_identity_login);
		setupWebView(mAccountLoginWebView);

		setupWebView(mIdentityLoginWebView);
		if (OPDatastoreDelegateImplementation.getInstance().getReloginInfo() == null) {
			Log.d("test", "logging in");

			LoginManager loginManager = new LoginManager(this, mAccountLoginWebView,
					mIdentityLoginWebView);
			loginManager.login();
		} else {
			Log.d("test", "re-logging in");

			LoginManager loginManager = new LoginManager(this, mAccountLoginWebView,
					mIdentityLoginWebView);
			loginManager.relogin(OPDatastoreDelegateImplementation
					.getInstance().getReloginInfo());
		}
		return view;
	}

	void setupWebView(WebView view) {

	}

	/* START implementation of LoginUIListener */
	@Override
	public void onStartIdentityLogin() {
		progressView.setVisibility(View.GONE);
	}

	@Override
	public void onLoginComplete() {

	}

	@Override
	public void onLoginError() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onIdentityLoginWebViewMadeVisible() {
		mIdentityLoginWebView.setVisibility(View.VISIBLE);
		mAccountLoginWebView.setVisibility(View.GONE);
	}

	@Override
	public void onAccountLoginWebViewMadeVisible() {
		mIdentityLoginWebView.setVisibility(View.GONE);
		mAccountLoginWebView.setVisibility(View.VISIBLE);
	}
	/* END implementation of LoginUIListener */

}
