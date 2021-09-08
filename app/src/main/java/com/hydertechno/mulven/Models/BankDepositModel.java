package com.hydertechno.mulven.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BankDepositModel implements Serializable {
    @SerializedName("depo_name")
    @Expose
    private String depoName;
    @SerializedName("depo_phone")
    @Expose
    private String depoPhone;
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("payment_option")
    @Expose
    private String paymentOption;
    @SerializedName("bank_tx_id")
    @Expose
    private String bankTxId;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("depo_slip")
    @Expose
    private String depoSlip;

    public String getDepoName() {
        return depoName;
    }

    public void setDepoName(String depoName) {
        this.depoName = depoName;
    }

    public String getDepoPhone() {
        return depoPhone;
    }

    public void setDepoPhone(String depoPhone) {
        this.depoPhone = depoPhone;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPaymentOption() {
        return paymentOption;
    }

    public void setPaymentOption(String paymentOption) {
        this.paymentOption = paymentOption;
    }

    public String getBankTxId() {
        return bankTxId;
    }

    public void setBankTxId(String bankTxId) {
        this.bankTxId = bankTxId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDepoSlip() {
        return depoSlip;
    }

    public void setDepoSlip(String depoSlip) {
        this.depoSlip = depoSlip;
    }
}
