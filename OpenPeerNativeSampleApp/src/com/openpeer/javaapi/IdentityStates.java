package com.openpeer.javaapi;


public enum IdentityStates
{
    IdentityState_Pending (0),
    IdentityState_PendingAssociation (1),
    IdentityState_WaitingAttachmentOfDelegate (2),
    IdentityState_WaitingForBrowserWindowToBeLoaded (3),
    IdentityState_WaitingForBrowserWindowToBeMadeVisible (4),
    IdentityState_WaitingForBrowserWindowToClose (5),
    IdentityState_Ready (6),
    IdentityState_Shutdown (7);
    
    IdentityStates (int value)
    {
        this.type = value;
    }

    private int type;

    public int getNumericType()
    {
        return type;
    }
}
