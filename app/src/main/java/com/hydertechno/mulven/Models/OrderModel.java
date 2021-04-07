package com.hydertechno.mulven.Models;

import java.util.ArrayList;
import java.util.List;

public class OrderModel {
    private String status;
    private List<OrderListModel> items = new ArrayList<>();

    public String getStatus() {
        return status;
    }

    public List<OrderListModel> getItems() {
        return items;
    }
}
