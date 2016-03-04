package com.bookmypacket.bmpclub.utils.retro;

import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

import static com.bookmypacket.bmpclub.utils.AppConstants.NetworkUrls.BASE_URL;

/**
 * Created by Manish on 09-01-2016.
 */
public class ServiceGenerator
{


    private static OkHttpClient     httpClient = new OkHttpClient();
    private static Retrofit.Builder builder    = new Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass)
    {
        httpClient.setConnectTimeout(10, TimeUnit.MINUTES);
        httpClient.setReadTimeout(10, TimeUnit.MINUTES);
        httpClient.setWriteTimeout(10, TimeUnit.MINUTES);
        Retrofit retrofit = builder.client(httpClient).build();
        return retrofit.create(serviceClass);
    }
}
