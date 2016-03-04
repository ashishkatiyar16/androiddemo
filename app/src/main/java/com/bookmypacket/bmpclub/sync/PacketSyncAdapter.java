package com.bookmypacket.bmpclub.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

/**
 * Created by Manish on 20-01-2016.
 */
public class PacketSyncAdapter extends AbstractThreadedSyncAdapter
{
    ContentResolver contentResolver;

    public PacketSyncAdapter(Context context, boolean autoInitialize)
    {
        super(context, autoInitialize);
        contentResolver = context.getContentResolver();
    }

    public PacketSyncAdapter(Context context, boolean autoInitialize,
                             boolean allowParallelSyncs)
    {
        super(context, autoInitialize, allowParallelSyncs);
        contentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle bundle, String s,
                              ContentProviderClient contentProviderClient, SyncResult syncResult)
    {

    }
}
