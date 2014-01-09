package com.openpeer.javaapi;


public abstract class OPCallDelegate {

	public abstract  void onCallStateChanged(
            OPCall call,
            CallStates state
            );
}
