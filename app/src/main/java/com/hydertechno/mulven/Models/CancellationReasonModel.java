package com.hydertechno.mulven.Models;

public class CancellationReasonModel {
    public String token;
    private String order_id;
    private String cancel_reason;
    private int status;

    public CancellationReasonModel(String token, String order_id, String cancel_reason, int status) {
        this.token = token;
        this.order_id = order_id;
        this.cancel_reason = cancel_reason;
        this.status = status;
    }

    public CancellationReasonModel() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getCancel_reason() {
        return cancel_reason;
    }

    public void setCancel_reason(String cancel_reason) {
        this.cancel_reason = cancel_reason;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

