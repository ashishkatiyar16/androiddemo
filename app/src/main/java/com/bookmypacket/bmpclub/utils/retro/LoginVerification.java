package com.bookmypacket.bmpclub.utils.retro;

import com.bookmypacket.bmpclub.dto.LoginVerifyRequest;
import com.bookmypacket.bmpclub.dto.LoginVerifyResponse;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by Manish on 1-01-2016.
 */
public interface LoginVerification
{
    @POST("loginverify")
    Call<LoginVerifyResponse> loginUser(@Body LoginVerifyRequest request);
}
