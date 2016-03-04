package com.bookmypacket.bmpclub.utils.net;

import com.bookmypacket.bmpclub.dto.LoginRequest;
import com.bookmypacket.bmpclub.dto.LoginVerifyRequest;
import com.bookmypacket.bmpclub.dto.MobileVerificationRequest;
import com.bookmypacket.bmpclub.dto.PaymentDetailsRequest;
import com.bookmypacket.bmpclub.dto.Registration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.bookmypacket.bmpclub.utils.AppConstants.RegistrationValues.OTP_VERIFICATION_STEP_NAME;
import static com.bookmypacket.bmpclub.utils.AppConstants.RegistrationValues.PAYMENT_DETAILS_STEP_NAME;
import static com.bookmypacket.bmpclub.utils.AppConstants.RegistrationValues.PAYMENT_MODE_OFFLINE;
import static com.bookmypacket.bmpclub.utils.AppConstants.RegistrationValues.PAYMENT_MODE_ONLINE;
import static com.bookmypacket.bmpclub.utils.AppConstants.RegistrationValues.REGISTRATION_STEP_1_NAME;
import static com.bookmypacket.bmpclub.utils.AppConstants.RequestParameters.OTP_VERIFICATION_OTPCODE;
import static com.bookmypacket.bmpclub.utils.AppConstants.RequestParameters.PAYMENT_DETAILS_AMOUNT;
import static com.bookmypacket.bmpclub.utils.AppConstants.RequestParameters.PAYMENT_DETAILS_COUPON_CODE;
import static com.bookmypacket.bmpclub.utils.AppConstants.RequestParameters.PAYMENT_DETAILS_EBS_RESPONSE_MESSAGE;
import static com.bookmypacket.bmpclub.utils.AppConstants.RequestParameters.PAYMENT_DETAILS_MODE_OF_PAYMENT;
import static com.bookmypacket.bmpclub.utils.AppConstants.RequestParameters.PAYMENT_DETAILS_TRANSACTIONID;
import static com.bookmypacket.bmpclub.utils.AppConstants.RequestParameters.REGISTRATION_ADDRESS_LINE1;
import static com.bookmypacket.bmpclub.utils.AppConstants.RequestParameters.REGISTRATION_ADDRESS_LINE2;
import static com.bookmypacket.bmpclub.utils.AppConstants.RequestParameters.REGISTRATION_BANK_ACCOUNT_NO;
import static com.bookmypacket.bmpclub.utils.AppConstants.RequestParameters.REGISTRATION_BANK_CITY;
import static com.bookmypacket.bmpclub.utils.AppConstants.RequestParameters.REGISTRATION_BANK_NAME;
import static com.bookmypacket.bmpclub.utils.AppConstants.RequestParameters.REGISTRATION_CITY;
import static com.bookmypacket.bmpclub.utils.AppConstants.RequestParameters.REGISTRATION_DELIVERY_PINCODES;
import static com.bookmypacket.bmpclub.utils.AppConstants.RequestParameters.REGISTRATION_DEVICE;
import static com.bookmypacket.bmpclub.utils.AppConstants.RequestParameters.REGISTRATION_EMAIL;
import static com.bookmypacket.bmpclub.utils.AppConstants.RequestParameters.REGISTRATION_FIRST_NAME;
import static com.bookmypacket.bmpclub.utils.AppConstants.RequestParameters.REGISTRATION_ID_PROOF_NO;
import static com.bookmypacket.bmpclub.utils.AppConstants.RequestParameters.REGISTRATION_ID_PROOF_TYPE;
import static com.bookmypacket.bmpclub.utils.AppConstants.RequestParameters.REGISTRATION_IFSCCODE;
import static com.bookmypacket.bmpclub.utils.AppConstants.RequestParameters.REGISTRATION_LAST_NAME;
import static com.bookmypacket.bmpclub.utils.AppConstants.RequestParameters.REGISTRATION_MOBILE;
import static com.bookmypacket.bmpclub.utils.AppConstants.RequestParameters.REGISTRATION_NAME_IN_BANK;
import static com.bookmypacket.bmpclub.utils.AppConstants.RequestParameters.REGISTRATION_PICKUP_PINCODES;
import static com.bookmypacket.bmpclub.utils.AppConstants.RequestParameters.REGISTRATION_PINCODE;
import static com.bookmypacket.bmpclub.utils.AppConstants.RequestParameters.REGISTRATION_STATE;
import static com.bookmypacket.bmpclub.utils.AppConstants.RequestParameters.REGISTRATION_STEP_NAME;
import static com.bookmypacket.bmpclub.utils.AppConstants.RequestParameters.REGISTRATION_TAX_ID;
import static com.bookmypacket.bmpclub.utils.AppConstants.RequestParameters.REGISTRATION_TAX_ID_TYPE;
import static com.bookmypacket.bmpclub.utils.AppConstants.RequestParameters.REGISTRATION_VEHICAL_REGISTRATION_NO;
import static com.bookmypacket.bmpclub.utils.AppConstants.RequestParameters.REGISTRATION_VEHICAL_TYPE;
/**
 * Created by Manish on 23-11-2015.
 */
