package com.bookmypacket.bmpclub.dto;

import java.io.Serializable;

/**
 * Created by Manish on 07-01-2016.
 */
public class Registration implements Serializable
{
    private String   firstName;
    private String   lastName;
    private String   emailId;
    private String   mobileNo;
    private String   taxationId;
    private String   taxationIdType;
    private String   idProofType;
    private String   idProofNumber;
    private String   vehicleType;
    private String   vehicleRegistrationNo;
    private String   empolyeeId;
    private String   addressLine1;
    private String   addressLine2;
    private String   pinCode;
    private String   city;
    private String   state;
    private String   banckAccountNo;
    private String   nameInBankAccount;
    private String   bankName;
    private String   ifscCode;
    private String   bankCity;
    private String[] deliveryServiceAreas;
    private String[] pickupServiceAreas;

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

    public String getIdProofType()
    {
        return idProofType;
    }

    public void setIdProofType(String idProofType)
    {
        this.idProofType = idProofType;
    }

    public String getIdProofNumber()
    {
        return idProofNumber;
    }

    public void setIdProofNumber(String idProofNumber)
    {
        this.idProofNumber = idProofNumber;
    }

    public String getVehicleType()
    {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType)
    {
        this.vehicleType = vehicleType;
    }

    public String getVehicleRegistrationNo()
    {
        return vehicleRegistrationNo;
    }

    public void setVehicleRegistrationNo(String vehicleRegistrationNo)
    {
        this.vehicleRegistrationNo = vehicleRegistrationNo;
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

    public String[] getDeliveryServiceAreas()
    {
        return deliveryServiceAreas;
    }

    public void setDeliveryServiceAreas(String[] deliveryServiceAreas)
    {
        this.deliveryServiceAreas = deliveryServiceAreas;
    }

    public String[] getPickupServiceAreas()
    {
        return pickupServiceAreas;
    }

    public void setPickupServiceAreas(String[] pickupServiceAreas)
    {
        this.pickupServiceAreas = pickupServiceAreas;
    }
}
