package com.bookmypacket.bmpclub.ui;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bookmypacket.bmpclub.R;
import com.bookmypacket.bmpclub.dto.Registration;
import com.bookmypacket.bmpclub.dto.RegistrationResponse;
import com.bookmypacket.bmpclub.utils.net.NetworkRequests;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import static android.provider.Settings.Secure.ANDROID_ID;
import static android.text.TextUtils.isEmpty;
import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;
import static com.android.volley.Request.Method.POST;
import static com.bookmypacket.bmpclub.R.id.btn_continue;
import static com.bookmypacket.bmpclub.R.id.et_acccountNo;
import static com.bookmypacket.bmpclub.R.id.et_bankName;
import static com.bookmypacket.bmpclub.R.id.et_bank_branch;
import static com.bookmypacket.bmpclub.R.id.et_ifsc_code;
import static com.bookmypacket.bmpclub.R.id.et_nameBank;
import static com.bookmypacket.bmpclub.R.id.lo_accountNo;
import static com.bookmypacket.bmpclub.R.id.lo_bankName;
import static com.bookmypacket.bmpclub.R.id.lo_bank_branch;
import static com.bookmypacket.bmpclub.R.id.lo_ifsc_code;
import static com.bookmypacket.bmpclub.R.id.lo_nameBank;
import static com.bookmypacket.bmpclub.R.string.json_error;
import static com.bookmypacket.bmpclub.R.string.toast_empty;
import static com.bookmypacket.bmpclub.utils.ActionUtils.mandatoryFocusChange;
import static com.bookmypacket.bmpclub.utils.AppConstants.BundleExtraKeys.REGISTRATION_DTO;
import static com.bookmypacket.bmpclub.utils.AppConstants.BundleExtraKeys.REGISTRATION_RESPONSE_DTO;
import static com.bookmypacket.bmpclub.utils.AppConstants.NetworkUrls.REGISTRATION_UPLOAD_PROFILE;

public class RegisterStep4Activity extends BaseActivity
{
    private TextView        btnCongtinue;
    private TextInputLayout tilNameInAccount;
    private EditText        etNameInAccount;
    private TextInputLayout tilAccountNo;
    private EditText        etAccountNo;
    private TextInputLayout tilIFSCCode;
    private EditText        etIFSCCode;
    private TextInputLayout tilBankName;
    private Spinner         etBankName;
    private TextInputLayout tilBankBranch;
    private EditText        etBankBranch;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step4);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        registerFab();
        initIds();
        setActions();
        addValidations();
        populateData();
        validateForm();
    }

    private void initIds()
    {
        btnCongtinue = (TextView) findViewById(btn_continue);
        tilNameInAccount = (TextInputLayout) findViewById(lo_nameBank);
        etNameInAccount = (EditText) findViewById(et_nameBank);
        tilAccountNo = (TextInputLayout) findViewById(lo_accountNo);
        etAccountNo = (EditText) findViewById(et_acccountNo);
        tilIFSCCode = (TextInputLayout) findViewById(lo_ifsc_code);
        etIFSCCode = (EditText) findViewById(et_ifsc_code);
        tilBankName = (TextInputLayout) findViewById(lo_bankName);
        etBankName = (Spinner) findViewById(et_bankName);
        tilBankBranch = (TextInputLayout) findViewById(lo_bank_branch);
        etBankBranch = (EditText) findViewById(et_bank_branch);
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
                    initRegistration();
                } else
                {
                    makeText(RegisterStep4Activity.this, toast_empty, LENGTH_LONG).show();
                }
