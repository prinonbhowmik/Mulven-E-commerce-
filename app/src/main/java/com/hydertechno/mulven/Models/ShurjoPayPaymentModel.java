package com.hydertechno.mulven.Models;

public class ShurjoPayPaymentModel {
    private String token;
    private String orderId;
    private String amount;
    private String sp_payment_option;
    private String bank_tx_id;
    private String tx_id;

    public ShurjoPayPaymentModel() {
    }

    public ShurjoPayPaymentModel(String token, String orderId, String amount, String sp_payment_option, String bank_tx_id, String tx_id) {
        this.token = token;
        this.orderId = orderId;
        this.amount = amount;
        this.sp_payment_option = sp_payment_option;
        this.bank_tx_id = bank_tx_id;
        this.tx_id = tx_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSp_payment_option() {
        return sp_payment_option;
    }

    public void setSp_payment_option(String sp_payment_option) {
        this.sp_payment_option = sp_payment_option;
    }

    public String getBank_tx_id() {
        return bank_tx_id;
    }

    public void setBank_tx_id(String bank_tx_id) {
        this.bank_tx_id = bank_tx_id;
    }

    public String getTx_id() {
        return tx_id;
    }

    public void setTx_id(String tx_id) {
        this.tx_id = tx_id;
    }
}



