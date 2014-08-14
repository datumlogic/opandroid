package com.openpeer.javaapi;

public enum CallStates
{
	CallState_None, // call has no state yet
	/**
	 * // call is negotiating in the background - do not present this call to a user yet.
	 */
	CallState_Preparing,
	/**
	 * call is incoming from a remote party
	 */
	CallState_Incoming,
	/**
	 * call has been placed to the remote party
	 */
	CallState_Placed,
	/**
	 * call is outgoing to a remote party and is receiving early media (media before being answered)
	 */
	CallState_Early,
	/**
	 * call is incoming from a remote party and is ringing
	 */
	CallState_Ringing,
	/**
	 * call is outgoing to a remote party and remote party is ringing
	 */
	CallState_Ringback,
	/**
	 * call is open
	 */
	CallState_Open,
	/**
	 * call is open, and participant is actively communicating
	 */
	CallState_Active,
	/**
	 * call is open, and participant is inactive
	 */
	CallState_Inactive,
	/**
	 * call is open but on hold
	 */
	CallState_Hold,
	/**
	 * call is hanging up
	 */
	CallState_Closing,
	/**
	 * call has ended
	 */
	CallState_Closed;

}
