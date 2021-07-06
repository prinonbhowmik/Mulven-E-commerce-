package com.hydertechno.mulven.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InvoiceDetailsModel {
    @SerializedName("orderDetails")
    @Expose
    private OrderDetails orderDetails;
    @SerializedName("timeline")
    @Expose
    private List<OrderTimelineModel> timeline = null;
    @SerializedName("items")
    @Expose
    private List<OrderItemsModel> items = null;
    @SerializedName("totalPay")
    @Expose
    private Double totalPay;

    public OrderDetails getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(OrderDetails orderDetails) {
        this.orderDetails = orderDetails;
    }

    public List<OrderTimelineModel> getTimeline() {
        return timeline;
    }

    public void setTimeline(List<OrderTimelineModel> timeline) {
        this.timeline = timeline;
    }

    public List<OrderItemsModel> getItems() {
        return items;
    }

    public void setItems(List<OrderItemsModel> items) {
        this.items = items;
    }

    public Double getTotalPay() {
        return totalPay;
    }

    public void setTotalPay(Double totalPay) {
        this.totalPay = totalPay;
    }
}
