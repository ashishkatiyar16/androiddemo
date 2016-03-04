package com.bookmypacket.bmpclub.dto;

import java.io.Serializable;

/**
 * Created by Manish on 8/1/16.
 */
public class LoginRequest implements Serializable{
    private String mobileNo;
    private String deviceId;

    public String getMobileNo() {return mobileNo;}

    public void setMobileNo(String mobileNo) {this.mobileNo = mobileNo;}

    public String getDeviceId()
    {
        return deviceId;
    }

    public void setDeviceId(String deviceId)
    {
        this.deviceId = deviceId;
    }
}