//                Registration registration = createRegistration();
//                Intent i =
//                        new Intent(RegisterStep4Activity.this, RegisterStep5Activity.class);
//                i.putExtra(REGISTRATION_DTO, registration);
//                startActivity(i);
            }
        });
    }

    private Registration createRegistration()
    {
        Registration reg = (Registration) getIntent().getSerializableExtra(REGISTRATION_DTO);
        reg.setNameInBankAccount(etNameInAccount.getText().toString());
        reg.setBanckAccountNo(etAccountNo.getText().toString());
        reg.setIfscCode(etIFSCCode.getText().toString());
        reg.setBankName(etBankName.getSelectedItem().toString());
        reg.setBankCity(etBankBranch.getText().toString());
        return reg;
    }

    private void initRegistration()
    {
        progressDialog.setTitle(R.string.pd_registration_profile_title);
        progressDialog.setMessage(getString(R.string.pd_registration_profile_message));
        progressDialog.show();
        String DEVICE_ID = Secure.getString(getContentResolver(), ANDROID_ID);
        try
        {
            final JSONObject jsonObject =
                    NetworkRequests.createRegistrationRequest1(createRegistration(), DEVICE_ID);
            JsonObjectRequest request =
                    new JsonObjectRequest(POST, REGISTRATION_UPLOAD_PROFILE, jsonObject,
                            new Listener<JSONObject>()
                            {
                                @Override
                                public void onResponse(JSONObject response)
                                {
                                    progressDialog.dismiss();
                                    Gson g = new Gson();
                                    RegistrationResponse res = g.fromJson(
                                            response.toString(),
                                            RegistrationResponse.class);
                                    if (res != null && res.getSuccess())
                                    {
                                        Intent i =
                                                new Intent(RegisterStep4Activity.this,
                                                        RegisterStep5Activity.class);
                                        i.putExtra(REGISTRATION_RESPONSE_DTO, res);
                                        i.putExtra(REGISTRATION_DTO, getIntent()
                                                .getSerializableExtra(
                                                        REGISTRATION_DTO));
                                        startActivity(i);
                                    } else
                                    {
                                        makeText(RegisterStep4Activity.this,
                                                res.getErrorMessage(), LENGTH_LONG)
                                                .show();
                                    }
                                }
                            }, new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            progressDialog.dismiss();
                            // String message = error.getMessage();
                            //String server  = new String(error.networkResponse.data);
                            makeText(RegisterStep4Activity.this, json_error, LENGTH_LONG).show();
                        }
                    });
            requestQueue.add(request);
        }
        catch (JSONException e)
        {
            makeText(RegisterStep4Activity.this, json_error, LENGTH_LONG).show();
        }
    }

    private void addValidations()
    {
        mandatoryFocusChange(etAccountNo, tilAccountNo, getString(R.string.error_accNo));
        mandatoryFocusChange(etBankBranch, tilBankBranch, getString(R.string.error_bankbranch));
      //  mandatoryFocusChange(etBankName, tilBankName, getString(R.string.error_bank));
        mandatoryFocusChange(etIFSCCode, tilIFSCCode, getString(R.string.error_ifsc_code));
        mandatoryFocusChange(etNameInAccount, tilNameInAccount, getString(R.string
                .error_name_account));
    }

    private boolean validForm()
    {
        boolean validForm = true;
        if (isEmpty(etAccountNo.getText()) || isEmpty(etBankBranch.getText()) ||
                isEmpty(etBankName.getSelectedItem().toString()) || isEmpty(etNameInAccount.getText()) ||
                isEmpty(etIFSCCode.getText()))
        {
            validForm = false;
        }
        return validForm;
    }

    private void populateData(){
        Registration reg = (Registration) getIntent().getSerializableExtra(REGISTRATION_DTO);
        etNameInAccount.setText(reg.getFirstName()+" "+reg.getLastName());
    }

    private void initSpinners()
    {
        ArrayAdapter<CharSequence> bankListArray = ArrayAdapter.createFromResource(this,
                R.array.bank_names_list,
                android.R
                        .layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        bankListArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        etBankName.setAdapter(bankListArray);

    }

    private boolean validateForm()
    {
        boolean valid = true;

        if (isEmpty(etBankName.getSelectedItem().toString())  || etBankName
                .getSelectedItemPosition() < 1)
        {
            valid = false;
        }
        return valid;
    }

}
