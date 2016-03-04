package com.bookmypacket.bmpclub.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bookmypacket.bmpclub.R;
import com.bookmypacket.bmpclub.dto.LoginVerifyRequest;
import com.bookmypacket.bmpclub.dto.LoginVerifyResponse;
import com.bookmypacket.bmpclub.dto.MobileVerificationRequest;
import com.bookmypacket.bmpclub.dto.RegistrationResponse;
import com.bookmypacket.bmpclub.utils.SharedPrefrenceManager;
import com.bookmypacket.bmpclub.utils.retro.LoginVerification;
import com.bookmypacket.bmpclub.utils.retro.RegistrationLoginService;
import com.bookmypacket.bmpclub.utils.retro.ServiceGenerator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;
import static com.bookmypacket.bmpclub.R.color.colorPrimary;
import static com.bookmypacket.bmpclub.R.drawable.back;
import static com.bookmypacket.bmpclub.R.layout.activity_mobile_verify;
import static com.bookmypacket.bmpclub.R.string.json_error;
import static com.bookmypacket.bmpclub.R.string.title_activity_mobile_number;
import static com.bookmypacket.bmpclub.utils.AppConstants.AUTH_HEADER_VALUE;
import static com.bookmypacket.bmpclub.utils.AppConstants.BundleExtraKeys.REGISTRATION_DTO;
import static com.bookmypacket.bmpclub.utils.AppConstants.BundleExtraKeys.REGISTRATION_RESPONSE_DTO;
import static com.bookmypacket.bmpclub.utils.AppConstants.BundleTags.MOBILE_NUMBER;
import static com.bookmypacket.bmpclub.utils.AppConstants.Headers.AUTH_HEADER_KEY;
import static com.bookmypacket.bmpclub.utils.AppConstants.RegistrationValues.OTP_VERIFICATION_STEP_NAME;
import static com.bookmypacket.bmpclub.utils.AppConstants.UserConstants.MOBILE_NUMBER_SEPERATOR;

/**
 * Created by Manish on 26-11-2015.
 */
