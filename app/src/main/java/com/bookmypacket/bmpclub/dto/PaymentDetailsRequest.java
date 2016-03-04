package com.bookmypacket.bmpclub.dto;

import java.io.Serializable;

import static com.bookmypacket.bmpclub.utils.AppConstants.RegistrationValues.PAYMENT_DETAILS_STEP_NAME;

/**
 * Created by Manish on 8/1/16.
 */
public class PaymentDetailsRequest implements Serializable {
    private String stepName = PAYMENT_DETAILS_STEP_NAME;
    private Double amount;
    private String modeOfPayment;
    private String ebsResponseMessage;
    private String couponCode;
    private String transactionId;
    private String mobileNo;

    public Double getAmount() {return amount;}

    public void setAmount(Double amount) {this.amount = amount;}

    public String getModeOfPayment() {return modeOfPayment;}

    public void setModeOfPayment(String modeOfPayment) {this.modeOfPayment = modeOfPayment;}

    public String getEbsResponseMessage() {return ebsResponseMessage;}

    public void setEbsResponseMessage(String ebsResponseMessage) {this.ebsResponseMessage = ebsResponseMessage;}

    public String getCouponCode() {return couponCode;}

    public void setCouponCode(String couponCode) {this.couponCode = couponCode;}

    public String getTransactionId() {return transactionId;}

    public void setTransactionId(String transactionId) {this.transactionId = transactionId;}

    public String getMobileNo() {return mobileNo;}

    public void setMobileNo(String mobileNo) {this.mobileNo = mobileNo;}

    public String getStepName()
    {
        return stepName;
    }

    public void setStepName(String stepName)
    {
        this.stepName = stepName;
    }
}
