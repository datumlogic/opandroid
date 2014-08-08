package com.openpeer.sample;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.openpeer.sample.push.HackApiService;
import com.openpeer.sample.push.PushRegistrationManager;
import com.openpeer.sdk.app.LoginManager;
import com.openpeer.sdk.app.LoginUIListener;
import com.openpeer.sdk.app.OPDataManager;
import com.openpeer.sdk.app.OPHelper;
import com.openpeer.sdk.datastore.OPDatastoreDelegateImplementation;
import com.openpeer.sample.contacts.ContactsFragment;
import com.openpeer.sample.delegates.OPCallDelegateImplementation;
import com.urbanairship.push.PushManager;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, null);
        progressView = view.findViewById(R.id.progress);
        mAccountLoginWebView = (WebView) view.findViewById(R.id.webview_account_login);
        mIdentityLoginWebView = (WebView) view.findViewById(R.id.webview_identity_login);
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
        if (loginTask == null) {
            loginTask = new AccountLogin();
        }
        loginTask.execute();
        // String reloginInfo = OPDataManager.getInstance().getReloginInfo();
        // if (reloginInfo == null || reloginInfo.length() == 0) {
        // Log.d("login", "LoginFragment startLogin() logging in");
        // LoginManager.getInstance().setup(this, mAccountLoginWebView, mIdentityLoginWebView, new OPCallDelegateImplementation()).login();
        // } else {
        // Log.d("login", "LoginFragment startLogin() relogging in");
        //
        // LoginManager.getInstance().setup(this, mAccountLoginWebView, mIdentityLoginWebView, new OPCallDelegateImplementation())
        // .relogin(OPDataManager.getInstance().getReloginInfo());
        // }

    }

    AccountLogin loginTask = null;

    private class AccountLogin extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

//			OPHelper.getInstance().init(getActivity(), null);
            String reloginInfo = OPDataManager.getInstance().getReloginInfo();
            if (reloginInfo == null || reloginInfo.length() == 0) {
                Log.d("login", "LoginFragment startLogin() logging in");
                LoginManager.getInstance()
                        .setup(LoginFragment.this, mAccountLoginWebView, mIdentityLoginWebView, new OPCallDelegateImplementation()).login();
            } else {
                Log.d("login", "LoginFragment startLogin() relogging in");

                LoginManager.getInstance()
                        .setup(LoginFragment.this, mAccountLoginWebView, mIdentityLoginWebView, new OPCallDelegateImplementation())
                        .relogin(OPDataManager.getInstance().getReloginInfo());
            }

            // TextView txt = (TextView) findViewById(R.id.output);
            // txt.setText("Executed");
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            loginTask = null;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
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

        PushManager.enablePush();

        //TODO: move it to proper place after login refactoring.
        String apid = PushManager.shared().getAPID();
        if (!TextUtils.isEmpty(apid)) {
            PushRegistrationManager.getInstance().associateDeviceToken(OPDataManager.getInstance().getSharedAccount().getPeerUri(),
                    PushManager.shared().getAPID(),
                    new Callback<HackApiService.HackAssociateResult>() {
                        @Override
                        public void success(HackApiService.HackAssociateResult hackAssociateResult, Response response) {

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
        if (!isDetached()) {
            Toast.makeText(getActivity(), R.string.msg_failed_login, Toast.LENGTH_LONG).show();
            ((BaseFragmentActivity) getActivity()).hideLoginFragment();
        }
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
