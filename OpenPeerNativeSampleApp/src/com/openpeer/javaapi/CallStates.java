package com.openpeer.javaapi;


public enum CallStates
{
    CallState_None (0),       // call has no state yet
    CallState_Preparing (1),  // call is negotiating in the background - do not present this call to a user yet...
    CallState_Incoming (2),   // call is incoming from a remote party
    CallState_Placed (3),     // call has been placed to the remote party
    CallState_Early (4),      // call is outgoing to a remote party and is receiving early media (media before being answered)
    CallState_Ringing (5),    // call is incoming from a remote party and is ringing
    CallState_Ringback (6),   // call is outgoing to a remote party and remote party is ringing
    CallState_Open (7),       // call is open
    CallState_Active (8),     // call is open, and participant is actively communicating
    CallState_Inactive (9),   // call is open, and participant is inactive
    CallState_Hold (10),       // call is open but on hold
    CallState_Closing (11),    // call is hanging up
    CallState_Closed (12);     // call has ended
    
    CallStates (int value)
    {
        this.type = value;
    }

    private int type;

    public int getNumericType()
    {
        return type;
    }
 }
