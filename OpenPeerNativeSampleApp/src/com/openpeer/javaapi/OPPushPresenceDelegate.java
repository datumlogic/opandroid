package com.openpeer.javaapi;

public abstract class OPPushPresenceDelegate {

	public abstract void onPushPresenceStateChanged(
			OPPushPresence presence,
			PushPresenceStates state
			);

	public abstract void onPushPresenceNewStatus(
			OPPushPresence presence,
			OPPushPresenceStatus status
			);
}
