package com.bookmypacket.bmpclub;

import android.app.IntentService;
import android.content.Intent;

import com.bookmypacket.bmpclub.dto.UserProfile;
import com.bookmypacket.bmpclub.dto.UserProfileRequest;
import com.bookmypacket.bmpclub.utils.retro.ServiceGenerator;
import com.bookmypacket.bmpclub.utils.retro.UserProfileService;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static com.bookmypacket.bmpclub.utils.SharedPrefrenceManager.getAuthHeader;
import static com.bookmypacket.bmpclub.utils.SharedPrefrenceManager.getMobileNo;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class UserProfileIntentService extends IntentService
{


    public UserProfileIntentService()
    {
        super("UserProfileIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        if (intent != null)
        {
            getUserProfile();
        }
    }

    private void getUserProfile()
    {
        UserProfileRequest request = new UserProfileRequest();
        request.setMobileNo(getMobileNo(this));
        UserProfileService service = ServiceGenerator.createService(UserProfileService.class);
        Call<UserProfile>  call    = service.get(getAuthHeader(this), request);
        call.enqueue(new Callback<UserProfile>()
        {
            @Override
            public void onResponse(Response<UserProfile> response, Retrofit retrofit)
            {
                if (response.body() != null)
                {
                    // saveProfile(UserProfileIntentService.this, response.body());
                }
            }

            @Override
            public void onFailure(Throwable t)
            {

            }
        });
    }
}
