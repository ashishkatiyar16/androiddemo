package com.bookmypacket.bmpclub.dto;

/**
 * Created by Manish on 18-01-2016.
 */
public class AcceptPacketRequest
{
    private String assignedTo;
    private String packetId;
    private String acceptanceStatus = "PACKET_DELIVERY_ACCEPTED";

    public String getAcceptanceStatus()
    {
        return acceptanceStatus;
    }

    public void setAcceptanceStatus(String acceptanceStatus)
    {
        this.acceptanceStatus = acceptanceStatus;
    }

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
}
