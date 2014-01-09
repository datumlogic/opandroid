package com.openpeer.javaapi;


public enum AccountStates
{
    AccountState_Pending,
    AccountState_PendingPeerFilesGeneration,
    AccountState_WaitingForAssociationToIdentity,
    AccountState_WaitingForBrowserWindowToBeLoaded,
    AccountState_WaitingForBrowserWindowToBeMadeVisible,
    AccountState_WaitingForBrowserWindowToClose,
    AccountState_Ready,
    AccountState_ShuttingDown,
    AccountState_Shutdown,
}
