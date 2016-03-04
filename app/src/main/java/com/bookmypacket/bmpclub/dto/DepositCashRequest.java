package com.bookmypacket.bmpclub.dto;

/**
 * Created by Manish on 24-01-2016.
 */
public class DepositCashRequest
{
    private String mobileNo;
    private String amount;
    private String modeOfPayment;
    private String ebsResponseMessage;
    private String couponCode;
    private String transactionId;

    public String getMobileNo()
    {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo)
    {
        this.mobileNo = mobileNo;
    }

    public String getAmount()
    {
        return amount;
    }

    public void setAmount(String amount)
    {
        this.amount = amount;
    }

    public String getModeOfPayment()
    {
        return modeOfPayment;
    }

    public void setModeOfPayment(String modeOfPayment)
    {
        this.modeOfPayment = modeOfPayment;
    }

    public String getEbsResponseMessage()
    {
        return ebsResponseMessage;
    }

    public void setEbsResponseMessage(String ebsResponseMessage)
    {
        this.ebsResponseMessage = ebsResponseMessage;
    }

    public String getCouponCode()
    {
        return couponCode;
    }

    public void setCouponCode(String couponCode)
    {
        this.couponCode = couponCode;
    }

    public String getTransactionId()
    {
        return transactionId;
    }

    public void setTransactionId(String transactionId)
    {
        this.transactionId = transactionId;
    }
}
