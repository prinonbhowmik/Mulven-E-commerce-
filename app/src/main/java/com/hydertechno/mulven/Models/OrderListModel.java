package com.hydertechno.mulven.Models;

public class OrderListModel {
    private String order_id;
    private String date;
    private String time;
    private String total;
    private String pay;
    private String orders_status;

    public OrderListModel(String order_id, String date, String time, String total, String pay, String orders_status) {
        this.order_id = order_id;
        this.date = date;
        this.time = time;
        this.total = total;
        this.pay = pay;
        this.orders_status = orders_status;
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

    public String getTotal() {
        return total;
    }

    public String getPay() {
        return pay;
    }
}
