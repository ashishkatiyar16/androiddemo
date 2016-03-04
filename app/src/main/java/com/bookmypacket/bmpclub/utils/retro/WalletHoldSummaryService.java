package com.bookmypacket.bmpclub.utils.retro;

import com.bookmypacket.bmpclub.dto.WalletSummaryRequest;
import com.bookmypacket.bmpclub.dto.WalletSummaryResponse;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.POST;

import static com.bookmypacket.bmpclub.utils.AppConstants.Headers.AUTH_HEADER_KEY;

/**
 * Created by Manish on 23-01-2016.
 */
public interface WalletHoldSummaryService
{
    @POST("walletWithholdamount")
    Call<WalletSummaryResponse> get(@Header(AUTH_HEADER_KEY) String authHeader,
                                    @Body WalletSummaryRequest request);
}
