package com.bookmypacket.bmpclub.utils.retro;

import com.bookmypacket.bmpclub.dto.DepositCashRequest;
import com.bookmypacket.bmpclub.dto.DepositResponse;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.POST;

import static com.bookmypacket.bmpclub.utils.AppConstants.Headers.AUTH_HEADER_KEY;

/**
 * Created by Manish on 24-01-2016.
 */
public interface DepositAmountService
{
    @POST("depositamount")
    Call<DepositResponse> deposit(@Header(AUTH_HEADER_KEY) String authHeader,
                                  @Body DepositCashRequest request);
}
