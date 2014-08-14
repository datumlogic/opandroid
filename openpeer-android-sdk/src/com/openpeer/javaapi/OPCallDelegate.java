package com.openpeer.javaapi;

/**
 * Callback events handler for call state changes. Application must provide an instance when login, and the instance must be valid
 * throughout the app lifecycle
 */
public abstract class OPCallDelegate {

	public abstract void onCallStateChanged(
			OPCall call,
			CallStates state
			);
}
