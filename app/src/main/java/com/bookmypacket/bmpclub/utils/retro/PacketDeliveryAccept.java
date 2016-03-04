package com.bookmypacket.bmpclub.utils.retro;

import com.bookmypacket.bmpclub.dto.AcceptPacketRequest;
import com.bookmypacket.bmpclub.dto.AcceptPacketResponse;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.POST;

import static com.bookmypacket.bmpclub.utils.AppConstants.Headers.AUTH_HEADER_KEY;

/**
 * Created by Manish on 11-01-2016.
 */
public interface PacketDeliveryAccept
{
    @POST("packetdeliveryaccept")
    Call<AcceptPacketResponse> acceptPacket(@Header(AUTH_HEADER_KEY) String authHeader,
                                            @Body AcceptPacketRequest request);
}
