package com.bookmypacket.bmpclub.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.bookmypacket.bmpclub.sync.PacketSyncAdapter;

public class PacketSyncService extends Service
{
    private static final Object sSyncAdapterLock = new Object();
    private static PacketSyncAdapter syncAdapter;

    @Override
    public void onCreate()
    {
        super.onCreate();
        synchronized (sSyncAdapterLock)
        {
            if (syncAdapter == null)
            {
                syncAdapter = new PacketSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        // TODO: Return the communication channel to the service.
        return syncAdapter.getSyncAdapterBinder();
    }
}
