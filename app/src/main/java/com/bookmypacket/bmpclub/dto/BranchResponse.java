package com.bookmypacket.bmpclub.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Manish on 24-01-2016.
 */
public class BranchResponse
{
    private String errorMessage;
    private List<BMPBranch> branchResponseList = new ArrayList<BMPBranch>();
    private boolean success;

    public String getErrorMessage()
    {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }

    public List<BMPBranch> getBranchResponseList()
    {
        return branchResponseList;
    }

    public void setBranchResponseList(
            List<BMPBranch> branchResponseList)
    {
        this.branchResponseList = branchResponseList;
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
