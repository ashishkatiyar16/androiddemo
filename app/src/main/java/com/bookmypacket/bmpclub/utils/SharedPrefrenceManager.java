package com.bookmypacket.bmpclub.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.bookmypacket.bmpclub.dto.LoginVerifyResponse;
import com.bookmypacket.bmpclub.dto.Registration;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static android.content.Context.MODE_PRIVATE;
import static com.bookmypacket.bmpclub.utils.AppConstants.AUTH_HEADER_VALUE;
import static com.bookmypacket.bmpclub.utils.AppConstants.BundleTags.MOBILE_NUMBER;
import static com.bookmypacket.bmpclub.utils.AppConstants.Headers.AUTH_HEADER_KEY;
import static com.bookmypacket.bmpclub.utils.AppConstants.SharedPrefrencesKeys.REGISTRATION_VALUES;
import static com.bookmypacket.bmpclub.utils.AppConstants.SharedPrefrencesKeys.USER_PROFILE;

/**
 * Created by Manish on 07-01-2016.
 */
public class SharedPrefrenceManager
{
    private static final String SHARED_PREF_FILE = "bmpclub";
    private static final Map<String, Object> PREFRENCES_CACHE = new ConcurrentHashMap<>();
    private static SharedPreferences preferences;

    private static SharedPreferences getPrefrence(Context ctx)
    {
        if (preferences == null)
        {
            preferences = ctx.getSharedPreferences(SHARED_PREF_FILE, MODE_PRIVATE);
        }
        return preferences;
    }

    public static String getAuthHeader(Context ctx)
    {
        if (TextUtils.isEmpty(AUTH_HEADER_VALUE))
        {
            AUTH_HEADER_VALUE = PreferenceManager.getDefaultSharedPreferences(ctx).getString(
                    AUTH_HEADER_KEY, AUTH_HEADER_VALUE);
        }
        return AUTH_HEADER_VALUE;
    }


    public static void saveAuthHeader(Context ctx)
    {
        PreferenceManager.getDefaultSharedPreferences(ctx)
                .edit().putString(AUTH_HEADER_KEY, AUTH_HEADER_VALUE).commit();
    }

    public static String getMobileNo(Context ctx)
    {
        String mobileNo = PreferenceManager.getDefaultSharedPreferences(ctx).getString(
                MOBILE_NUMBER, MOBILE_NUMBER);
        return mobileNo;
    }

    public static void saveMobileNo(Context ctx, String mobileNo)
    {
        PreferenceManager.getDefaultSharedPreferences(ctx)
                .edit().putString(MOBILE_NUMBER, mobileNo).commit();
    }

    public static void savePrefrence(Context ctx, Map<String, String> prefs)
    {

        SharedPreferences.Editor editor = getPrefrence(ctx).edit();
        Set<String>              keys   = prefs.keySet();
        for (String key : keys)
        {
            editor.putString(key, prefs.get(key));
        }
        editor.commit();
    }

    public static void savePrefrence(Context ctx, String key, String pref)
    {

        SharedPreferences.Editor editor = getPrefrence(ctx).edit();
        editor.putString(key, pref);
        editor.commit();
    }

    public static void deletePrefrence(Context ctx, List<String> keys)
    {
        SharedPreferences.Editor editor = getPrefrence(ctx).edit();
        for (String k : keys)
        {
            editor.remove(k);
        }
        editor.commit();
    }

    public static void deletePrefrence(Context ctx, String key)
    {
        SharedPreferences.Editor editor = getPrefrence(ctx).edit();
        editor.remove(key);

        editor.commit();
    }

    public static String getPrefrence(Context ctx, String key)
    {
        String value = getPrefrence(ctx).getString(key, "");
        return value;
    }

    public static void saveRegistration(Context ctx, Registration reg)
    {
        Gson   gson      = new Gson();
        String regString = gson.toJson(reg);
        savePrefrence(ctx, REGISTRATION_VALUES, regString);
    }

    public static Registration getRegistration(Context ctx)
    {
        Gson   g      = new Gson();
        String regStr = getPrefrence(ctx, REGISTRATION_VALUES);
        return g.fromJson(regStr, Registration.class);
    }

    public static void saveProfile(Context ctx, LoginVerifyResponse profile)
    {
        Gson   gson      = new Gson();
        String regString = gson.toJson(profile);
        savePrefrence(ctx, USER_PROFILE, regString);
    }

    public static LoginVerifyResponse getProfile(Context ctx)
    {
        Gson   g      = new Gson();
        String regStr = getPrefrence(ctx, USER_PROFILE);
        return g.fromJson(regStr, LoginVerifyResponse.class);
    }

    public static void clearData(Context ctx)
    {
        SharedPreferences.Editor et = getPrefrence(ctx).edit();
        et.clear();
        et.commit();
        AUTH_HEADER_VALUE = null;
        et = PreferenceManager.getDefaultSharedPreferences(ctx).edit();
        et.clear();
        et.commit();

    }

}
