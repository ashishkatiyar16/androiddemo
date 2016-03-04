package com.bookmypacket.bmpclub.utils.retro;

import com.bookmypacket.bmpclub.dto.UserProfile;
import com.bookmypacket.bmpclub.dto.UserProfileRequest;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.POST;

import static com.bookmypacket.bmpclub.utils.AppConstants.Headers.AUTH_HEADER_KEY;

/**
 * Created by Manish on 23-01-2016.
 */
public interface UserProfileService
{
    @POST("userprofile")
    Call<UserProfile> get(@Header(AUTH_HEADER_KEY) String authHeader,
                          @Body UserProfileRequest request);
}
