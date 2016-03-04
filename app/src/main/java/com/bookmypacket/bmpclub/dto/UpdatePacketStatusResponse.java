package com.bookmypacket.bmpclub.dto;

/**
 * Created by Manish on 28-01-2016.
 */
public class UpdatePacketStatusResponse
{
    private String  errorMessage;
    private boolean isSuccess;
    private String  profileId;
    private String  message;
    private String  packetId;
    private String  packetToken;

    public String getErrorMessage()
    {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess()
    {
        return isSuccess;
    }

    public void setIsSuccess(boolean isSuccess)
    {
        this.isSuccess = isSuccess;
    }

    public String getProfileId()
    {
        return profileId;
    }

    public void setProfileId(String profileId)
    {
        this.profileId = profileId;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getPacketId()
    {
        return packetId;
    }

    public void setPacketId(String packetId)
    {
        this.packetId = packetId;
    }

    public String getPacketToken()
    {
        return packetToken;
    }

    public void setPacketToken(String packetToken)
    {
        this.packetToken = packetToken;
    }
}
