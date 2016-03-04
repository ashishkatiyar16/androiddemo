package com.bookmypacket.bmpclub.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bookmypacket.bmpclub.R;
import com.bookmypacket.bmpclub.dto.WalletWithdrawRequest;
import com.bookmypacket.bmpclub.dto.WalletWithdrawResponse;
import com.bookmypacket.bmpclub.utils.SharedPrefrenceManager;
import com.bookmypacket.bmpclub.utils.retro.ServiceGenerator;
import com.bookmypacket.bmpclub.utils.retro.WalletWithdrawService;

import java.util.concurrent.TimeUnit;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static android.text.TextUtils.isEmpty;
import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;
import static com.bookmypacket.bmpclub.utils.AppConstants.RS_SYMBOL;
import static com.bookmypacket.bmpclub.utils.AppConstants.SharedPrefrencesKeys.WALLET_AVAILABLE_AMOUNT;
import static com.bookmypacket.bmpclub.utils.SharedPrefrenceManager.getPrefrence;

public class WithdrawMoneyActivity extends BaseActivity
{
    private TextInputLayout textInputLayout;
    private EditText        editText;
    private TextView        btnContinue;
    private Handler handler = new Handler();
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_money);
        initIds();
        addActions();
    }

    private void initIds()
    {
        textInputLayout = (TextInputLayout) findViewById(R.id.til_with_amount);
        editText = (EditText) findViewById(R.id.et_with_amount);
        radioGroup = (RadioGroup) findViewById(R.id.rg_mode);
        btnContinue = (TextView) findViewById(R.id.btn_continue);
    }

    private void addActions()
    {
        btnContinue.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (validateAmount())
                {
                    withdrawMoney();
                }
            }
        });

    }

    private void withdrawMoney()
    {
        progressDialog.setMessage(getString(R.string.pd_wallet_withdraw));
        progressDialog.show();
        String                authHeader = SharedPrefrenceManager.getAuthHeader(this);
        String                mobileNo   = SharedPrefrenceManager.getMobileNo(this);
        WalletWithdrawRequest request    = new WalletWithdrawRequest();
        request.setMobileNo(mobileNo);
        request.setAmount(editText.getText().toString());
        WalletWithdrawService service =
                ServiceGenerator.createService(WalletWithdrawService.class);
        Call<WalletWithdrawResponse> call = service.deduct(authHeader, request);
        call.enqueue(new Callback<WalletWithdrawResponse>()
        {
            @Override
            public void onResponse(Response<WalletWithdrawResponse> response, Retrofit retrofit)
            {
                progressDialog.dismiss();
                if (response.body() != null && response.body().getSuccess())
                {
                    syncData();
                    finish();
                } else if (response.body() != null && !response.body().getSuccess())
                {
                    makeText(WithdrawMoneyActivity.this, response.body().getErrorMessage(),
                             LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Throwable t)
            {
                progressDialog.dismiss();
                makeText(WithdrawMoneyActivity.this, R.string.json_error,
                         LENGTH_LONG).show();
            }
        });

    }

    private boolean validateAmount()
    {
        boolean valid         = true;
        int     allowedAmount = (int) getAmount(getPrefrence(this, WALLET_AVAILABLE_AMOUNT));
        int askedAmount = (int) getAmount(editText.getText().toString());
        if (askedAmount > allowedAmount)
        {
            valid = false;
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError(
                    getString(R.string.error_withdrawal_max) + RS_SYMBOL + allowedAmount);
            handler.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError(null);
                }
            }, TimeUnit.SECONDS.toMillis(5));
        }
        return valid;
    }


    private double getAmount(String val)
    {
        if (!isEmpty(val))
        {
            return Double.parseDouble(val);
        } else
        {
            return 0.0;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bmp, menu);
        return true;
    }
}
