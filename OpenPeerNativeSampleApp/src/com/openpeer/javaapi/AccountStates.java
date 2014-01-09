package com.openpeer.javaapi;


public enum AccountStates
{
    AccountState_Pending (0),
    AccountState_PendingPeerFilesGeneration(1),
    AccountState_WaitingForAssociationToIdentity(2),
    AccountState_WaitingForBrowserWindowToBeLoaded(3),
    AccountState_WaitingForBrowserWindowToBeMadeVisible(4),
    AccountState_WaitingForBrowserWindowToClose(5),
    AccountState_Ready(6),
    AccountState_ShuttingDown(7),
    AccountState_Shutdown(8);
    
    AccountStates (int value)
    {
        this.type = value;
    }

    private int type;

    public int getNumericType()
    {
        return type;
    }
}
