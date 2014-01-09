package com.openpeer.javaapi;


public abstract class OPIdentityDelegate {

	public abstract void onIdentityStateChanged(
			OPIdentity identity,
            IdentityStates state
            );
	public abstract void onIdentityPendingMessageForInnerBrowserWindowFrame(OPIdentity identity);
}
