package com.bookmypacket.bmpclub.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Manish on 22-01-2016.
 */
public class RegisterHubResponse
{
    private String errorMessage;
    private List<HubName> hubResponseList = new ArrayList<HubName>();
    private boolean success;

    public String getErrorMessage()
    {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }

    public List<HubName> getHubResponseList()
    {
        return hubResponseList;
    }

    public void setHubResponseList(List<HubName> hubResponseList)
    {
        this.hubResponseList = hubResponseList;
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
