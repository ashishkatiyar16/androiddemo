package com.bookmypacket.bmpclub.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.bookmypacket.bmpclub.R;
import com.bookmypacket.bmpclub.dto.DepositCashRequest;
import com.bookmypacket.bmpclub.dto.DepositResponse;
import com.bookmypacket.bmpclub.dto.LoginVerifyResponse;
import com.bookmypacket.bmpclub.utils.AppConstants;
import com.bookmypacket.bmpclub.utils.retro.DepositAmountService;
import com.bookmypacket.bmpclub.utils.retro.ServiceGenerator;
import com.ebs.android.sdk.Config;
import com.ebs.android.sdk.EBSPayment;
import com.ebs.android.sdk.PaymentRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static android.text.TextUtils.isEmpty;
import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;
import static com.bookmypacket.bmpclub.utils.AppConstants.RegistrationValues.PAYMENT_MODE_OFFLINE;
import static com.bookmypacket.bmpclub.utils.AppConstants.RegistrationValues.PAYMENT_MODE_ONLINE;
import static com.bookmypacket.bmpclub.utils.SharedPrefrenceManager.getAuthHeader;
import static com.bookmypacket.bmpclub.utils.SharedPrefrenceManager.getProfile;
import static java.lang.Double.parseDouble;

public class WalletTopupActivity extends BaseActivity
{
    private EditText        depositAmount;
    private TextInputLayout depositTil;
    private View            btnCash;
    private View            btnEBS;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_topup);
        depositAmount = (EditText) findViewById(R.id.et_deposit_amount);
        depositTil = (TextInputLayout) findViewById(R.id.til_deposit_amount);
        btnCash = findViewById(R.id.btn_cash);
        btnEBS = findViewById(R.id.btn_ebs);
        addActions();


    }

    private void updateTransaction(String payment)
    {
        progressDialog.setMessage("Wait...");
        progressDialog.show();
        String              authHeader  = getAuthHeader(this);
        LoginVerifyResponse userProfile = getProfile(this);
        DepositCashRequest  request     = new DepositCashRequest();
        request.setAmount(depositAmount.getText().toString());
        request.setMobileNo(userProfile.getMobileNo());
        if (payment == null)
        {
            request.setCouponCode(PAYMENT_MODE_OFFLINE);
            request.setModeOfPayment(PAYMENT_MODE_OFFLINE);
        } else
        {
            request.setCouponCode(PAYMENT_MODE_ONLINE);
            request.setModeOfPayment(PAYMENT_MODE_ONLINE);
            request.setEbsResponseMessage(transactionId(payment));
        }
        request.setTransactionId(userProfile.getMobileNo() + System.currentTimeMillis());
        DepositAmountService  service = ServiceGenerator.createService(DepositAmountService.class);
        Call<DepositResponse> call    = service.deposit(authHeader, request);
        call.enqueue(new Callback<DepositResponse>()
        {
            @Override
            public void onResponse(Response<DepositResponse> response, Retrofit retrofit)
            {
                progressDialog.dismiss();
                if (response.body() != null && response.body().isSuccess())
                {
                    makeText(WalletTopupActivity.this, R.string.payment_success, LENGTH_LONG)
                            .show();
                    Handler h = new Handler();
                    h.postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            finish();
                        }
                    }, LENGTH_LONG);
                } else
                {
                    makeText(WalletTopupActivity.this, response.body().getErrorMessage(),
                             LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Throwable t)
            {
                progressDialog.dismiss();
                makeText(WalletTopupActivity.this, R.string.json_error, LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        if (intent != null)
        {
            String payment_id = intent.getStringExtra("payment_id");
            if (payment_id != null)
            {
                updateTransaction(payment_id);
            }
        }
        super.onNewIntent(intent);
    }

    private void callEbsKit(LoginVerifyResponse reg)
    {
        /**
         * Set Parameters Before Initializing the EBS Gateway, All mandatory
         * values must be provided
         */

        /** Payment Amount Details */
        // Total Amount

        PaymentRequest.getInstance().setTransactionAmount(
                String.format("%.2f", parseDouble(depositAmount.getText().toString())));

        /** Mandatory */

        PaymentRequest.getInstance().setAccountId(AppConstants.PaymentGateway.ACC_ID);
        PaymentRequest.getInstance().setSecureKey(AppConstants.PaymentGateway.SECRET_KEY);

        // Reference No
        PaymentRequest.getInstance()
                .setReferenceNo(reg.getMobileNo() + String.valueOf(System.currentTimeMillis()));
        /** Mandatory */

        // Email Id
        PaymentRequest.getInstance().setBillingEmail(reg.getEmailId());
        /** Mandatory */

        /**
         * Set failure id as 1 to display amount and reference number on failed
         * transaction page. set 0 to disable
         */
        PaymentRequest.getInstance().setFailureid("0");
        /** Mandatory */

        // Currency
        PaymentRequest.getInstance().setCurrency("INR");
        /** Mandatory */

        /** Optional */
        // Your Reference No or Order Id for this transaction
        PaymentRequest.getInstance().setTransactionDescription(
                "BMP Security Amount");

        /** Billing Details */
        PaymentRequest.getInstance().setBillingName(reg.getFirstName() + " " + reg.getLastName());
        /** Optional */
        PaymentRequest.getInstance().setBillingAddress(
                reg.getAddressLine1() + "," + reg.getAddressLine2());
        /** Optional */
        PaymentRequest.getInstance().setBillingCity(reg.getCity());
        /** Optional */
        PaymentRequest.getInstance().setBillingPostalCode(reg.getPinCode());
        /** Optional */
        PaymentRequest.getInstance().setBillingState(reg.getState());
        /** Optional */
        PaymentRequest.getInstance().setBillingCountry("IND");
        // ** Optional */
        PaymentRequest.getInstance().setBillingPhone(reg.getMobileNo());
        /** Optional */
        /** set custom message for failed transaction */

        PaymentRequest.getInstance().setFailuremessage(
                getResources().getString(R.string.payment_failure_message));
        /** Optional */
        /** Shipping Details */
        PaymentRequest.getInstance().setShippingName(reg.getFirstName() + " " + reg.getLastName());
        /** Optional */
        PaymentRequest.getInstance()
                .setShippingAddress(reg.getAddressLine1() + "," + reg.getAddressLine2());
        /** Optional */
        PaymentRequest.getInstance().setShippingCity(reg.getCity());
        /** Optional */
        PaymentRequest.getInstance().setShippingPostalCode(reg.getPinCode());
        /** Optional */
        PaymentRequest.getInstance().setShippingState(reg.getState());
        /** Optional */
        PaymentRequest.getInstance().setShippingCountry("IND");
        /** Optional */
        PaymentRequest.getInstance().setShippingEmail("test@testmail.com");
        /** Optional */
        PaymentRequest.getInstance().setShippingPhone(reg.getMobileNo());
        /** Optional */
        /* enable log by setting 1 and disable by setting 0 */
        PaymentRequest.getInstance().setLogEnabled("1");

        /**
         * Initialise parameters for dyanmic values sending from merchant custom
         * values from merchant
         */

        ArrayList<HashMap<String, String>> custom_post_parameters =
                new ArrayList<HashMap<String, String>>();
        HashMap<String, String> hashpostvalues = new HashMap<String, String>();
        hashpostvalues.put("account_details", reg.getTaxationId());
        hashpostvalues.put("merchant_type", "gold");
        custom_post_parameters.add(hashpostvalues);

        PaymentRequest.getInstance()
                .setCustomPostValues(custom_post_parameters);
        /** Optional-Set dyanamic values */

        // PaymentRequest.getInstance().setFailuremessage(getResources().getString(R.string.payment_failure_message));

        EBSPayment.getInstance()
                .init(WalletTopupActivity.this, AppConstants.PaymentGateway.ACC_ID,
                      AppConstants.PaymentGateway.SECRET_KEY,
                      Config.Mode.ENV_TEST, Config.Encryption.ALGORITHM_MD5,
                      getString(R.string.hostname_topup));

        // EBSPayment.getInstance().init(context, accId, secretkey, environment,
        // algorithm, host_name);

    }


    private String transactionId(String ebsJson)
    {
        JSONObject jObject;
        try
        {
            jObject = new JSONObject(ebsJson);
            String txId = jObject.getString("MerchantRefNo");
            return txId;
        }
        catch (JSONException ex)
        {
            return null;
        }
    }

    private void addActions()
    {
        btnEBS.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (validForm())
                {
                    callEbsKit(getProfile(WalletTopupActivity.this));
                }
            }
        });
        btnCash.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (validForm())
                {
                    updateTransaction(null);
                }
            }
        });
        depositAmount.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                if (!isEmpty(depositAmount.getText()) &&
                        parseDouble(depositAmount.getText().toString()) >= 500)
                {
                    depositTil.setError(null);
                    depositTil.setErrorEnabled(false);
                }
            }
        });
    }

    private boolean validForm()
    {
        boolean valid = true;
        if (isEmpty(depositAmount.getText()))
        {
            valid = false;
        } else
        {
            double amount = parseDouble(depositAmount.getText().toString());
            if (amount < 500)
            {
                depositTil.setError(getString(R.string.error_til_deposit));
                valid = false;
            }
        }
        return valid;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bmp, menu);
        return true;
    }
}
