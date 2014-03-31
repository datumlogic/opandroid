package com.openpeer.javaapi;


public enum VideoOrientations
{
    VideoOrientation_LandscapeLeft (0),
    VideoOrientation_PortraitUpsideDown (1),
    VideoOrientation_LandscapeRight (2),
    VideoOrientation_Portrait (3);
    
    VideoOrientations (int value)
    {
        this.type = value;
    }

    private int type;

    public int getNumericType()
    {
        return type;
    }
}
