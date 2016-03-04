package com.bookmypacket.bmpclub.utils.retro;


import com.bookmypacket.bmpclub.dto.RegistrationResponse;
import com.squareup.okhttp.RequestBody;

import retrofit.Call;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;

/**
 * Created by Manish on 08-01-2016.
 */
public interface ImageUploadService
{

    @Multipart
    @POST("userregistration")
    Call<RegistrationResponse> upload(
            @Part("cancelCheque\"; filename=\"cancelCheque.jpg\" ") RequestBody cancelCheque,
            @Part("addressProof\"; filename=\"addressProof.jpg\" ") RequestBody addressProof,
            @Part("idProof\"; filename=\"idProof.jpg\" ") RequestBody idProof,
            @Part("image\"; filename=\"image.jpg\" ") RequestBody image,
            @Part("taxId\"; filename=\"taxId.jpg\" ") RequestBody taxId,
            @Part(value = "profileId") String profileId);
}
