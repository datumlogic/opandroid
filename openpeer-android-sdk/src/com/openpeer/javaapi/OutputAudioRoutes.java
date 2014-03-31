package com.openpeer.javaapi;


public enum OutputAudioRoutes
{
    OutputAudioRoute_Headphone (0),
    OutputAudioRoute_BuiltInReceiver (1),
    OutputAudioRoute_BuiltInSpeaker (2);
    
    OutputAudioRoutes (int value)
    {
        this.type = value;
    }

    private int type;

    public int getNumericType()
    {
        return type;
    }
}
