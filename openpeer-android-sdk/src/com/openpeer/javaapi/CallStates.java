/*******************************************************************************
 *
 *  Copyright (c) 2014 , Hookflash Inc.
 *  All rights reserved.
 *  
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *  
 *  1. Redistributions of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 *  
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 *  ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *  
 *  The views and conclusions contained in the software and documentation are those
 *  of the authors and should not be interpreted as representing official policies,
 *  either expressed or implied, of the FreeBSD Project.
 *******************************************************************************/
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
