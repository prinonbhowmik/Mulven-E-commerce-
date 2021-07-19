package com.hydertechno.mulven.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RefundSettlementResponse {
    @SerializedName("bkash")
    @Expose
    private SettlementModel bkash;
    @SerializedName("nagad")
    @Expose
    private SettlementModel nagad;
    @SerializedName("bank")
    @Expose
    private SettlementModel bank;

    public SettlementModel getBkash() {
        return bkash;
    }

    public void setBkash(SettlementModel bkash) {
        this.bkash = bkash;
    }

    public SettlementModel getNagad() {
        return nagad;
    }

    public void setNagad(SettlementModel nagad) {
        this.nagad = nagad;
    }

    public SettlementModel getBank() {
        return bank;
    }

    public void setBank(SettlementModel bank) {
        this.bank = bank;
    }

}