public class NetworkRequests
{
    public static JSONObject createRegistrationRequest1(Registration registration, String deviceId)
            throws JSONException
    {
        JSONObject regJson = new JSONObject();
        regJson.put(REGISTRATION_STEP_NAME, REGISTRATION_STEP_1_NAME);
        regJson.put(REGISTRATION_DEVICE, deviceId);
        regJson.put(REGISTRATION_FIRST_NAME, registration.getFirstName());
        regJson.put(REGISTRATION_LAST_NAME, registration.getLastName());
        regJson.put(REGISTRATION_EMAIL, registration.getEmailId());
        regJson.put(REGISTRATION_MOBILE, registration.getMobileNo());
        regJson.put(REGISTRATION_ADDRESS_LINE1, registration.getAddressLine1());
        regJson.put(REGISTRATION_ADDRESS_LINE2, registration.getAddressLine2());
        regJson.put(REGISTRATION_PINCODE, registration.getPinCode());
        regJson.put(REGISTRATION_CITY, registration.getCity());
        regJson.put(REGISTRATION_STATE, registration.getState());
        JSONArray deliveryJson  = new JSONArray();
        String[] deliveryAreas = registration.getDeliveryServiceAreas();//.split(",");
        for (String d : deliveryAreas)
        {
            deliveryJson.put(d);
        }
        regJson.put(REGISTRATION_DELIVERY_PINCODES, deliveryJson);
        JSONArray pickupJson  = new JSONArray();
        String[] pickupAreas = registration.getPickupServiceAreas();//.split(",");
        for (String p : pickupAreas)
        {
            pickupJson.put(p);
        }
        regJson.put(REGISTRATION_PICKUP_PINCODES, pickupJson);
        regJson.put(REGISTRATION_ID_PROOF_TYPE, registration.getIdProofType());
        regJson.put(REGISTRATION_ID_PROOF_NO, registration.getIdProofNumber());
        regJson.put(REGISTRATION_TAX_ID_TYPE, registration.getTaxationIdType());
        regJson.put(REGISTRATION_TAX_ID, registration.getTaxationId());
        regJson.put(REGISTRATION_VEHICAL_TYPE, registration.getVehicleType());
        regJson.put(REGISTRATION_VEHICAL_REGISTRATION_NO, registration.getVehicleRegistrationNo());
        regJson.put(REGISTRATION_NAME_IN_BANK, registration.getNameInBankAccount());
        regJson.put(REGISTRATION_BANK_ACCOUNT_NO, registration.getBanckAccountNo());
        regJson.put(REGISTRATION_IFSCCODE, registration.getIfscCode());
        regJson.put(REGISTRATION_BANK_NAME, registration.getBankName());
        regJson.put(REGISTRATION_BANK_CITY, registration.getBankCity());
        return regJson;
    }

    public static JSONObject otpVerificationOfRegistration(
            MobileVerificationRequest otpVerificationRequest, String deviceId)
            throws JSONException
    {
        JSONObject otpJson = new JSONObject();
        otpJson.put(REGISTRATION_STEP_NAME, OTP_VERIFICATION_STEP_NAME);
        otpJson.put(REGISTRATION_DEVICE, deviceId);
        otpJson.put(OTP_VERIFICATION_OTPCODE, otpVerificationRequest.getOtpCode());
        otpJson.put(REGISTRATION_MOBILE, otpVerificationRequest.getMobileNo());
        return otpJson;
    }
    public static JSONObject paymentDetailsRequest(PaymentDetailsRequest paymentDetailsRequest, String deviceId)
            throws JSONException
    {
        JSONObject paymenDetailsJson = new JSONObject();
        paymenDetailsJson.put(REGISTRATION_STEP_NAME, PAYMENT_DETAILS_STEP_NAME);
        paymenDetailsJson.put(REGISTRATION_DEVICE, deviceId);
        paymenDetailsJson.put(PAYMENT_DETAILS_AMOUNT, paymentDetailsRequest.getAmount());
        if(PAYMENT_MODE_ONLINE==paymentDetailsRequest.getModeOfPayment()){
            paymenDetailsJson.put(PAYMENT_DETAILS_MODE_OF_PAYMENT, PAYMENT_MODE_ONLINE);
        }
        else {
            paymenDetailsJson.put(PAYMENT_DETAILS_MODE_OF_PAYMENT, PAYMENT_MODE_OFFLINE);
        }
        paymenDetailsJson.put(PAYMENT_DETAILS_EBS_RESPONSE_MESSAGE, paymentDetailsRequest.getEbsResponseMessage());
        paymenDetailsJson.put(PAYMENT_DETAILS_COUPON_CODE, paymentDetailsRequest.getCouponCode());
        paymenDetailsJson.put(PAYMENT_DETAILS_TRANSACTIONID, paymentDetailsRequest.getTransactionId());
        paymenDetailsJson.put(REGISTRATION_MOBILE, paymentDetailsRequest.getMobileNo());
        return paymenDetailsJson;
    }
    public static JSONObject LoginRequest(LoginRequest loginRequest, String deviceId)
            throws JSONException
    {
        JSONObject loginReqJson = new JSONObject();
        loginReqJson.put(REGISTRATION_DEVICE, deviceId);
        loginReqJson.put(REGISTRATION_MOBILE, loginRequest.getMobileNo());
        return loginReqJson;
    }
    public static JSONObject LoginVerifyRequest(LoginVerifyRequest loginVerifyRequest, String deviceId)
            throws JSONException
    {
        JSONObject loginVerifyJson = new JSONObject();
        loginVerifyJson.put(OTP_VERIFICATION_OTPCODE, loginVerifyRequest.getOtpCode());
        loginVerifyJson.put(REGISTRATION_MOBILE, loginVerifyRequest.getMobileNo());
        return loginVerifyJson;
    }
}
