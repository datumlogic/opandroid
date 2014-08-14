package com.openpeer.sdk.app;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.openpeer.javaapi.OPIdentity;

import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class OPIdentityLoginWebViewClient extends WebViewClient {

	OPIdentity mIdentity;

	public void setIdentity(OPIdentity identity) {
		mIdentity = identity;
	}

	boolean mInnerFrameLoaded;

	public OPIdentityLoginWebViewClient(OPIdentity identity) {
		mIdentity = identity;
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
			Log.w("login", "url to intercept " + url);
			String data = url.substring(url.lastIndexOf("data="));
			data = data.substring(5);
			// Log.w("JNI", data);
			try {
				data = URLDecoder.decode(data, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Log.w("login", "Identity Received from JS: " + data);
			mIdentity.handleMessageFromInnerBrowserWindowFrame(data);

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

		if (!mInnerFrameLoaded) {
			mInnerFrameLoaded = true;
			String cmd = String.format("javascript:initInnerFrame(\'%s\')",
					mIdentity.getInnerBrowserWindowFrameURL());
			Log.w("login", "INIT INNER FRAME: " + cmd);
			view.loadUrl(cmd);
			// LoginManager.initInnerFrame();
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
