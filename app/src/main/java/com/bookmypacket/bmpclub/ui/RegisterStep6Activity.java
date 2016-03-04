package com.bookmypacket.bmpclub.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bookmypacket.bmpclub.R;
import com.bookmypacket.bmpclub.dto.PaymentDetailsRequest;
import com.bookmypacket.bmpclub.dto.PaymentDetailsResponse;
import com.bookmypacket.bmpclub.dto.Registration;
import com.bookmypacket.bmpclub.dto.RegistrationResponse;
import com.bookmypacket.bmpclub.utils.AppConstants;
import com.bookmypacket.bmpclub.utils.retro.RegistrationPaymentService;
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

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;
import static com.bookmypacket.bmpclub.utils.AppConstants.BundleExtraKeys.REGISTRATION_DTO;
import static com.bookmypacket.bmpclub.utils.AppConstants.BundleExtraKeys.REGISTRATION_RESPONSE_DTO;
import static com.bookmypacket.bmpclub.utils.AppConstants.RegistrationValues.PAYMENT_MODE_OFFLINE;
import static com.bookmypacket.bmpclub.utils.AppConstants.RegistrationValues.PAYMENT_MODE_ONLINE;

public class RegisterStep6Activity extends BaseActivity
{
    private static RegistrationResponse RESPONSE;
    ArrayList<HashMap<String, String>> custom_post_parameters;
    private TextView btnCash;
    private TextView btnEBS;
    private EditText depositAmount;
    private TextInputLayout tilDeposit;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step6);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        registerFab();
        initIds();
        setActions();
        String payment_id = getIntent().getStringExtra("payment_id");
        if (RESPONSE == null)
        {
            RESPONSE =
                    (RegistrationResponse) getIntent().getSerializableExtra(
                            REGISTRATION_RESPONSE_DTO);
        }
        if (payment_id != null)
        {
            Log.i("EBS_PAYMENT", payment_id);
            submitCash(payment_id);
        }
    }

    private void setActions()
    {
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
                if (TextUtils.isEmpty(depositAmount.getText()))
                {
                    return;
                }
                int amount = Integer.parseInt(depositAmount.getText().toString());
                if (amount > 10000)
                {
                    tilDeposit.setError(getResources().getString(R.string.error_security_high));

                } else if (amount < 1000)
                {
                    tilDeposit.setError(getResources().getString(R.string.error_security_low));
                } else
                {
                    tilDeposit.setError(null);
                }
            }
        });
        btnCash.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                submitCash(null);
            }
        });
        btnEBS.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                callEbsKit((Registration) getIntent().getSerializableExtra(REGISTRATION_DTO));
            }
        });
    }

    private void initIds()
    {
        btnCash = (TextView) findViewById(R.id.btn_cash);
        btnEBS = (TextView) findViewById(R.id.btn_ebs);
        depositAmount = (EditText) findViewById(R.id.et_security_amount);
        tilDeposit = (TextInputLayout) findViewById(R.id.til_deposit);
    }

    private void submitCash(String payment)
    {
        progressDialog.setMessage("Wait.....");
        progressDialog.show();
        PaymentDetailsRequest request = new PaymentDetailsRequest();
        request.setAmount(Double.parseDouble(depositAmount.getText().toString()));
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
        request.setTransactionId(String.valueOf(System.currentTimeMillis()));
        request.setMobileNo(RESPONSE.getProfileId());
        RegistrationPaymentService service = ServiceGenerator.createService
                (RegistrationPaymentService.class);
        Call<PaymentDetailsResponse> response = service.register(request);
        response.enqueue(new Callback<PaymentDetailsResponse>()
        {
            @Override
            public void onResponse(Response<PaymentDetailsResponse> response, Retrofit retrofit)
            {
                progressDialog.dismiss();
                PaymentDetailsResponse resp = response.body();
                if (resp != null && resp.getSuccess())
                {
                    Intent i =
                            new Intent(RegisterStep6Activity.this, RegistractionDoneActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.putExtra(REGISTRATION_RESPONSE_DTO, resp);
                    startActivity(i);
                } else
                {
                    Toast.makeText(RegisterStep6Activity.this, resp.getErrorMessage(), Toast
                            .LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Throwable t)
            {
                progressDialog.dismiss();
                makeText(RegisterStep6Activity.this, R.string.json_error,
                         LENGTH_LONG).show();
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
                submitCash(payment_id);
            }
        }
        super.onNewIntent(intent);
    }

    private void callEbsKit(Registration reg)
    {
        /**
         * Set Parameters Before Initializing the EBS Gateway, All mandatory
         * values must be provided
         */

        /** Payment Amount Details */
        // Total Amount

        PaymentRequest.getInstance().setTransactionAmount(
                String.format("%.2f", Double.parseDouble(depositAmount.getText().toString())));

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

        custom_post_parameters = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> hashpostvalues = new HashMap<String, String>();
        hashpostvalues.put("account_details", reg.getTaxationId());
        hashpostvalues.put("merchant_type", "gold");
        custom_post_parameters.add(hashpostvalues);

        PaymentRequest.getInstance()
                .setCustomPostValues(custom_post_parameters);
        /** Optional-Set dyanamic values */

        // PaymentRequest.getInstance().setFailuremessage(getResources().getString(R.string.payment_failure_message));
        try
        {
            EBSPayment.getInstance()
                    .init(RegisterStep6Activity.this, AppConstants.PaymentGateway.ACC_ID,
                          AppConstants.PaymentGateway.SECRET_KEY,
                          Config.Mode.ENV_TEST, Config.Encryption.ALGORITHM_MD5,
                          AppConstants.PaymentGateway.HOST_NAME_REGISTRATION);
        }
        catch (Exception et)
        {
            makeText(getApplicationContext(), R.string.payment_gateway_error, LENGTH_LONG).show();
        }

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


}
