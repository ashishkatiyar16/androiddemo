package com.bookmypacket.bmpclub.ui;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.bookmypacket.bmpclub.R;
import com.bookmypacket.bmpclub.adapters.PlacesAutoCompleteAdapter;
import com.bookmypacket.bmpclub.dto.Registration;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;

import java.util.List;
import java.util.Locale;

import static android.text.TextUtils.isEmpty;
import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;
import static com.bookmypacket.bmpclub.R.string.toast_empty;
import static com.bookmypacket.bmpclub.utils.ActionUtils.mandatoryFocusChange;
import static com.bookmypacket.bmpclub.utils.AppConstants.BundleExtraKeys.REGISTRATION_DTO;
import static com.bookmypacket.bmpclub.utils.AppConstants.GooglePlacesParams.BOUNDS_INDIA;

public class RegisterStep2Activity extends BaseActivity
        implements GoogleApiClient.ConnectionCallbacks {
    private TextView                  btnCongtinue;
    private TextInputLayout           tilAddressLine1;
    private EditText                  etAddressLine1;
    private TextInputLayout           tilAddressLine2;
    private EditText                  etAddressLine2;
    private TextInputLayout           tilPincode;
    private EditText                  etPincode;
    private TextInputLayout           tilCity;
    private AutoCompleteTextView      etCity;
    private TextInputLayout           tilState;
    private EditText                  etState;
    private PlacesAutoCompleteAdapter placesAutoCompleteAdapter;
    private ResultCallback<PlaceBuffer> cityUpdateResultCallBack = new ResultCallback<PlaceBuffer>()
    {
        @Override
        public void onResult(PlaceBuffer places)
        {
            if (!places.getStatus().isSuccess())
            {
                places.release();
                makeText(getApplicationContext(), R.string.toast_google_location,
                         LENGTH_LONG).show();
            } else
            {
                Place p = places.get(0);
                etCity.setText(p.getName());
                places.release();
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        registerFab();
        initIds();
        setActions();
        addValidations();
        registerGoogleClient(this);
        placesAutoCompleteAdapter =
                new PlacesAutoCompleteAdapter(this, mGoogleApiClient, BOUNDS_INDIA, null);
        etCity.setAdapter(placesAutoCompleteAdapter);
        etCity.setThreshold(3);
    }

    private void setActions()
    {
        btnCongtinue.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (validForm())
                {
                    Registration registration = createRegistration();
                    Intent i =
                            new Intent(RegisterStep2Activity.this, RegisterStepPincodes.class);
                    i.putExtra(REGISTRATION_DTO, registration);
                    startActivity(i);
                } else
                {
                    makeText(RegisterStep2Activity.this, toast_empty, LENGTH_LONG).show();
                }
            }
        });
        etCity.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
//                AutocompletePrediction prediction = placesAutoCompleteAdapter.getItem(position);
//                PendingResult<PlaceBuffer> placeBuffer = Places.GeoDataApi.getPlaceById(mGoogleApiClient, prediction.getPlaceId());
//                placeBuffer.setResultCallback(cityUpdateResultCallBack);
            }
        });
    }

    private void initIds() {
        btnCongtinue = (TextView) findViewById(R.id.btn_continue);
        tilAddressLine1 = (TextInputLayout) findViewById(R.id.til_address1);
        etAddressLine1 = (EditText) findViewById(R.id.et_address1);
        tilAddressLine2 = (TextInputLayout) findViewById(R.id.til_address2);
        etAddressLine2 = (EditText) findViewById(R.id.et_address2);
        tilPincode = (TextInputLayout) findViewById(R.id.til_pincode);
        etPincode = (EditText) findViewById(R.id.et_pincode);
        tilCity = (TextInputLayout) findViewById(R.id.til_city);
        etCity = (AutoCompleteTextView) findViewById(R.id.et_city);
        tilState = (TextInputLayout) findViewById(R.id.til_state);
        etState = (EditText) findViewById(R.id.et_state);
    }

    private Registration createRegistration() {
        Registration reg = (Registration) getIntent().getSerializableExtra(REGISTRATION_DTO);
        reg.setAddressLine1(etAddressLine1.getText().toString());
        reg.setAddressLine2(etAddressLine2.getText().toString());
        reg.setCity(etCity.getText().toString());
        reg.setState(etState.getText().toString());
        reg.setPinCode(etPincode.getText().toString());
        return reg;
    }

    private void addValidations() {
        mandatoryFocusChange(etAddressLine1, tilAddressLine1, getString(R.string.error_address1));
        mandatoryFocusChange(etAddressLine2, tilAddressLine2, getString(R.string.error_address2));
        mandatoryFocusChange(etCity, tilCity, getString(R.string.error_city));
        mandatoryFocusChange(etState, tilState, getString(R.string.error_state));
        mandatoryFocusChange(etPincode, tilPincode, getString(R.string.error_pincode));
    }

    private boolean validForm() {
        boolean allowed = true;
        if (isEmpty(etAddressLine1.getText()) || isEmpty(etAddressLine2.getText()) ||
                isEmpty(etCity.getText()) || isEmpty(etPincode.getText()) ||
                isEmpty(etState.getText())) {
            allowed = false;
        }
        return allowed;
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (lastLocation != null) {
            // Determine whether a Geocoder is available.
            if (!Geocoder.isPresent()) {
                makeText(this, R.string.no_geocoder_available, LENGTH_LONG).show();
                return;
            }
            decodeLocation(lastLocation);

        }
    }

    private void decodeLocation(final Location lastLocation) {
        AsyncTask<Location, Void, Address> task = new AsyncTask<Location, Void, Address>() {
            @Override
            protected Address doInBackground(Location... locations) {
                Address address = null;
                Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(lastLocation.getLatitude(),
                            lastLocation.getLongitude(), 1);
                } catch (Exception ioException) {

                }

                if (addresses.size() != 0) {
                    address = addresses.get(0);
                }
                return address;
            }

            @Override
            protected void onPostExecute(Address address) {
                if (address != null) {
                    updateUIWidgets(address);
                }
                super.onPostExecute(address);
            }
        };
        task.execute(lastLocation);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    private void updateUIWidgets(final Address address) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                etState.setText(address.getAdminArea());
                etCity.setText(address.getLocality());
                etPincode.setText(address.getPostalCode());
            }
        });
    }
}
