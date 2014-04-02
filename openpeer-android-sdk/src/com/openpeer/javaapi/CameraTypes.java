package com.openpeer.javaapi;


public enum CameraTypes
{
    CameraType_None (0),
    CameraType_Front (1),
    CameraType_Back (2);
    
    CameraTypes (int value)
    {
        this.type = value;
    }

    private int type;

    public int getNumericType()
    {
        return type;
    }
}
