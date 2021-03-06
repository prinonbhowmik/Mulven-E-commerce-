package com.hydertechno.mulven.Models;

public class OrderListModel {
    private String order_id;
    private String date;
    private String time;
    private String orders_status;
    private String pay_status;

    public OrderListModel(String order_id, String date, String time, String orders_status, String pay_status) {
        this.order_id = order_id;
        this.date = date;
        this.time = time;
        this.orders_status = orders_status;
        this.pay_status = pay_status;
    }

    public OrderListModel() {
    }

    public String getOrder_id() {
        return order_id;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getOrders_status() {
        return orders_status;
    }

    public String getPay_status() {
        return pay_status;
    }
}
