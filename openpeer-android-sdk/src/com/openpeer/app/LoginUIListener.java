package com.openpeer.app;

public interface LoginUIListener {
	public void onStartIdentityLogin();

	public void onLoginComplete();

	public void onLoginError();

	public void onIdentityLoginWebViewMadeVisible();

	public void onAccountLoginWebViewMadeVisible();

	public void onIdentityLoginWebViewClose();

	public void onAccountLoginWebViewMadeClose();
}
