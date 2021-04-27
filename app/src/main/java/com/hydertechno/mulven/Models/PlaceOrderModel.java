package com.hydertechno.mulven.Models;

import org.json.JSONObject;

import java.util.List;

public class PlaceOrderModel {

    private int status;
    private List<PlaceItemModel> item;

    public int getStatus() {
        return status;
    }

    public List<PlaceItemModel> getItem() {
        return item;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setItem(List<PlaceItemModel> item) {
        this.item = item;
    }
}
