package com.bookmypacket.bmpclub.utils.retro;

import com.bookmypacket.bmpclub.dto.BranchResponse;
import com.bookmypacket.bmpclub.dto.HubName;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by Manish on 25-01-2016.
 */
public interface GetBranchListService
{
    @POST("branchlist")
    Call<BranchResponse> register(@Body HubName request);
}
