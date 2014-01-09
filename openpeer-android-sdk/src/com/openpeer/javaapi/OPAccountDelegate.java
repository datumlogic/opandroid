package com.openpeer.javaapi;


public abstract class OPAccountDelegate {

	public abstract void onAccountStateChanged(
            OPAccount account,
            AccountStates state
            );

	public abstract void onAccountAssociatedIdentitiesChanged(OPAccount account);

	public abstract void onAccountPendingMessageForInnerBrowserWindowFrame(OPAccount account);
}
