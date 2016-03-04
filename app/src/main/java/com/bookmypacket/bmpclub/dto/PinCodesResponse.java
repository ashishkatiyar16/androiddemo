package com.bookmypacket.bmpclub.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Manish on 27-01-2016.
 */
public class PinCodesResponse
{
    private List<BranchPincode> branchPincodeResponseList = new ArrayList<BranchPincode>();
    private String  errorMessage;
    private boolean success;

    public List<BranchPincode> getBranchPincodeResponseList()
    {
        return branchPincodeResponseList;
    }

    public void setBranchPincodeResponseList(
            List<BranchPincode> branchPincodeResponseList)
    {
        this.branchPincodeResponseList = branchPincodeResponseList;
    }

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
        return success;
    }

    public void setSuccess(boolean success)
    {
        this.success = success;
    }
}
