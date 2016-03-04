package com.bookmypacket.bmpclub;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bookmypacket.bmpclub.ui.BMPJobsActivity;
import com.bookmypacket.bmpclub.ui.BMPMobileNumberActivity;
import com.bookmypacket.bmpclub.ui.BMPWebViewActivity;
import com.bookmypacket.bmpclub.ui.RegisterStep1Activity;
import com.bookmypacket.bmpclub.utils.SharedPrefrenceManager;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;

import static android.graphics.Paint.UNDERLINE_TEXT_FLAG;
import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;
import static com.bookmypacket.bmpclub.R.id.tv_privacy;
import static com.bookmypacket.bmpclub.R.id.tv_tnc;
import static com.bookmypacket.bmpclub.utils.AppConstants.BundleTags.CONTAINER_URL;
import static com.bookmypacket.bmpclub.utils.AppConstants.Contants.PRIVACY_URL;
import static com.bookmypacket.bmpclub.utils.AppConstants.Contants.TNC_URL;
import static com.google.android.gms.common.ConnectionResult.SERVICE_DISABLED;
import static com.google.android.gms.common.ConnectionResult.SERVICE_MISSING;
import static com.google.android.gms.common.ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED;

public class BMPSplash extends AppCompatActivity
{

    private TextView tvTnC;
    private TextView tvPP;
    private TextView signInBtn;
    private TextView signupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmpsplash);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        String header = SharedPrefrenceManager.getAuthHeader(this);
        if (TextUtils.isEmpty(header))
        {
            signInBtn = (TextView) findViewById(R.id.signin_btn);
            signupBtn = (TextView) findViewById(R.id.signup_btn);
            tvTnC = (TextView) findViewById(tv_tnc);
            tvTnC.setPaintFlags(UNDERLINE_TEXT_FLAG);

            tvPP = (TextView) findViewById(tv_privacy);
            tvPP.setPaintFlags(UNDERLINE_TEXT_FLAG);
            setActions();
        } else
        {
            Intent i = new Intent(this, BMPJobsActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }
    }

    private void setActions()
    {
        tvTnC.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                loadUrl(TNC_URL);
            }
        });
        tvPP.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                loadUrl(PRIVACY_URL);
            }
        });
        signInBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(BMPSplash.this, BMPMobileNumberActivity.class);
                startActivity(i);
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
        signupBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                LocationManager lm = (LocationManager) getApplicationContext().getSystemService(
                        Context.LOCATION_SERVICE);
                boolean gps_enabled = false;
                try
                {
                    gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    if (gps_enabled)
                    {
                        Intent i = new Intent(BMPSplash.this, RegisterStep1Activity.class);
                        startActivity(i);
                    } else
                    {
                        makeText(getApplicationContext(), R.string.toast_location,
                                 LENGTH_LONG).show();
                    }
                }
                catch (Exception ex)
                {
                    makeText(getApplicationContext(), R.string.json_error,
                             LENGTH_LONG).show();
                }

            }
        });
    }

    private void loadUrl(String url)
    {
        Intent i = new Intent(BMPSplash.this, BMPWebViewActivity.class);
        i.putExtra(CONTAINER_URL, url);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bmpsplash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (status == SERVICE_VERSION_UPDATE_REQUIRED || status == SERVICE_MISSING ||
                status == SERVICE_DISABLED)
        {
            GoogleApiAvailability.getInstance().getErrorDialog(this, status, 100).show();
        }
    }

}
