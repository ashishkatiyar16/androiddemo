package com.bookmypacket.bmpclub.utils.retro;

import com.bookmypacket.bmpclub.dto.LoginRequest;
import com.bookmypacket.bmpclub.dto.LoginResponse;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by Manish on 09-01-2016.
 */
public interface LoginService
{

    @POST("login")
    Call<LoginResponse> register(@Body LoginRequest request);
}
