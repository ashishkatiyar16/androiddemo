package com.bookmypacket.bmpclub.utils.retro;

import com.bookmypacket.bmpclub.dto.BMPPacketsList;
import com.bookmypacket.bmpclub.dto.PacketsListRequest;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.POST;

import static com.bookmypacket.bmpclub.utils.AppConstants.Headers.AUTH_HEADER_KEY;

/**
 * Created by Manish on 28-01-2016.
 */
public interface PacketsCompletedService
{
    @POST("packetsforjobclosing")
    Call<BMPPacketsList> getPacketList(@Header(AUTH_HEADER_KEY) String authHeader,
                                       @Body PacketsListRequest request);
}
