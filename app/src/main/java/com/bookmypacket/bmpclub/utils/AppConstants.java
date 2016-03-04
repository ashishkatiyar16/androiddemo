package com.bookmypacket.bmpclub.utils;

import com.bookmypacket.bmpclub.BuildConfig;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by Manish on 25-11-2015.
 */
public class AppConstants
{
    public static final String RS_SYMBOL         = "\u20B9 ";
    public static       String AUTH_HEADER_VALUE = "";

    public interface GooglePlacesParams{
        String BASE_URL = "";
        String API_KEY = "";
        LatLngBounds BOUNDS_INDIA = new LatLngBounds(new LatLng(7.9,68.4),new LatLng(37.4,97.6));
    }

    public interface PaymentGateway
    {

        int    ACC_ID                 = 10618; // Provided by EBS
        String SECRET_KEY             = "cf7f1a600c8e852c5a5d7cc50877b00d";
        String HOST_NAME_REGISTRATION = "EBS";
    }

    public interface Contants
    {
        String TNC_URL     = "http://www.bookmypacket.com/staticpages/tc";
        String PRIVACY_URL = "http://www.bookmypacket.com/staticpages/policy";
    }

    public interface BundleTags
    {
        String CONTAINER_URL = "url";
        String MOBILE_NUMBER = "mobile";
    }

    public interface UserConstants
    {
        long MOBILE_NO_TIMEOUT = MILLISECONDS.convert(1, SECONDS);
        String MOBILE_NUMBER_SEPERATOR = "-";
    }

    public interface SystemConstants
    {
        int VOLLEY_CACHE_SIZE = 2 * 1024 * 1024;
    }

    public interface NetworkUrls
    {
        String BASE_URL                    = BuildConfig.BASE_URL;
        String REGISTRATION_UPLOAD_PROFILE = BASE_URL + "userregistration";
    }

    public interface RequestParameters
    {
        String REGISTRATION_FIRST_NAME              = "firstName";
        String REGISTRATION_LAST_NAME               = "lastName";
        String REGISTRATION_STEP_NAME               = "stepName";
        String REGISTRATION_EMAIL                   = "emailId";
        String REGISTRATION_MOBILE                  = "mobileNo";
        String REGISTRATION_DEVICE                  = "deviceId";
        String REGISTRATION_TAX_ID                  = "taxationId";
        String REGISTRATION_TAX_ID_TYPE             = "taxationIdType";
        String REGISTRATION_ID_PROOF_TYPE           = "idProofType";
        String REGISTRATION_ID_PROOF_NO             = "idProofNumber";
        String REGISTRATION_VEHICAL_TYPE            = "vehicleType";
        String REGISTRATION_VEHICAL_REGISTRATION_NO = "vehicleRegistrationNo";
        String REGISTRATION_EMPLOYEE_ID             = "empolyeeId";
        String REGISTRATION_ADDRESS_LINE1           = "addressLine1";
        String REGISTRATION_ADDRESS_LINE2           = "addressLine2";
        String REGISTRATION_PINCODE                 = "pinCode";
        String REGISTRATION_CITY                    = "city";
        String REGISTRATION_STATE                   = "state";
        String REGISTRATION_BANK_ACCOUNT_NO         = "banckAccountNo";
        String REGISTRATION_NAME_IN_BANK            = "nameInBankAccount";
        String REGISTRATION_BANK_NAME               = "bankName";
        String REGISTRATION_IFSCCODE                = "ifscCode";
        String REGISTRATION_BANK_CITY               = "bankCity";
        String REGISTRATION_DELIVERY_PINCODES       = "deliveryServiceAreas";
        String REGISTRATION_PICKUP_PINCODES         = "pickupServiceAreas";
        String REGISTRATION_OTP                     = "otp";
        String OTP_VERIFICATION_OTPCODE             = "otpCode";
        String PAYMENT_DETAILS_AMOUNT               = "amount";
        String PAYMENT_DETAILS_MODE_OF_PAYMENT      = "modeOfPayment";
        String PAYMENT_DETAILS_EBS_RESPONSE_MESSAGE = "ebsResponseMessage";
        String PAYMENT_DETAILS_COUPON_CODE          = "couponCode";
        String PAYMENT_DETAILS_TRANSACTIONID        = "transactionId";
    }

    public interface RegistrationValues
    {
        String REGISTRATION_STEP_1_NAME  = "PROFILE_DATA";
        String OTP_VERIFICATION_STEP_NAME = "OTP_VERIFICATION";
        String PAYMENT_DETAILS_STEP_NAME = "PAYMENT_DETAILS";
        String PAYMENT_MODE_ONLINE       = "ONLINE_EBS";
        String PAYMENT_MODE_OFFLINE      = "OFFLINE";
    }

    public interface BundleExtraKeys
    {
        String REGISTRATION_DTO = "registration";
        String REGISTRATION_RESPONSE_DTO = "reg_resp";
        String PACKET           = "packet";
    }

    public interface SharedPrefrencesKeys
    {
        String SENT_TOKEN_TO_SERVER    = "token_sent";
        String WALLET_HOLD_AMOUNT      = "hold_amount";
        String WALLET_AVAILABLE_AMOUNT = "walllet_amount";
        String REGISTRATION_VALUES     = "registration";
        String USER_PROFILE = "user_profile";
        String GCM_TOKEN               = "gcm_token";
    }

    public interface Headers
    {
        String AUTH_HEADER_KEY = "Authorization_bmp";
    }
}
