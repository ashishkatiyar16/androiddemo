package com.bookmypacket.bmpclub.utils.retro;

import com.bookmypacket.bmpclub.dto.PaymentDetailsRequest;
import com.bookmypacket.bmpclub.dto.PaymentDetailsResponse;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by Manish on 18-01-2016.
 */
public interface RegistrationPaymentService
{
    @POST("userregistration")
    Call<PaymentDetailsResponse> register(@Body PaymentDetailsRequest request);
}
