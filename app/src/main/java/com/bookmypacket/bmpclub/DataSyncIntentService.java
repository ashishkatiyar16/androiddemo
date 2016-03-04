package com.bookmypacket.bmpclub;

import android.app.IntentService;
import android.content.Intent;

import com.bookmypacket.bmpclub.dto.WalletSummaryRequest;
import com.bookmypacket.bmpclub.dto.WalletSummaryResponse;
import com.bookmypacket.bmpclub.utils.SharedPrefrenceManager;
import com.bookmypacket.bmpclub.utils.retro.ServiceGenerator;
import com.bookmypacket.bmpclub.utils.retro.WalletAmountService;
import com.bookmypacket.bmpclub.utils.retro.WalletHoldSummaryService;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static com.bookmypacket.bmpclub.utils.AppConstants.SharedPrefrencesKeys.WALLET_AVAILABLE_AMOUNT;
import static com.bookmypacket.bmpclub.utils.AppConstants.SharedPrefrencesKeys.WALLET_HOLD_AMOUNT;
import static com.bookmypacket.bmpclub.utils.SharedPrefrenceManager.getMobileNo;
import static com.bookmypacket.bmpclub.utils.SharedPrefrenceManager.savePrefrence;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class DataSyncIntentService extends IntentService
{


    public DataSyncIntentService()
    {
        super("DataSyncIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent)
    {
        if (intent != null)
        {
            getHoldAmount();
        }
    }

    private void getHoldAmount()
    {
        String               authHeader = SharedPrefrenceManager.getAuthHeader(this);
        String mobileNo = getMobileNo(this);
        WalletSummaryRequest request    = new WalletSummaryRequest();
        request.setMobileNo(mobileNo);
        WalletAmountService amountService =
                ServiceGenerator.createService(WalletAmountService.class);
        WalletHoldSummaryService service =
                ServiceGenerator.createService(WalletHoldSummaryService.class);
        Call<WalletSummaryResponse> call = service.get(authHeader, request);
        call.enqueue(new Callback<WalletSummaryResponse>()
        {
            @Override
            public void onResponse(Response<WalletSummaryResponse> response, Retrofit retrofit)
            {
                if (response.body() != null && response.body().isSuccess())
                {
                    savePrefrence(DataSyncIntentService.this,
                                  WALLET_HOLD_AMOUNT,
                                  response.body().getAmount());
                }
            }

            @Override
            public void onFailure(Throwable t)
            {

            }
        });
        Call<WalletSummaryResponse> walletCall = amountService.get(authHeader, request);
        walletCall.enqueue(new Callback<WalletSummaryResponse>()
        {
            @Override
            public void onResponse(Response<WalletSummaryResponse> response, Retrofit retrofit)
            {
                if (response.body() != null && response.body().isSuccess())
                {
                    savePrefrence(DataSyncIntentService.this,
                                  WALLET_AVAILABLE_AMOUNT,
                                  response.body().getAmount());
                }
            }

            @Override
            public void onFailure(Throwable t)
            {

            }
        });
    }


}
