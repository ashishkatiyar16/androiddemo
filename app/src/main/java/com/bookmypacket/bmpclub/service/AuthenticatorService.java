package com.bookmypacket.bmpclub.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.bookmypacket.bmpclub.sync.Authenticator;

public class AuthenticatorService extends Service
{
    private Authenticator authenticator;

    @Override
    public void onCreate()
    {
        super.onCreate();
        authenticator = new Authenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return authenticator.getIBinder();
    }
}
