package com.openpeer.javaapi;


public enum MessageDeliveryStates {
	MessageDeliveryState_Discovering (0),
	MessageDeliveryState_UserNotAvailable (1),
	MessageDeliveryState_Delivered (2);

	MessageDeliveryStates (int value)
	{
		this.type = value;
	}

	private int type;

	public int getNumericType()
	{
		return type;
	}
}
