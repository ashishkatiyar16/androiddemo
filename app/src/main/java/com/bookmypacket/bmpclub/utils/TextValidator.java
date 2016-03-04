package com.bookmypacket.bmpclub.utils;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.widget.EditText;

import static android.text.TextUtils.isEmpty;

/**
 * Created by Manish on 07-01-2016.
 */
public class TextValidator
{

    public static final boolean lengthValidator(TextInputLayout til, EditText eit, String err,
                                                int minLength, int maxLength)
    {
        boolean  valid = true;
        Editable text  = eit.getText();
        if (isEmpty(text) || text.length() < minLength || text.length() > maxLength)
        {
            til.setError(err);
            valid = false;
        }
        return valid;

    }

    public static final boolean regexValidator(TextInputLayout til, EditText eit, String err, String
            regex)
    {
        boolean  valid = true;
        Editable text  = eit.getText();
        if (isEmpty(text))
        {
            til.setError(err);
            valid = false;
        } else if (!text.toString().matches(regex))
        {
            til.setError(err);
            valid = false;
        }
        return valid;
    }
}
