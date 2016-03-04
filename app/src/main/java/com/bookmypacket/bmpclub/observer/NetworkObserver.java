package com.bookmypacket.bmpclub.observer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.bookmypacket.bmpclub.ui.NoNetworkActivity;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by Manish on 26-01-2016.
 */
public class NetworkObserver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean     isConnected   = activeNetwork != null && activeNetwork.isConnected();
        if (!isConnected)
        {
            Intent i = new Intent(context, NoNetworkActivity.class);
            i.setFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(i);
        }
    }

}
