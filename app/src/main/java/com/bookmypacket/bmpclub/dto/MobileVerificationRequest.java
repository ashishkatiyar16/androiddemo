package com.bookmypacket.bmpclub.dto;

import java.io.Serializable;

/**
 * Created by Manish on 8/1/16.
 */
public class MobileVerificationRequest implements Serializable
{
    private String stepName;
    private String deviceId;
    private String otpCode;
    private String mobileNo;

    public String getStepName()
    {
        return stepName;
    }

    public void setStepName(String stepName)
    {
        this.stepName = stepName;
    }

    public String getDeviceId()
    {
        return deviceId;
    }

    public void setDeviceId(String deviceId)
    {
        this.deviceId = deviceId;
    }

    public String getOtpCode()
    {
        return otpCode;
    }

    public void setOtpCode(String otpCode)
    {
        this.otpCode = otpCode;
    }

    public String getMobileNo()
    {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo)
    {
        this.mobileNo = mobileNo;
    }
}
