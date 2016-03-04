package com.bookmypacket.bmpclub.utils.retro;

import com.bookmypacket.bmpclub.dto.WalletWithdrawRequest;
import com.bookmypacket.bmpclub.dto.WalletWithdrawResponse;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.POST;

import static com.bookmypacket.bmpclub.utils.AppConstants.Headers.AUTH_HEADER_KEY;

/**
 * Created by Manish on 23-01-2016.
 */
public interface WalletWithdrawService
{
    @POST("deductsecurityamount")
    Call<WalletWithdrawResponse> deduct(@Header(AUTH_HEADER_KEY) String authHeader,
                                        @Body WalletWithdrawRequest request);
}
