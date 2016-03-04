package com.bookmypacket.bmpclub.utils.retro;

import com.bookmypacket.bmpclub.dto.MobileVerificationRequest;
import com.bookmypacket.bmpclub.dto.RegistrationResponse;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by Manish on 09-01-2016.
 */
public interface RegistrationLoginService
{
    @POST("userregistration")
    Call<RegistrationResponse> register(@Body MobileVerificationRequest request);
}
