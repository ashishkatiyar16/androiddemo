package com.bookmypacket.bmpclub.utils.retro;

import com.squareup.okhttp.RequestBody;

import org.json.JSONObject;

import java.util.Map;

import retrofit.Call;
import retrofit.http.Header;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.PartMap;

import static com.bookmypacket.bmpclub.utils.AppConstants.Headers.AUTH_HEADER_KEY;

/**
 * Created by Manish on 18-01-2016.
 */
public interface DeliverPacketService
{
    @Multipart
    @POST("packetdeliverystatusupdate")
    Call<JSONObject> deliver(@Header(AUTH_HEADER_KEY) String authHeader,
                             @PartMap Map<String, RequestBody> images,
                             @Part("packetId") String packetId,
                             @Part("idProofType") String idProofType,
                             @Part("idProofNo") String idProofNo,
                             @Part("lattitude") String lattitude, @Part("longitude")
                             String longitude, @Part("packetStatus") String packetStatus,
                             @Part("assignedTo") String assignedTo);
}
