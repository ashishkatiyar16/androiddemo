package com.bookmypacket.bmpclub.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class BMPPacketsList
{

    /**
     * An array of sample (dummy) items.
     */
    private List<BMPPacket> packetResponses = new ArrayList<BMPPacket>();
    private String  errorMessage;
    private String  totalAmount;
    private String  pendingAmount;
    private String  recievedAmount;
    private Boolean success;

    public List<BMPPacket> getPacketResponses()
    {
        return packetResponses;
    }

    public void setPacketResponses(
            List<BMPPacket> packetResponses)
    {
        this.packetResponses = packetResponses;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }

    public String getTotalAmount()
    {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount)
    {
        this.totalAmount = totalAmount;
    }

    public String getPendingAmount()
    {
        return pendingAmount;
    }

    public void setPendingAmount(String pendingAmount)
    {
        this.pendingAmount = pendingAmount;
    }

    public String getRecievedAmount()
    {
        return recievedAmount;
    }

    public void setRecievedAmount(String recievedAmount)
    {
        this.recievedAmount = recievedAmount;
    }

    public Boolean getSuccess()
    {
        return success;
    }

    public void setSuccess(Boolean success)
    {
        this.success = success;
    }
}
