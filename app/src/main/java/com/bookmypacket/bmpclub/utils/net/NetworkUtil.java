package com.bookmypacket.bmpclub.utils.net;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;

import java.io.File;

import static com.bookmypacket.bmpclub.utils.AppConstants.SystemConstants.VOLLEY_CACHE_SIZE;

/**
 * Created by Manish on 25-11-2015.
 */
public class NetworkUtil
{
    private static RequestQueue requestQueue;

    private NetworkUtil(File file)
    {
        Cache   cache   = new DiskBasedCache(file, VOLLEY_CACHE_SIZE);
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();
    }

    public static RequestQueue getInstance(File file)
    {
        if (requestQueue == null)
        {
            new NetworkUtil(file);
        }
        return requestQueue;
    }
}
