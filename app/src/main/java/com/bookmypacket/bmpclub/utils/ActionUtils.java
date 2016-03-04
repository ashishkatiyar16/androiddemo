package com.bookmypacket.bmpclub.utils;

import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Manish on 21-01-2016.
 */
public class ActionUtils
{
    public static void mandatoryFocusChange(final EditText et, final TextInputLayout til,
                                            final String error)
    {
        et.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View view, boolean b)
            {

                if (b)
                {
                    til.setError(null);
                    til.setErrorEnabled(false);

                } else
                {
                    if (TextUtils.isEmpty(et.getText()))
                    {
                        til.setErrorEnabled(true);
                        til.setError(error);
                    }
                }
            }
        });
    }
}
