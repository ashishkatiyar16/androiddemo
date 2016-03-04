package com.bookmypacket.bmpclub.dto;

import java.io.Serializable;

/**
 * A dummy item representing a piece of content.
 */
public class BMPPacket implements Serializable
{
    private String  packetId;
    private String  packetName;
    private String  description;
    private String  otpCode;
    private String  aWBNo;
    private String  status;
    private String  fromCityPin;
    private String  toCityPin;
    private String  category;
    private String  length;
    private String  width;
    private String  height;
    private String  weight;
    private String  packetCost;
    private String  courierCost;
    private String  insuranceCost;
    private String  ispaymentDone;
    private String  pickupTime;
    private String  pickupDate;
    private String  deliveryAddress;
    private String  pickupAddress;
    private String  sendpushNotification;
    private String  resendpushNotification;
    private String  isClubMemberAccepted;
    private String  packetPickupStatus;
    private String  clubMemberId;
    private String  message;
    private String  pickupCustomerMobileNo;
    private String  deliveryCustomerMobileNo;
    private String  codAmount;
    private boolean idProofMandatory;
    private boolean exists;
    private boolean otpcodeVerified;
    private boolean cod;
    private String earnings;

    public String getEarnings()
    {
        return earnings;
    }

    public void setEarnings(String earnings)
    {
        this.earnings = earnings;
    }

    public String getPacketId()
    {
        return packetId;
    }

    public void setPacketId(String packetId)
    {
        this.packetId = packetId;
    }

    public String getPacketName()
    {
        return packetName;
    }

    public void setPacketName(String packetName)
    {
        this.packetName = packetName;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getOtpCode()
    {
        return otpCode;
    }

    public void setOtpCode(String otpCode)
    {
        this.otpCode = otpCode;
    }

    public String getaWBNo()
    {
        return aWBNo;
    }

    public void setaWBNo(String aWBNo)
    {
        this.aWBNo = aWBNo;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getFromCityPin()
    {
        return fromCityPin;
    }

    public void setFromCityPin(String fromCityPin)
    {
        this.fromCityPin = fromCityPin;
    }

    public String getToCityPin()
    {
        return toCityPin;
    }

    public void setToCityPin(String toCityPin)
    {
        this.toCityPin = toCityPin;
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public String getLength()
    {
        return length;
    }

    public void setLength(String length)
    {
        this.length = length;
    }

    public String getWidth()
    {
        return width;
    }

    public void setWidth(String width)
    {
        this.width = width;
    }

    public String getHeight()
    {
        return height;
    }

    public void setHeight(String height)
    {
        this.height = height;
    }

    public String getWeight()
    {
        return weight;
    }

    public void setWeight(String weight)
    {
        this.weight = weight;
    }

    public String getPacketCost()
    {
        return packetCost;
    }

    public void setPacketCost(String packetCost)
    {
        this.packetCost = packetCost;
    }

    public String getCourierCost()
    {
        return courierCost;
    }

    public void setCourierCost(String courierCost)
    {
        this.courierCost = courierCost;
    }

    public String getInsuranceCost()
    {
        return insuranceCost;
    }

    public void setInsuranceCost(String insuranceCost)
    {
        this.insuranceCost = insuranceCost;
    }

    public String getIspaymentDone()
    {
        return ispaymentDone;
    }

    public void setIspaymentDone(String ispaymentDone)
    {
        this.ispaymentDone = ispaymentDone;
    }

    public String getPickupTime()
    {
        return pickupTime;
    }

    public void setPickupTime(String pickupTime)
    {
        this.pickupTime = pickupTime;
    }

    public String getPickupDate()
    {
        return pickupDate;
    }

    public void setPickupDate(String pickupDate)
    {
        this.pickupDate = pickupDate;
    }

    public String getDeliveryAddress()
    {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress)
    {
        this.deliveryAddress = deliveryAddress;
    }

    public String getPickupAddress()
    {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress)
    {
        this.pickupAddress = pickupAddress;
    }

    public String getSendpushNotification()
    {
        return sendpushNotification;
    }

    public void setSendpushNotification(String sendpushNotification)
    {
        this.sendpushNotification = sendpushNotification;
    }

    public String getResendpushNotification()
    {
        return resendpushNotification;
    }

    public void setResendpushNotification(String resendpushNotification)
    {
        this.resendpushNotification = resendpushNotification;
    }

    public String getIsClubMemberAccepted()
    {
        return isClubMemberAccepted;
    }

    public void setIsClubMemberAccepted(String isClubMemberAccepted)
    {
        this.isClubMemberAccepted = isClubMemberAccepted;
    }

    public String getPacketPickupStatus()
    {
        return packetPickupStatus;
    }

    public void setPacketPickupStatus(String packetPickupStatus)
    {
        this.packetPickupStatus = packetPickupStatus;
    }

    public String getClubMemberId()
    {
        return clubMemberId;
    }

    public void setClubMemberId(String clubMemberId)
    {
        this.clubMemberId = clubMemberId;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getPickupCustomerMobileNo()
    {
        return pickupCustomerMobileNo;
    }

    public void setPickupCustomerMobileNo(String pickupCustomerMobileNo)
    {
        this.pickupCustomerMobileNo = pickupCustomerMobileNo;
    }

    public String getDeliveryCustomerMobileNo()
    {
        return deliveryCustomerMobileNo;
    }

    public void setDeliveryCustomerMobileNo(String deliveryCustomerMobileNo)
    {
        this.deliveryCustomerMobileNo = deliveryCustomerMobileNo;
    }

    public String getCodAmount()
    {
        return codAmount;
    }

    public void setCodAmount(String codAmount)
    {
        this.codAmount = codAmount;
    }

    public boolean isIdProofMandatory()
    {
        return idProofMandatory;
    }

    public void setIdProofMandatory(boolean idProofMandatory)
    {
        this.idProofMandatory = idProofMandatory;
    }

    public boolean isExists()
    {
        return exists;
    }

    public void setExists(boolean exists)
    {
        this.exists = exists;
    }

    public boolean isOtpcodeVerified()
    {
        return otpcodeVerified;
    }

    public void setOtpcodeVerified(boolean otpcodeVerified)
    {
        this.otpcodeVerified = otpcodeVerified;
    }

    public boolean isCod()
    {
        return cod;
    }

    public void setCod(boolean cod)
    {
        this.cod = cod;
    }
}
