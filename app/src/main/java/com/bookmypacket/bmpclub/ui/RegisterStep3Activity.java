package com.bookmypacket.bmpclub.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.bookmypacket.bmpclub.R;
import com.bookmypacket.bmpclub.dto.Registration;

import static android.text.TextUtils.isEmpty;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;
import static com.bookmypacket.bmpclub.R.string.toast_empty;
import static com.bookmypacket.bmpclub.utils.ActionUtils.mandatoryFocusChange;
import static com.bookmypacket.bmpclub.utils.AppConstants.BundleExtraKeys.REGISTRATION_DTO;

public class RegisterStep3Activity extends BaseActivity
{
    private TextView        btnCongtinue;
    private Spinner         spProofType;
    private Spinner         spVehicalType;
    private Spinner         spTaxType;
    private TextInputLayout tilVehical;
    private EditText        etVehicalNo;
    private TextInputLayout tilTaxId;
    private EditText        etTaxId;
    private TextInputLayout tilIdProof;
    private EditText        etIdProof;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step3);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        registerFab();
        initIds();
        setActions();
        initSpinners();
        addValidations();
    }

    private void setActions()
    {
        btnCongtinue.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (validateForm())
                {
                    Registration registration = createRegistration();
                    Intent i =
                            new Intent(RegisterStep3Activity.this, RegisterStep4Activity.class);
                    i.putExtra(REGISTRATION_DTO, registration);
                    startActivity(i);
                } else
                {
                    makeText(RegisterStep3Activity.this, toast_empty, LENGTH_LONG).show();
                }
            }
        });
        spVehicalType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                if (i > 0)
                {
                    tilVehical.setVisibility(VISIBLE);
                } else
                {
                    tilVehical.setVisibility(GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });

    }

    private void initIds()
    {
        btnCongtinue = (TextView) findViewById(R.id.btn_continue);
        spProofType = (Spinner) findViewById(R.id.sp_proofType);
        spVehicalType = (Spinner) findViewById(R.id.sp_vehical_type);
        spTaxType = (Spinner) findViewById(R.id.sp_taxType);
        tilVehical = (TextInputLayout) findViewById(R.id.til_vehical_no);
        etVehicalNo = (EditText) findViewById(R.id.et_vehicalNo);
        tilTaxId = (TextInputLayout) findViewById(R.id.til_serviceTax);
        etTaxId = (EditText) findViewById(R.id.et_serviceTax);
        tilIdProof = (TextInputLayout) findViewById(R.id.til_idProofNo);
        etIdProof = (EditText) findViewById(R.id.et_idProofNo);
    }

    private void initSpinners()
    {
        ArrayAdapter<CharSequence> idProofArray = ArrayAdapter.createFromResource(this,
                                                                                  R.array.id_proof_types,
                                                                                  android.R
                                                                                          .layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        idProofArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spProofType.setAdapter(idProofArray);

        ArrayAdapter<CharSequence> vehicalAdapter = ArrayAdapter.createFromResource(this,
                                                                                    R.array.vehical_type,
                                                                                    android.R
                                                                                            .layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        vehicalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spVehicalType.setAdapter(vehicalAdapter);
        ArrayAdapter<CharSequence> taxtTypeAdapter = ArrayAdapter.createFromResource(this,
                                                                                     R.array.id_tax_types,
                                                                                     android.R
                                                                                             .layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        taxtTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spTaxType.setAdapter(taxtTypeAdapter);

    }

    private Registration createRegistration()
    {
        Registration reg = (Registration) getIntent().getSerializableExtra(REGISTRATION_DTO);
        reg.setTaxationIdType(spTaxType.getSelectedItem().toString());
        reg.setTaxationId(etTaxId.getText().toString());
        reg.setIdProofType(spProofType.getSelectedItem().toString());
        reg.setIdProofNumber(etIdProof.getText().toString());
        if (spVehicalType.getSelectedItemPosition() > 0)
        {
            reg.setVehicleType(spVehicalType.getSelectedItem().toString());
            reg.setVehicleRegistrationNo(etVehicalNo.getText().toString());
        }
        return reg;
    }

    private void addValidations()
    {
        mandatoryFocusChange(etIdProof, tilIdProof, getString(R.string.error_idproof));
        mandatoryFocusChange(etTaxId, tilTaxId, getString(R.string.error_taxId));
    }

    private boolean validateForm()
    {
        boolean valid = true;

        if (isEmpty(etIdProof.getText()) || isEmpty(etTaxId.getText()) || spProofType
                .getSelectedItemPosition() < 1)
        {
            valid = false;
        } else if (spVehicalType.getSelectedItemPosition() > 0 && isEmpty(etVehicalNo.getText()))
        {
            valid = false;
        }
        return valid;
    }
}
