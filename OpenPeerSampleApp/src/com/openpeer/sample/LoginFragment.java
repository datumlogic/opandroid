package com.openpeer.sample;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.openpeer.sdk.app.LoginManager;
import com.openpeer.sdk.app.LoginUIListener;
import com.openpeer.sdk.app.OPDataManager;
import com.openpeer.sdk.datastore.OPDatastoreDelegateImplementation;
import com.openpeer.sample.contacts.ContactsFragment;
import com.openpeer.sample.delegates.OPCallDelegateImplementation;

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
		// view.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		startLogin();
		// }
		// });
		return view;
	}

	void startLogin() {
		String reloginInfo = OPDataManager.getInstance().getReloginInfo();
		if (reloginInfo == null || reloginInfo.length() == 0) {
			Log.d("login", "LoginFragment startLogin() logging in");
			LoginManager.getInstance()
					.setup(this, mAccountLoginWebView, mIdentityLoginWebView, new OPCallDelegateImplementation())
					.login();
		} else {
			Log.d("login", "LoginFragment startLogin() relogging in");

			LoginManager.getInstance()
					.setup(this, mAccountLoginWebView, mIdentityLoginWebView, new OPCallDelegateImplementation())
					.relogin(OPDataManager.getInstance().getReloginInfo());
		}

	}

	void setupWebView(WebView view) {
		WebSettings webSettings = view.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setDomStorageEnabled(true);
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

	}

	/* START implementation of LoginUIListener */
	@Override
	public void onStartIdentityLogin() {
		progressView.setVisibility(View.GONE);
		mAccountLoginWebView.setVisibility(View.GONE);

		mIdentityLoginWebView.setVisibility(View.VISIBLE);
	}

	@Override
	public void onLoginComplete() {
		getActivity().runOnUiThread(new Runnable() {
			public void run() {
				if (!isDetached()) {
					((BaseFragmentActivity) getActivity()).hideLoginFragment();
				}
			}
		});

	}

	@Override
	public void onLoginError() {
		// TODO Auto-generated method stub

	}
	@Override
	public void onIdentityLoginWebViewMadeVisible() {
		mIdentityLoginWebView.setVisibility(View.VISIBLE);
		mAccountLoginWebView.setVisibility(View.GONE);
		progressView.setVisibility(View.GONE);

	}

	@Override
	public void onAccountLoginWebViewMadeVisible() {
		progressView.setVisibility(View.GONE);
		mIdentityLoginWebView.setVisibility(View.GONE);
		mAccountLoginWebView.setVisibility(View.VISIBLE);
	}

	public void onIdentityLoginWebViewClose() {
		mIdentityLoginWebView.setVisibility(View.GONE);
	}

	public void onAccountLoginWebViewMadeClose() {
		mAccountLoginWebView.setVisibility(View.GONE);
	}
	/* END implementation of LoginUIListener */

}
