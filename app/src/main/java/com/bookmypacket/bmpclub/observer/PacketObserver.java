package com.bookmypacket.bmpclub.observer;

import android.database.ContentObserver;
import android.os.Handler;

/**
 * Created by Manish on 20-01-2016.
 */
public class PacketObserver extends ContentObserver
{
    public PacketObserver(Handler handler)
    {
        super(handler);
    }
}
