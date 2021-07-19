package com.hydertechno.mulven.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TransactionModel {
    @SerializedName("HoldingBal")
    @Expose
    private Double holdingBal;
    @SerializedName("ActiveVoucherBal")
    @Expose
    private Double activeVoucherBal;
    @SerializedName("ActiveVoucherWithBal")
    @Expose
    private Double activeVoucherWithBal;
    @SerializedName("ActiveAccountBal")
    @Expose
    private Double activeAccountBal;
    @SerializedName("ActiveAccountWithBal")
    @Expose
    private Double activeAccountWithBal;
    @SerializedName("ActiveCashbackBal")
    @Expose
    private Double activeCashbackBal;
    @SerializedName("ActiveCashbackWithBal")
    @Expose
    private Double activeCashbackWithBal;
    @SerializedName("transaction")
    @Expose
    private List<Transaction> transaction = null;

    public Double getHoldingBal() {
        return holdingBal;
    }

    public void setHoldingBal(Double holdingBal) {
        this.holdingBal = holdingBal;
    }

    public Double getActiveVoucherBal() {
        return activeVoucherBal;
    }

    public void setActiveVoucherBal(Double activeVoucherBal) {
        this.activeVoucherBal = activeVoucherBal;
    }

    public Double getActiveVoucherWithBal() {
        return activeVoucherWithBal;
    }

    public void setActiveVoucherWithBal(Double activeVoucherWithBal) {
        this.activeVoucherWithBal = activeVoucherWithBal;
    }

    public Double getActiveAccountBal() {
        return activeAccountBal;
    }

    public void setActiveAccountBal(Double activeAccountBal) {
        this.activeAccountBal = activeAccountBal;
    }

    public Double getActiveAccountWithBal() {
        return activeAccountWithBal;
    }

    public void setActiveAccountWithBal(Double activeAccountWithBal) {
        this.activeAccountWithBal = activeAccountWithBal;
    }

    public Double getActiveCashbackBal() {
        return activeCashbackBal;
    }

    public void setActiveCashbackBal(Double activeCashbackBal) {
        this.activeCashbackBal = activeCashbackBal;
    }

    public Double getActiveCashbackWithBal() {
        return activeCashbackWithBal;
    }

    public void setActiveCashbackWithBal(Double activeCashbackWithBal) {
        this.activeCashbackWithBal = activeCashbackWithBal;
    }

    public List<Transaction> getTransaction() {
        return transaction;
    }

    public void setTransaction(List<Transaction> transaction) {
        this.transaction = transaction;
    }
}
