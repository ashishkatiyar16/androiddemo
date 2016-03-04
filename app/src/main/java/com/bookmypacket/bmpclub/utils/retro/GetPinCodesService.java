package com.bookmypacket.bmpclub.utils.retro;

import com.bookmypacket.bmpclub.dto.BMPBranch;
import com.bookmypacket.bmpclub.dto.PinCodesResponse;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by Manish on 27-01-2016.
 */
public interface GetPinCodesService
{
    @POST("branchpincodelist")
    Call<PinCodesResponse> register(@Body BMPBranch request);
}
