package com.bookmypacket.bmpclub.gcm;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by Manish on 27-12-2015.
 */
public class BMPGcmInstanceIdListener extends InstanceIDListenerService
{
    @Override
    public void onTokenRefresh()
    {
        super.onTokenRefresh();
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}
