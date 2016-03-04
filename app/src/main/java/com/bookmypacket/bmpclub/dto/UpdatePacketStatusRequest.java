package com.bookmypacket.bmpclub.dto;

/**
 * Created by Manish on 28-01-2016.
 */
public class UpdatePacketStatusRequest
{
    private String packetId;
    private String assignedTo;
    private String packetStatus = "PACKET_UNDELIVERED";
    private String lattitude;
    private String packetSubStatus;
    private String longitute;
    private String otpVerified;

    public String getPacketId()
    {
        return packetId;
    }

    public void setPacketId(String packetId)
    {
        this.packetId = packetId;
    }

    public String getAssignedTo()
    {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo)
    {
        this.assignedTo = assignedTo;
    }

    public String getPacketStatus()
    {
        return packetStatus;
    }

    public void setPacketStatus(String packetStatus)
    {
        this.packetStatus = packetStatus;
    }

    public String getLattitude()
    {
        return lattitude;
    }

    public void setLattitude(String lattitude)
    {
        this.lattitude = lattitude;
    }

    public String getPacketSubStatus()
    {
        return packetSubStatus;
    }

    public void setPacketSubStatus(String packetSubStatus)
    {
        this.packetSubStatus = packetSubStatus;
    }

    public String getLongitute()
    {
        return longitute;
    }

    public void setLongitute(String longitute)
    {
        this.longitute = longitute;
    }

    public String getOtpVerified()
    {
        return otpVerified;
    }

    public void setOtpVerified(String otpVerified)
    {
        this.otpVerified = otpVerified;
    }
}