public class MobileNumberVerifyActivity extends BaseActivity
{
    private String      phoneNumber;
    private TextView    phoneNumberView;
    private EditText    editText;
    private boolean     login;
    private SMSReceiver listener;
    private View        btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(activity_mobile_verify);
        // Set up the login form.
        toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        initToolBar(0, title_activity_mobile_number);
        initViews();
        phoneNumber = getIntent().getStringExtra(MOBILE_NUMBER);
        RegistrationResponse response =
                (RegistrationResponse) getIntent().getSerializableExtra(REGISTRATION_RESPONSE_DTO);
        if (response == null)
        {
            login = true;
        } else
        {
            phoneNumber = response.getProfileId();
            login = false;
        }
        populateData();
        registerReceiver();
        addAction();

    }

    private void initViews()
    {
        phoneNumberView = (TextView) findViewById(R.id.textPhoneNo);
        editText = (EditText) findViewById(R.id.editVerifyCOde);
        btnDone = findViewById(R.id.linearNextPage);
    }

    private void addAction()
    {
        btnDone.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                launchScreen(view);
            }
        });
    }

    private void populateData()
    {
        String phoneText = getResources().getString(R.string.country_code_india) +
                MOBILE_NUMBER_SEPERATOR + phoneNumber;
        phoneNumberView.setText(phoneText);
    }

    @Override
    protected void initToolBar(int colorCode, int title)
    {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(back);
        getSupportActionBar().setHomeButtonEnabled(true);
        setStatusBarColor(ContextCompat.getColor(MobileNumberVerifyActivity.this, colorPrimary));
        toolbar.setTitleTextColor(
                ContextCompat.getColor(MobileNumberVerifyActivity.this, colorPrimary));
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onBackPressed();
            }
        });
    }


    public void launchScreen(View v)
    {
        progressDialog.setMessage(getString(R.string.pd_wait));
        progressDialog.show();
        if (login)
        {
            launchLogin();
        } else
        {
            launchRegister();
        }
    }

    private void launchLogin()
    {
        LoginVerifyRequest loginRequest = new LoginVerifyRequest();
        loginRequest.setMobileNo(phoneNumber);
        loginRequest.setDeviceId(getDeviceId());
        loginRequest.setOtpCode(editText.getText().toString().trim());
        LoginVerification loginService =
                ServiceGenerator.createService(LoginVerification.class);
        Call<LoginVerifyResponse> responseCall = loginService.loginUser(loginRequest);
        responseCall.enqueue(new Callback<LoginVerifyResponse>()
        {
            @Override
            public void onResponse(Response<LoginVerifyResponse> response, Retrofit retrofit)
            {
                progressDialog.dismiss();

                if (response.body() != null && response.body().isSuccess())
                {
                    LoginVerifyResponse resp = response.body();
                    AUTH_HEADER_VALUE = response.headers().get(AUTH_HEADER_KEY);
                    SharedPrefrenceManager.saveAuthHeader(MobileNumberVerifyActivity.this);
                    SharedPrefrenceManager.saveProfile(MobileNumberVerifyActivity.this, resp);
                    Intent i = new Intent(MobileNumberVerifyActivity.this, BMPJobsActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    getPushToken();
                    syncData();
                    syncProfile();
                } else if (response.body() != null)
                {
                    makeText(MobileNumberVerifyActivity.this, response.body().getErrorMessage(),
                             LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Throwable t)
            {
                progressDialog.dismiss();
            }
        });
    }

    private void launchRegister()
    {
        RegistrationLoginService registrationService =
                ServiceGenerator.createService(RegistrationLoginService.class);
        MobileVerificationRequest request = new MobileVerificationRequest();
        request.setStepName(OTP_VERIFICATION_STEP_NAME);
        request.setDeviceId(getDeviceId());

        request.setMobileNo(phoneNumber);
        request.setOtpCode(editText.getText().toString());
        Call<RegistrationResponse> responseCall = registrationService.register(request);
        responseCall.enqueue(new Callback<RegistrationResponse>()
        {
            @Override
            public void onResponse(Response<RegistrationResponse> response, Retrofit retrofit)
            {
                progressDialog.dismiss();
                RegistrationResponse resp = response.body();
                if (resp != null && resp.getSuccess())
                {
                    Intent i = new Intent(MobileNumberVerifyActivity.this, RegisterStep6Activity
                            .class);
                    i.putExtra(REGISTRATION_RESPONSE_DTO, resp);
                    i.putExtra(REGISTRATION_DTO,
                               getIntent().getSerializableExtra(REGISTRATION_DTO));
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                } else if (resp != null && !resp.getSuccess())
                {
                    makeText(MobileNumberVerifyActivity.this, resp.getErrorMessage(),
                             LENGTH_LONG).show();
                } else
                {
                    makeText(MobileNumberVerifyActivity.this, json_error, LENGTH_LONG)
                            .show();
                }
            }

            @Override
            public void onFailure(Throwable t)
            {
                progressDialog.dismiss();
                makeText(MobileNumberVerifyActivity.this, json_error, LENGTH_LONG).show();
            }
        });
    }


    private void registerReceiver()
    {

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        if (listener == null)
        {
            listener = new SMSReceiver();
        }
        registerReceiver(listener, filter);

    }


    private void unRegisterReceiver()
    {
        if (listener != null)
        {
            unregisterReceiver(listener);
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        unRegisterReceiver();
    }

    private class SMSReceiver extends BroadcastReceiver
    {
        private final String  TAG          = "SMSLIstener";
        private final Pattern CODE_PATTERN = Pattern.compile("\\d{5}");

        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED"))
            {
                Log.d(TAG, "Msg Rec");
                Bundle bundle =
                        intent.getExtras();           //---get the SMS message passed in---
                SmsMessage[] msgs = null;
                String msg_from;
                if (bundle != null)
                {
                    //---retrieve the SMS message received---
                    try
                    {
                        Object[] pdus = (Object[]) bundle.get("pdus");
                        //msgs = new SmsMessage[pdus.length];
                        Log.d(TAG, "Pdus Lenght" + pdus.length);
                        for (int i = 0; i < pdus.length; i++)
                        {
                            Log.d(TAG, "Pdus Lenght" + pdus.length + ", i=" + i);
                            SmsMessage msg = SmsMessage.createFromPdu((byte[]) pdus[i]);
                            msg_from = msg.getOriginatingAddress();
                            String msgBody = msg.getMessageBody().toLowerCase();
                            String sender = msg.getDisplayOriginatingAddress();
                            Log.d(TAG, "" + msgBody);

//
                            if (sender.contains("BMPEXP"))
                            {
                                Log.d(TAG, "Contains BMP");
//                                String[] splittedString = msgBody.split(Pattern.quote("."));
                                Matcher m = CODE_PATTERN.matcher(msgBody);
                                m.find();
                                String activationCode = m.group(0);
//                                String[] splittedString =
//                                        msgBody.split("bmp verification code is ");
//                                splittedString = splittedString[1].trim().split("\\s");
//                                String activationCode = splittedString[0];
                                Log.d(TAG, "" + activationCode);
                                Log.d(TAG, "Code:" + activationCode);
                                if (!TextUtils.isEmpty(activationCode))
                                {
                                    editText.setText(activationCode);
                                    launchScreen(editText);
                                    //handler.post(run);
                                }
                                break;
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        Log.d(TAG, "Exception :" + e.getMessage());
                    }
                }
            }
        }
    }
}
