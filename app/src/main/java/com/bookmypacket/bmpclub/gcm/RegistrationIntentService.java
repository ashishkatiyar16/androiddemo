package com.bookmypacket.bmpclub.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

import static com.apptentive.android.sdk.Apptentive.PUSH_PROVIDER_APPTENTIVE;
import static com.apptentive.android.sdk.Apptentive.setPushNotificationIntegration;
import static com.bookmypacket.bmpclub.R.string.gcm_defaultSenderId;
import static com.bookmypacket.bmpclub.utils.AppConstants.SharedPrefrencesKeys.SENT_TOKEN_TO_SERVER;
import static com.google.android.gms.gcm.GoogleCloudMessaging.INSTANCE_ID_SCOPE;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class RegistrationIntentService extends IntentService
{
    //    protected final String   DEVICE_ID = Secure.getString(getContentResolver(), ANDROID_ID);
    private String[] TOPICS = {"bmp.pickup", "bmp.delivery"};

    public RegistrationIntentService()
    {
        super("BMPRegistrationIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        try
        {
            InstanceID instanceID = InstanceID.getInstance(this);
            String token =
                    instanceID.getToken(getString(gcm_defaultSenderId), INSTANCE_ID_SCOPE, null);
            subscribeTopics(token);
            preferences.edit().putBoolean(SENT_TOKEN_TO_SERVER, true).apply();
            registerToken(token);
        }
        catch (IOException e)
        {
            preferences.edit().putBoolean(SENT_TOKEN_TO_SERVER, false).apply();
        }
    }

    private void registerToken(String token)
    {
        setPushNotificationIntegration(getApplicationContext(), PUSH_PROVIDER_APPTENTIVE, token);
    }

    private void subscribeTopics(String token) throws IOException
    {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        for (String topic : TOPICS)
        {
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }

}
