package com.bookmypacket.bmpclub.ui;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bookmypacket.bmpclub.R;
import com.bookmypacket.bmpclub.dto.BMPBranch;
import com.bookmypacket.bmpclub.dto.BranchPincode;
import com.bookmypacket.bmpclub.dto.BranchResponse;
import com.bookmypacket.bmpclub.dto.HubName;
import com.bookmypacket.bmpclub.dto.PinCodesResponse;
import com.bookmypacket.bmpclub.dto.RegisterHubRequest;
import com.bookmypacket.bmpclub.dto.RegisterHubResponse;
import com.bookmypacket.bmpclub.dto.Registration;
import com.bookmypacket.bmpclub.utils.retro.GetBranchListService;
import com.bookmypacket.bmpclub.utils.retro.GetHubListService;
import com.bookmypacket.bmpclub.utils.retro.GetPinCodesService;
import com.bookmypacket.bmpclub.utils.retro.ServiceGenerator;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;
import static com.bookmypacket.bmpclub.utils.ActionUtils.mandatoryFocusChange;
import static com.bookmypacket.bmpclub.utils.AppConstants.BundleExtraKeys.REGISTRATION_DTO;

public class RegisterStepPincodes extends BaseActivity
        implements GoogleApiClient.ConnectionCallbacks
{
    private Spinner         hubList;
    private MultiAutoCompleteTextView deliveryAddress;
    private Spinner                   deliveryBranch;
    private Map<String, List<String>> deliveryPincodes;
    private MultiAutoCompleteTextView pickupAddress;
    private Spinner                   pickupBranch;
    private TextView        btnContinue;
    private String          currentLat;
    private String          currentLong;
    private boolean         isHubList;
    private TextInputLayout tilDelivery;
    private TextInputLayout tilPickup;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step_pincodes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        registerFab();
        initIds();
        isHubList = true;
        registerGoogleClient(this);
        progressDialog.setMessage(getString(R.string.pd_hub_list));
        progressDialog.show();
        addActions();

    }

    private void initIds()
    {
        deliveryAddress = (MultiAutoCompleteTextView) findViewById(R.id.matv_delivery_pin);
        deliveryBranch = (Spinner) findViewById(R.id.sp_delivery_centers);
        deliveryAddress.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        List<String> delivery = new ArrayList<>();
        ArrayAdapter<String> adp = new ArrayAdapter<String>(this,
                                                            android.R.layout.simple_dropdown_item_1line,
                                                            delivery);
        deliveryAddress.setThreshold(1);
        deliveryAddress.setAdapter(adp);

        pickupAddress = (MultiAutoCompleteTextView) findViewById(R.id.matv_pickup_pin);
        pickupBranch = (Spinner) findViewById(R.id.sp_pickup_centers);
        pickupAddress.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        List<String> pickup = new ArrayList<>();
        ArrayAdapter<String> pic = new ArrayAdapter<String>(this,
                                                            android.R.layout.simple_dropdown_item_1line,
                                                            delivery);
        pickupAddress.setThreshold(1);
        pickupAddress.setAdapter(pic);
        hubList = (Spinner) findViewById(R.id.sp_hubs_centers);
        tilDelivery = (TextInputLayout) findViewById(R.id.til_delivery);
        tilPickup = (TextInputLayout) findViewById(R.id.til_pickup);
        btnContinue = (TextView) findViewById(R.id.btn_continue);

    }

    private void addActions()
    {
        mandatoryFocusChange(deliveryAddress, tilDelivery, getString(R.string.error_delivery));
        mandatoryFocusChange(pickupAddress, tilPickup, getString(R.string.error_pickup));
        btnContinue.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (validatePincodes())
                {
                    Intent i = new Intent(RegisterStepPincodes.this, RegisterStep3Activity.class);
                    i.putExtra(REGISTRATION_DTO, createRegistration());
                    startActivity(i);
                }
            }
        });
    }

    @Override
    public void onConnected(Bundle bundle)
    {
       /* Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        currentLong = "" + location.getLongitude();
        currentLat = "" + location.getLatitude();
        */
        currentLong = ""+1.000;
        currentLat = " "+2.00;
        if (isHubList)
        {
            fetchHubList();
        }

    }

    @Override
    public void onConnectionSuspended(int i)
    {

    }

    private void fetchHubList()
    {
        RegisterHubRequest request = new RegisterHubRequest();
        request.setLattitude(currentLat);
        request.setLongitude(currentLong);
        GetHubListService         service = ServiceGenerator.createService(GetHubListService.class);
        Call<RegisterHubResponse> call    = service.register(request);
        call.enqueue(new Callback<RegisterHubResponse>()
        {
            @Override
            public void onResponse(Response<RegisterHubResponse> response, Retrofit retrofit)
            {
                progressDialog.dismiss();
                if (response.body() != null && response.body().isSuccess())
                {
                    renderHubList(response.body().getHubResponseList());
                } else if (response.body() != null)
                {
                    renderToast(response.body().getErrorMessage());
                } else
                {
                    renderToast(getString(R.string.json_error));
                }
            }

            @Override
            public void onFailure(Throwable t)
            {

            }
        });
    }

    private void renderToast(String message)
    {
        makeText(RegisterStepPincodes.this, message, LENGTH_LONG).show();
    }

    private void renderHubList(List<HubName> hubs)
    {
        final List<String> hubArray = new ArrayList<>();
        for (HubName hub : hubs)
        {
            hubArray.add(hub.getHubName());
        }
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                ArrayAdapter<String> hubadap = new ArrayAdapter<String>(RegisterStepPincodes.this,
                                                                        android.R.layout.simple_dropdown_item_1line,
                                                                        hubArray);
                hubList.setAdapter(hubadap);
            }
        });

        hubList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int index, long l)
            {
                String hubName = hubArray.get(index);
                progressDialog.setMessage(getString(R.string.pd_branch_list));
                fetchBranchList(hubName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
    }

    private void fetchBranchList(String hubName)
    {
        HubName request = new HubName();
        request.setHubName(hubName);
        GetBranchListService service = ServiceGenerator.createService(GetBranchListService.class);
        Call<BranchResponse> call    = service.register(request);
        call.enqueue(new Callback<BranchResponse>()
        {
            @Override
            public void onResponse(Response<BranchResponse> response, Retrofit retrofit)
            {
                progressDialog.dismiss();
                if (response.body() != null && response.body().isSuccess())
                {
                    renderBranchList(response.body().getBranchResponseList());
                } else if (response.body() != null)
                {
                    renderToast(response.body().getErrorMessage());
                } else
                {
                    renderToast(getString(R.string.json_error));
                }
            }

            @Override
            public void onFailure(Throwable t)
            {
                progressDialog.dismiss();
            }
        });
    }

    private void renderBranchList(List<BMPBranch> branchResponseList)
    {
        final List<String> branches = new ArrayList<>();
        for (BMPBranch branch : branchResponseList)
        {
            branches.add(branch.getBranchName());
        }

        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                ArrayAdapter<String> adapDelivery =
                        new ArrayAdapter<String>(RegisterStepPincodes.this,
                                                 android.R.layout.simple_dropdown_item_1line,
                                                 branches);
                ArrayAdapter<String> adapPickup =
                        new ArrayAdapter<String>(RegisterStepPincodes.this,
                                                 android.R.layout.simple_dropdown_item_1line,
                                                 branches);
                deliveryBranch.setAdapter(adapDelivery);
                pickupBranch.setAdapter(adapPickup);
            }
        });
        deliveryBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                fetchPincodes(branches.get(i), deliveryAddress);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
                fetchPincodes(branches.get(0), deliveryAddress);
            }
        });
        pickupBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                fetchPincodes(branches.get(i), pickupAddress);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
                fetchPincodes(branches.get(0), pickupAddress);
            }
        });
        fetchPincodes(branches.get(0), deliveryAddress);
        fetchPincodes(branches.get(0), pickupAddress);
    }

    private void fetchPincodes(String s, final MultiAutoCompleteTextView mv)
    {
        progressDialog.setMessage(getString(R.string.pd_pincodes));
        progressDialog.show();
        BMPBranch request = new BMPBranch();
        request.setBranchName(s);
        GetPinCodesService     service = ServiceGenerator.createService(GetPinCodesService.class);
        Call<PinCodesResponse> call    = service.register(request);
        call.enqueue(new Callback<PinCodesResponse>()
        {
            @Override
            public void onResponse(Response<PinCodesResponse> response, Retrofit retrofit)
            {
                progressDialog.dismiss();
                if (response.body() != null && response.body().isSuccess())
                {
                    renderPinCodes(response.body().getBranchPincodeResponseList(), mv);
                } else if (response.body() != null)
                {
                    renderToast(response.body().getErrorMessage());
                } else
                {
                    renderToast(getString(R.string.json_error));
                }
            }

            @Override
            public void onFailure(Throwable t)
            {
                progressDialog.dismiss();
            }
        });
    }

    private void renderPinCodes(List<BranchPincode> branchPincodeResponseList,
                                final MultiAutoCompleteTextView mv)
    {
        final List<String> pincodes = new ArrayList<>();
        for (BranchPincode bp : branchPincodeResponseList)
        {
            pincodes.add(bp.getPincode());
        }
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                ArrayAdapter<String> adp = new ArrayAdapter<String>(RegisterStepPincodes.this,
                                                                    android.R.layout.simple_dropdown_item_1line,
                                                                    pincodes);
                mv.setThreshold(1);
                mv.setAdapter(adp);
            }
        });

    }

    private Registration createRegistration()
    {
        Registration reg = (Registration) getIntent().getSerializableExtra(REGISTRATION_DTO);
        reg.setDeliveryServiceAreas(deliveryAddress.getText().toString().split(","));
        reg.setPickupServiceAreas(pickupAddress.getText().toString().split(","));
        return reg;
    }

    private boolean validatePincodes()
    {
        if (TextUtils.isEmpty(deliveryAddress.getText()))
        {
            return false;
        }
        return !TextUtils.isEmpty(pickupAddress.getText());
    }
}
