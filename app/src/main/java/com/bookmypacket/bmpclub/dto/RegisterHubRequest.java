package com.bookmypacket.bmpclub.dto;

/**
 * Created by Manish on 22-01-2016.
 */
public class RegisterHubRequest
{
    private String lattitude;
    private String longitude;

    public String getLattitude()
    {
        return lattitude;
    }

    public void setLattitude(String lattitude)
    {
        this.lattitude = lattitude;
    }

    public String getLongitude()
    {
        return longitude;
    }

    public void setLongitude(String longitude)
    {
        this.longitude = longitude;
    }
}
