package com.bookmypacket.bmpclub.utils.net;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

/**
 * Created by Manish on 08-01-2016.
 */
public class ImageUploaderRequest extends StringRequest
{
    private Map<String, String> params;

    public ImageUploaderRequest(int method, String url,
                                Response.Listener<String> listener,
                                Response.ErrorListener errorListener)
    {
        super(method, url, listener, errorListener);
    }

    public ImageUploaderRequest(String url, Response.Listener<String> listener,
                                Response.ErrorListener errorListener)
    {
        super(url, listener, errorListener);
    }

    public ImageUploaderRequest(int method, String url,
                                Response.Listener<String> listener,
                                Response.ErrorListener errorListener,
                                Map<String, String> params)
    {
        super(method, url, listener, errorListener);
        this.params = params;
    }

    public ImageUploaderRequest(String url,
                                Response.Listener<String> listener,
                                Response.ErrorListener errorListener,
                                Map<String, String> params)
    {
        super(url, listener, errorListener);
        this.params = params;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError
    {
        return params;
    }
}
