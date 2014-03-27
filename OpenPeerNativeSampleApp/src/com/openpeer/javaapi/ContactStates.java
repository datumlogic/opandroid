package com.openpeer.javaapi;


public enum ContactStates {

	ContactState_NotApplicable (0),
	ContactState_Finding (1),
	ContactState_Connected (2),
	ContactState_Disconnected (3);
    
	ContactStates (int value)
    {
        this.type = value;
    }

    private int type;

    public int getNumericType()
    {
        return type;
    }
}
