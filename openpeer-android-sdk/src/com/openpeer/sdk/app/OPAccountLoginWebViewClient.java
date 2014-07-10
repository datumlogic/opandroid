package com.openpeer.sdk.app;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.openpeer.javaapi.OPAccount;

public class OPAccountLoginWebViewClient extends WebViewClient {

	OPAccount mAccount;
	boolean mInnerFrameLoaded;
	boolean mNamespaceGrantInnerFrameLoaded;

	public OPAccountLoginWebViewClient(OPAccount account) {
		mAccount = account;
	}

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		view.loadUrl(url);
		return true;
	}

	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		super.onPageStarted(view, url, favicon);
	}

	@Override
	public void onLoadResource(WebView view, String url) {
		// view.loadUrl(url);
		if (url.contains("datapass")) {
			int i = 1;
			i++;
		} else {
			super.onLoadResource(view, url);
		}

	}

	@Override
	public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
		if (url.contains("datapass")) {
			Log.d("login", "Account client " + url);
			String data = url.substring(url.lastIndexOf("data="));
			data = data.substring(5);
			// Log.w("JNI", data);
			try {
				data = URLDecoder.decode(data, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Log.d("login", "Account Client Namespace grant Received from JS: " + data);
			// mAccount.handleMessageFromInnerBrowserWindowFrame(data);
			mAccount.handleMessageFromInnerBrowserWindowFrame(data);

			return null;
		} else if (url.contains("?reload=true")) {
			int i = 1;
			i++;
			return null;
		} else {
			return super.shouldInterceptRequest(view, url);
		}

	}

	@Override
	public void onPageFinished(WebView view, String url) {

		if (!mNamespaceGrantInnerFrameLoaded) {
			mNamespaceGrantInnerFrameLoaded = true;
			String cmd = String.format("javascript:initInnerFrame(\'%s\')",
					mAccount.getInnerBrowserWindowFrameURL());
			Log.d("login", "Account Client INIT NAMESPACE GRANT INNER FRAME: " + cmd);
			view.loadUrl(cmd);

		} else {
			super.onPageFinished(view, url);
		}
	}

	@Override
	public void onReceivedError(WebView view, int errorCode,
			String description, String failingUrl) {
		int i = 1;
		i++;
	}

}
