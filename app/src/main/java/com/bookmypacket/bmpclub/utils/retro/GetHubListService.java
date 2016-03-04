package com.bookmypacket.bmpclub.utils.retro;

import com.bookmypacket.bmpclub.dto.RegisterHubRequest;
import com.bookmypacket.bmpclub.dto.RegisterHubResponse;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by Manish on 24-01-2016.
 */
public interface GetHubListService
{
    @POST("hublist")
    Call<RegisterHubResponse> register(@Body RegisterHubRequest request);
}
