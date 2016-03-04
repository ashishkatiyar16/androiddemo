package com.bookmypacket.bmpclub.dto;

import java.io.Serializable;

/**
 * Created by Manish on 8/1/16.
 */
public class LoginVerifyRequest implements Serializable {
    private String mobileNo;
    private String otpCode;
    private String deviceId;

    public String getMobileNo() {return mobileNo;}

    public void setMobileNo(String mobileNo) {this.mobileNo = mobileNo;}

    public String getOtpCode() {return otpCode;}

    public void setOtpCode(String otpCode) {this.otpCode = otpCode;}

    public String getDeviceId()
    {
        return deviceId;
    }

    public void setDeviceId(String deviceId)
    {
        this.deviceId = deviceId;
    }
}
