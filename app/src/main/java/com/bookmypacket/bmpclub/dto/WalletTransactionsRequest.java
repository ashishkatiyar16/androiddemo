package com.bookmypacket.bmpclub.dto;

/**
 * Created by Manish on 23-01-2016.
 */
public class WalletTransactionsRequest
{
    private String mobileNo;
    private String pageNumber;
    private String pageSize = "11";

    public String getMobileNo()
    {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo)
    {
        this.mobileNo = mobileNo;
    }

    public String getPageNumber()
    {
        return pageNumber;
    }

    public void setPageNumber(String pageNumber)
    {
        this.pageNumber = pageNumber;
    }

    public String getPageSize()
    {
        return pageSize;
    }

    public void setPageSize(String pageSize)
    {
        this.pageSize = pageSize;
    }
}
