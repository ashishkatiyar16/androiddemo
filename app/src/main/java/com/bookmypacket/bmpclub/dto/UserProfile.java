package com.bookmypacket.bmpclub.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Manish on 23-01-2016.
 */
public class UserProfile implements Serializable
{

    private String firstName;
    private String lastName;
    private String emailId;
    private String mobileNo;
    private String otpCode;
    private String ebsResponseMessage;
    private String amount;
    private String modeOfPayment;
    private String transactionId;
    private String couponCode;
    private String userId;
    private String authorizationDateTime;
    private String status;
    private String policy;
    private String taxationId;
    private String taxationIdType;
    private String idProofNumber;
    private String cancelCheque;
    private String vehicleType;
    private String empolyeeId;
    private String addressLine1;
    private String addressLine2;
    private String pinCode;
    private String city;
    private String state;
    private String banckAccountNo;
    private String nameInBankAccount;
    private String bankName;
    private String ifscCode;
    private String bankCity;
    private List<String> deliveryServiceAreas = new ArrayList<String>();
    private List<String> pickupServiceAreas   = new ArrayList<String>();

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getEmailId()
    {
        return emailId;
    }

    public void setEmailId(String emailId)
    {
        this.emailId = emailId;
    }

    public String getMobileNo()
    {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo)
    {
        this.mobileNo = mobileNo;
    }

    public String getOtpCode()
    {
        return otpCode;
    }

    public void setOtpCode(String otpCode)
    {
        this.otpCode = otpCode;
    }

    public String getEbsResponseMessage()
    {
        return ebsResponseMessage;
    }

    public void setEbsResponseMessage(String ebsResponseMessage)
    {
        this.ebsResponseMessage = ebsResponseMessage;
    }

    public String getAmount()
    {
        return amount;
    }

    public void setAmount(String amount)
    {
        this.amount = amount;
    }

    public String getModeOfPayment()
    {
        return modeOfPayment;
    }

    public void setModeOfPayment(String modeOfPayment)
    {
        this.modeOfPayment = modeOfPayment;
    }

    public String getTransactionId()
    {
        return transactionId;
    }

    public void setTransactionId(String transactionId)
    {
        this.transactionId = transactionId;
    }

    public String getCouponCode()
    {
        return couponCode;
    }

    public void setCouponCode(String couponCode)
    {
        this.couponCode = couponCode;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getAuthorizationDateTime()
    {
        return authorizationDateTime;
    }

    public void setAuthorizationDateTime(String authorizationDateTime)
    {
        this.authorizationDateTime = authorizationDateTime;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getPolicy()
    {
        return policy;
    }

    public void setPolicy(String policy)
    {
        this.policy = policy;
    }

    public String getTaxationId()
    {
        return taxationId;
    }

    public void setTaxationId(String taxationId)
    {
        this.taxationId = taxationId;
    }

    public String getTaxationIdType()
    {
        return taxationIdType;
    }

    public void setTaxationIdType(String taxationIdType)
    {
        this.taxationIdType = taxationIdType;
    }

    public String getIdProofNumber()
    {
        return idProofNumber;
    }

    public void setIdProofNumber(String idProofNumber)
    {
        this.idProofNumber = idProofNumber;
    }

    public String getCancelCheque()
    {
        return cancelCheque;
    }

    public void setCancelCheque(String cancelCheque)
    {
        this.cancelCheque = cancelCheque;
    }

    public String getVehicleType()
    {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType)
    {
        this.vehicleType = vehicleType;
    }

    public String getEmpolyeeId()
    {
        return empolyeeId;
    }

    public void setEmpolyeeId(String empolyeeId)
    {
        this.empolyeeId = empolyeeId;
    }

    public String getAddressLine1()
    {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1)
    {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2()
    {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2)
    {
        this.addressLine2 = addressLine2;
    }

    public String getPinCode()
    {
        return pinCode;
    }

    public void setPinCode(String pinCode)
    {
        this.pinCode = pinCode;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public String getBanckAccountNo()
    {
        return banckAccountNo;
    }

    public void setBanckAccountNo(String banckAccountNo)
    {
        this.banckAccountNo = banckAccountNo;
    }

    public String getNameInBankAccount()
    {
        return nameInBankAccount;
    }

    public void setNameInBankAccount(String nameInBankAccount)
    {
        this.nameInBankAccount = nameInBankAccount;
    }

    public String getBankName()
    {
        return bankName;
    }

    public void setBankName(String bankName)
    {
        this.bankName = bankName;
    }

    public String getIfscCode()
    {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode)
    {
        this.ifscCode = ifscCode;
    }

    public String getBankCity()
    {
        return bankCity;
    }

    public void setBankCity(String bankCity)
    {
        this.bankCity = bankCity;
    }

    public List<String> getDeliveryServiceAreas()
    {
        return deliveryServiceAreas;
    }

    public void setDeliveryServiceAreas(List<String> deliveryServiceAreas)
    {
        this.deliveryServiceAreas = deliveryServiceAreas;
    }

    public List<String> getPickupServiceAreas()
    {
        return pickupServiceAreas;
    }

    public void setPickupServiceAreas(List<String> pickupServiceAreas)
    {
        this.pickupServiceAreas = pickupServiceAreas;
    }
}
