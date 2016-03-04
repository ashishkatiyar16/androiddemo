package com.bookmypacket.bmpclub.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bookmypacket.bmpclub.R;
import com.bookmypacket.bmpclub.dto.LoginRequest;
import com.bookmypacket.bmpclub.dto.LoginResponse;
import com.bookmypacket.bmpclub.utils.SharedPrefrenceManager;
import com.bookmypacket.bmpclub.utils.retro.LoginService;
import com.bookmypacket.bmpclub.utils.retro.ServiceGenerator;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static com.bookmypacket.bmpclub.R.color.colorPrimary;
import static com.bookmypacket.bmpclub.R.drawable.back;
import static com.bookmypacket.bmpclub.R.id.app_toolbar;
import static com.bookmypacket.bmpclub.R.id.progressBar1;
import static com.bookmypacket.bmpclub.R.layout.activity_bmpmobile_number;
import static com.bookmypacket.bmpclub.R.string.title_activity_bmpmobile_number;
import static com.bookmypacket.bmpclub.utils.AppConstants.BundleTags.MOBILE_NUMBER;
import static com.bookmypacket.bmpclub.utils.AppConstants.UserConstants.MOBILE_NO_TIMEOUT;

/**
 * A login screen that offers login via email/password.
 */
public class BMPMobileNumberActivity extends BaseActivity
{
    private ImageView progressBar;
    private EditText  phoneNumberView;
    private Handler handler = new Handler();
    private String mobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(activity_bmpmobile_number);
        // Set up the login form.
        toolbar = (Toolbar) findViewById(app_toolbar);
        initToolBar(0, title_activity_bmpmobile_number);
        progressBar = (ImageView) findViewById(progressBar1);
        phoneNumberView = (EditText) findViewById(R.id.editPhoneNo);
        addActions();
    }

    private void addActions()
    {
        phoneNumberView.addTextChangedListener(new TextWatcher()
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
                int charLen = 0;
                if (editable != null)
                {
                    charLen = phoneNumberView.getText().toString().trim().length();
                }
                switch (charLen)
                {
                    case 0:
                        progressBar.setVisibility(GONE);
                        break;
                    case 1:
                        progressBar.setVisibility(VISIBLE);
                        progressBar.setImageResource(R.drawable.p1);
                        break;
                    case 2:
                        progressBar.setImageResource(R.drawable.p2);
                        break;
                    case 3:
                        progressBar.setImageResource(R.drawable.p3);
                        break;
                    case 4:
                        progressBar.setImageResource(R.drawable.p4);
                        break;
                    case 5:
                        progressBar.setImageResource(R.drawable.p5);
                        break;
                    case 6:
                        progressBar.setImageResource(R.drawable.p6);
                        break;
                    case 7:
                        progressBar.setImageResource(R.drawable.p7);
                        break;
                    case 8:
                        progressBar.setImageResource(R.drawable.p8);
                        break;
                    case 9:
                        progressBar.setImageResource(R.drawable.p9);
                        break;
                    case 10:
                        progressBar.setImageResource(R.drawable.checkmark);
                        handler.postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                progressDialog.setMessage("Login.....");
                                progressDialog.show();
                                loadAfter2Sec();
                            }
                        }, MOBILE_NO_TIMEOUT);

                        break;
                }
            }
        });
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
        setStatusBarColor(ContextCompat.getColor(BMPMobileNumberActivity.this, colorPrimary));
        toolbar.setTitleTextColor(
                ContextCompat.getColor(BMPMobileNumberActivity.this, colorPrimary));
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onBackPressed();
            }
        });
    }


    private void loadAfter2Sec()
    {

        LoginRequest request = new LoginRequest();
        request.setDeviceId(getDeviceId());
        request.setMobileNo(phoneNumberView.getText().toString());
        LoginService        service  = ServiceGenerator.createService(LoginService.class);
        Call<LoginResponse> response = service.register(request);
        response.enqueue(new Callback<LoginResponse>()
        {
            @Override
            public void onResponse(Response<LoginResponse> response, Retrofit retrofit)
            {
                try
                {
                    progressDialog.dismiss();
                    if (response.body().getErrorMessage() != null)
                    {
                        makeText(BMPMobileNumberActivity.this, response.body().getErrorMessage(),
                                 LENGTH_LONG).show();
                    } else
                    {
                        processResponse();
                    }
                }
                catch (Throwable t)
                {

                }
            }

            @Override
            public void onFailure(Throwable t)
            {
                progressDialog.dismiss();
                makeText(BMPMobileNumberActivity.this, "Unknow Error", LENGTH_SHORT).show();
            }
        });

    }

    private void processResponse()
    {
        mobileNumber = phoneNumberView.getText().toString().trim();
        SharedPrefrenceManager.saveMobileNo(BMPMobileNumberActivity.this, mobileNumber);
        Intent i = new Intent(BMPMobileNumberActivity.this, MobileNumberVerifyActivity.class);
        i.putExtra(MOBILE_NUMBER, mobileNumber);
        startActivity(i);
    }
}

