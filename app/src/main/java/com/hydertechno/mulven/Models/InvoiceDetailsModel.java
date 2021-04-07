package com.hydertechno.mulven.Models;

import java.util.List;

public class InvoiceDetailsModel {
    private OrderDetails orderDetails;
    private List<OrderTimelineModel> timeline;
    private List<OrderItemsModel> items;
    private String totalPay;

    public OrderDetails getOrderDetails() {
        return orderDetails;
    }

    public List<OrderTimelineModel> getTimeline() {
        return timeline;
    }

    public List<OrderItemsModel> getItems() {
        return items;
    }

    public String getTotalPay() {
        return totalPay;
    }
}
