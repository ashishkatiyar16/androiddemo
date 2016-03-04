package com.bookmypacket.bmpclub.utils.retro;

import com.bookmypacket.bmpclub.dto.Transaction;
import com.bookmypacket.bmpclub.dto.WalletTransactionsRequest;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.POST;

import static com.bookmypacket.bmpclub.utils.AppConstants.Headers.AUTH_HEADER_KEY;

/**
 * Created by Manish on 23-01-2016.
 */
public interface WalletTransactionsHistoryService
{
    @POST("transactionhistory")
    Call<List<Transaction>> get(@Header(AUTH_HEADER_KEY) String authHeader,
                                @Body WalletTransactionsRequest request);
}
