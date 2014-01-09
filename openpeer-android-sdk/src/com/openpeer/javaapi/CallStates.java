package com.openpeer.javaapi;


public enum CallStates
{
    CallState_None,       // call has no state yet
    CallState_Preparing,  // call is negotiating in the background - do not present this call to a user yet...
    CallState_Incoming,   // call is incoming from a remote party
    CallState_Placed,     // call has been placed to the remote party
    CallState_Early,      // call is outgoing to a remote party and is receiving early media (media before being answered)
    CallState_Ringing,    // call is incoming from a remote party and is ringing
    CallState_Ringback,   // call is outgoing to a remote party and remote party is ringing
    CallState_Open,       // call is open
    CallState_Active,     // call is open, and participant is actively communicating
    CallState_Inactive,   // call is open, and participant is inactive
    CallState_Hold,       // call is open but on hold
    CallState_Closing,    // call is hanging up
    CallState_Closed,     // call has ended
 }
