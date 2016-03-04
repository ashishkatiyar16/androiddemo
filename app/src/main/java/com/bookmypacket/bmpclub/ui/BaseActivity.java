package com.bookmypacket.bmpclub.ui;


import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.apptentive.android.sdk.Apptentive;
import com.bookmypacket.bmpclub.BMPSplash;
import com.bookmypacket.bmpclub.DataSyncIntentService;
import com.bookmypacket.bmpclub.R;
import com.bookmypacket.bmpclub.gcm.RegistrationIntentService;
import com.bookmypacket.bmpclub.observer.NetworkObserver;
import com.bookmypacket.bmpclub.utils.SharedPrefrenceManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;

import static android.location.LocationManager.GPS_PROVIDER;
import static android.os.Build.VERSION.SDK_INT;
import static android.provider.Settings.Secure.ANDROID_ID;
import static android.view.WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS;
import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;
import static com.bookmypacket.bmpclub.R.color.white;
import static com.bookmypacket.bmpclub.utils.AppConstants.BundleTags.CONTAINER_URL;
import static com.bookmypacket.bmpclub.utils.AppConstants.Contants.PRIVACY_URL;
import static com.bookmypacket.bmpclub.utils.AppConstants.Contants.TNC_URL;
import static com.bookmypacket.bmpclub.utils.net.NetworkUtil.getInstance;
import static com.google.android.gms.location.LocationServices.API;
import static com.google.android.gms.location.places.Places.GEO_DATA_API;

/**
 * Created by Manish on 26-11-2015.
 */
public class BaseActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener
{
    // protected final String DEVICE_ID = Secure.getString(getContentResolver(), ANDROID_ID);
    protected Toolbar         toolbar;
    protected RequestQueue    requestQueue;
    protected ProgressDialog  progressDialog;
    protected GoogleApiClient mGoogleApiClient;
    private   NetworkObserver listener;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestQueue = getInstance(getCacheDir());
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Wait...");
        registerNetworkObserver();
//        registerGoogleClient(new GoogleApiClient.ConnectionCallbacks()
//        {
//            @Override
//            public void onConnected(Bundle bundle)
//            {
//                location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//            }
//
//            @Override
//            public void onConnectionSuspended(int i)
//            {
//                mGoogleApiClient.connect();
//            }
//        });
    }

    protected void initToolBar(int colorCode, int title)
    {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setBackgroundColor(colorCode);
        setStatusBarColor(colorCode);
        toolbar.setTitleTextColor(ContextCompat.getColor(BaseActivity.this, white));
    }

    protected void registerGoogleClient(GoogleApiClient.ConnectionCallbacks callbacks)
    {
        if (mGoogleApiClient == null)
        {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(callbacks)
                    .addOnConnectionFailedListener(this)
                    .addApi(API)
                    .addApi(GEO_DATA_API)
                    .build();
        }
    }

    protected void setStatusBarColor(int color)
    {
        if (SDK_INT >= 21)
        {
            Window window = getWindow();
            window.addFlags(FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(color);
        }
    }

    protected void registerFab()
    {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Apptentive.showMessageCenter(BaseActivity.this);
            }
        });
    }

    @Override
    protected void onStart()
    {
        Apptentive.onStart(this);
        if (mGoogleApiClient != null)
        {
            mGoogleApiClient.connect();
        }
        super.onStart();
    }

    @Override
    protected void onStop()
    {
        if (mGoogleApiClient != null)
        {
            mGoogleApiClient.disconnect();
        }
        Apptentive.onStop(this);
        super.onStop();

    }

    protected String getDeviceId()
    {
        return Secure.getString(getContentResolver(), ANDROID_ID);
    }

    protected void loadUrl(String url)
    {
        Intent i = new Intent(BaseActivity.this, BMPWebViewActivity.class);
        i.putExtra(CONTAINER_URL, url);
        startActivity(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int     id      = item.getItemId();
        boolean handled = false;
        //noinspection SimplifiableIfStatement
        switch (id)
        {
            case R.id.action_privacy:
                handled = true;
                loadUrl(PRIVACY_URL);
                break;
            case R.id.action_tnc:
                handled = true;
                loadUrl(TNC_URL);
                break;
            case R.id.action_logout:
                handled = true;
                logout();
                break;
        }
        return handled;
//        if (id == R.id.action_settings)
//        {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
    }

    private void logout()
    {
        SharedPrefrenceManager.clearData(BaseActivity.this);
        Intent intent = new Intent(BaseActivity.this, BMPSplash.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    protected void getPushToken()
    {
        Intent i = new Intent(this, RegistrationIntentService.class);
        startService(i);
    }

    protected void syncData()
    {
        Intent i = new Intent(this, DataSyncIntentService.class);
        startService(i);
    }

    protected void syncProfile()
    {
//        Intent i = new Intent(this, UserProfileIntentService.class);
//        startService(i);
    }

    private void registerNetworkObserver()
    {
        try
        {
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            if (listener == null)
            {
                listener = new NetworkObserver();
            }
            registerReceiver(listener, filter);
        }
        catch (Throwable t)
        {

        }

    }

    private void unregisterNetworkObserver()
    {
        try
        {
            if (listener != null)
            {
                unregisterReceiver(listener);
            }
        }
        catch (Throwable t)
        {

        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        unregisterNetworkObserver();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        unregisterNetworkObserver();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        registerNetworkObserver();
    }

    public ProgressDialog getProgressDialog()
    {
        return progressDialog;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {

    }

//    public Location getLocation()
//    {
//        LocationManager lm = (LocationManager) getApplicationContext().getSystemService(
//                Context.LOCATION_SERVICE);
//        boolean gps_enabled = false;
//        try
//        {
//            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
//            if (gps_enabled)
//            {
//                return location;
//            } else
//            {
//                makeText(getApplicationContext(), R.string.toast_location_update,
//                         LENGTH_LONG).show();
//                return null;
//            }
//        }
//        catch (Exception ex)
//        {
//            makeText(getApplicationContext(), R.string.json_error,
//                     LENGTH_LONG).show();
//        }
//        return null;
//    }

    public Location getLocation()
    {
        LocationManager lm = (LocationManager) getApplicationContext().getSystemService(
                Context.LOCATION_SERVICE);
        boolean gps_enabled;
        try
        {
            gps_enabled = lm.isProviderEnabled(GPS_PROVIDER);
            if (!gps_enabled)
            {
                return null;
            } else
            {
                location = lm.getLastKnownLocation(GPS_PROVIDER);
                return location;
            }
        }
        catch (SecurityException ex)
        {
            makeText(getApplicationContext(), R.string.json_error,
                     LENGTH_LONG).show();
        }
        return null;
    }
    public void showPopup(final String title, final String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setTitle(title);
        builder.setPositiveButton(R.string.apptentive_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });



          // Create the AlertDialog
        AlertDialog dialog = builder.create();


    }
    public void showToast(String message) {
        makeText(this, message, LENGTH_LONG).show();
    }
}
