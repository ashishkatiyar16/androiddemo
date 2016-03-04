package com.bookmypacket.bmpclub.dto;

/**
 * Created by Manish on 23-01-2016.
 */
public class WalletWithdrawRequest
{
    private String amount;
    private String mobileNo;

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
}
