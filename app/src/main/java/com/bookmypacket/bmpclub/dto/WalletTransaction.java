package com.bookmypacket.bmpclub.dto;

/**
 * Created by Manish on 18-01-2016.
 */
public class WalletTransaction
{
    private String  transactionId;
    private String  mobileNo;
    private String  preTransactionAmount;
    private String  postTransactionAmount;
    private String  transactionStatus;
    private String  transactionType;
    private String  transactionOtp;
    private String  commission;
    private String  userId;
    private String  transactionAmount;
    private String  dateCreated;
    private String  modeofPayment;
    private String  couponCode;
    private Boolean otpVerified;

    public String getTransactionId()
    {
        return transactionId;
    }

    public void setTransactionId(String transactionId)
    {
        this.transactionId = transactionId;
    }

    public String getMobileNo()
    {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo)
    {
        this.mobileNo = mobileNo;
    }

    public String getPreTransactionAmount()
    {
        return preTransactionAmount;
    }

    public void setPreTransactionAmount(String preTransactionAmount)
    {
        this.preTransactionAmount = preTransactionAmount;
    }

    public String getPostTransactionAmount()
    {
        return postTransactionAmount;
    }

    public void setPostTransactionAmount(String postTransactionAmount)
    {
        this.postTransactionAmount = postTransactionAmount;
    }

    public String getTransactionStatus()
    {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus)
    {
        this.transactionStatus = transactionStatus;
    }

    public String getTransactionType()
    {
        return transactionType;
    }

    public void setTransactionType(String transactionType)
    {
        this.transactionType = transactionType;
    }

    public String getTransactionOtp()
    {
        return transactionOtp;
    }

    public void setTransactionOtp(String transactionOtp)
    {
        this.transactionOtp = transactionOtp;
    }

    public String getCommission()
    {
        return commission;
    }

    public void setCommission(String commission)
    {
        this.commission = commission;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getTransactionAmount()
    {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount)
    {
        this.transactionAmount = transactionAmount;
    }

    public String getDateCreated()
    {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated)
    {
        this.dateCreated = dateCreated;
    }

    public String getModeofPayment()
    {
        return modeofPayment;
    }

    public void setModeofPayment(String modeofPayment)
    {
        this.modeofPayment = modeofPayment;
    }

    public String getCouponCode()
    {
        return couponCode;
    }

    public void setCouponCode(String couponCode)
    {
        this.couponCode = couponCode;
    }

    public Boolean getOtpVerified()
    {
        return otpVerified;
    }

    public void setOtpVerified(Boolean otpVerified)
    {
        this.otpVerified = otpVerified;
    }
}
