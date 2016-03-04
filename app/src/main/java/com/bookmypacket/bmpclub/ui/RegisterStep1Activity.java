package com.bookmypacket.bmpclub.ui;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.bookmypacket.bmpclub.R;
import com.bookmypacket.bmpclub.dto.Registration;

import java.util.HashSet;
import java.util.Set;

import static android.text.TextUtils.isEmpty;
import static android.util.Patterns.EMAIL_ADDRESS;
import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;
import static com.bookmypacket.bmpclub.R.id.btn_continue;
import static com.bookmypacket.bmpclub.R.id.et_emailAddress;
import static com.bookmypacket.bmpclub.R.id.et_firstName;
import static com.bookmypacket.bmpclub.R.id.et_lastName;
import static com.bookmypacket.bmpclub.R.id.et_mobileno;
import static com.bookmypacket.bmpclub.R.id.lo_email;
import static com.bookmypacket.bmpclub.R.id.lo_fname;
import static com.bookmypacket.bmpclub.R.id.lo_lname;
import static com.bookmypacket.bmpclub.R.id.lo_mobileno;
import static com.bookmypacket.bmpclub.R.layout.activity_register_step1;
import static com.bookmypacket.bmpclub.R.string.toast_empty;
import static com.bookmypacket.bmpclub.utils.ActionUtils.mandatoryFocusChange;
import static com.bookmypacket.bmpclub.utils.AppConstants.BundleExtraKeys.REGISTRATION_DTO;

public class RegisterStep1Activity extends BaseActivity
{
    private TextView             btnCongtinue;
    private TextInputLayout      tilMobileNo;
    private EditText             etMobileNo;
    private TextInputLayout      tilFName;
    private EditText             etFName;
    private TextInputLayout      tilLName;
    private EditText             etLName;
    private TextInputLayout      tilEmail;
    private AutoCompleteTextView etEmail;
    private CheckBox             tncAccepted;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(activity_register_step1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initIds();
        setActions();
        registerFab();
        addValidations();
        populateAutoFillEmails();
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
                            new Intent(RegisterStep1Activity.this, RegisterStep2Activity.class);
                    i.putExtra(REGISTRATION_DTO, registration);
                    startActivity(i);
                } else
                {
                    makeText(RegisterStep1Activity.this, toast_empty, LENGTH_LONG).show();
                }
            }
        });
        tncAccepted.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                makeText(RegisterStep1Activity.this, R.string.tnc_text, LENGTH_LONG).show();
            }
        });
    }

    private void initIds()
    {
        btnCongtinue = (TextView) findViewById(btn_continue);
        tilMobileNo = (TextInputLayout) findViewById(lo_mobileno);
        etMobileNo = (EditText) findViewById(et_mobileno);
        tilFName = (TextInputLayout) findViewById(lo_fname);
        etFName = (EditText) findViewById(et_firstName);
        tilLName = (TextInputLayout) findViewById(lo_lname);
        etLName = (EditText) findViewById(et_lastName);
        tilEmail = (TextInputLayout) findViewById(lo_email);
        etEmail = (AutoCompleteTextView) findViewById(et_emailAddress);
        tncAccepted = (CheckBox) findViewById(R.id.tnc_accepted);

    }


    private Registration createRegistration()
    {
        Registration reg = new Registration();
        reg.setMobileNo(etMobileNo.getText().toString());
        reg.setFirstName(etFName.getText().toString());
        reg.setLastName(etLName.getText().toString());
        reg.setEmailId(etEmail.getText().toString());
        return reg;
    }


    private void addValidations()
    {
        mandatoryFocusChange(etEmail, tilEmail, getString(R.string.error_email));
        mandatoryFocusChange(etFName, tilFName, getString(R.string.error_fname));
        mandatoryFocusChange(etLName, tilLName, getString(R.string.error_lname));
        mandatoryFocusChange(etMobileNo, tilMobileNo, getString(R.string.error_mobileNo));
    }

    private boolean validateForm()
    {
        boolean allowed = true;
        if (isEmpty(etEmail.getText()) || isEmpty(etMobileNo.getText()) ||
                isEmpty(etFName.getText()) || isEmpty(etLName.getText()))
        {
            return false;
        }
        if (!isEmpty(etMobileNo.getText()) && etMobileNo.getText().length() < 10)
        {
            tilMobileNo.setErrorEnabled(true);
            tilMobileNo.setError(getString(R.string.error_mobile_length));
            return false;
        }
        if (!isEmpty(etEmail.getText()) &&
                !EMAIL_ADDRESS.matcher(etEmail.getText()).matches())
        {
            tilEmail.setErrorEnabled(true);
            tilEmail.setError(getString(R.string.error_validemail));
            return false;
        }

        if (!tncAccepted.isChecked())
        {
            return false;
        }
        return allowed;
    }

    private void populateAutoFillEmails()
    {
        Set<String> emailSet = new HashSet<>();
        Account[]   accounts = AccountManager.get(this).getAccounts();
        for (Account account : accounts)
        {
            if (EMAIL_ADDRESS.matcher(account.name).matches())
            {
                emailSet.add(account.name);
            }
        }
        String[] emails = new String[emailSet.size()];
        emails = emailSet.toArray(emails);
        ArrayAdapter<String> adp = new ArrayAdapter<String>(RegisterStep1Activity.this,
                                                            android.R.layout.simple_dropdown_item_1line,
                                                            emails);
        etEmail.setThreshold(1);
        etEmail.setAdapter(adp);
    }
}
