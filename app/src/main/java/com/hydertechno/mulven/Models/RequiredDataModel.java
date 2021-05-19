package com.hydertechno.mulven.Models;

public class RequiredDataModel extends com.sm.shurjopaysdk.model.RequiredDataModel {
    private String merchantName;
    private String merchantPass;
    private String uniqID;
    private double totalAmount;
    private String token;

    public RequiredDataModel(String merchantName, String merchantPass, String uniqID, double totalAmount, String token) {
        super(merchantName, merchantPass, uniqID, totalAmount, token);
    }


    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantPass() {
        return merchantPass;
    }

    public void setMerchantPass(String merchantPass) {
        this.merchantPass = merchantPass;
    }

    @Override
    public String getUniqID() {
        return uniqID;
    }

    @Override
    public void setUniqID(String uniqID) {
        this.uniqID = uniqID;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

