package com.hydertechno.mulven.Models;

public class OrderDetails {
    private Integer id;
    private String order_id;
    private Integer vendor_id;
    private Integer customer_id;
    private String date;
    private String time;
    private String orders_status;
    private String delivery_address;
    private String cancel_reason;
    private String date_time;
    private String shop_name;
    private String seller_phone;
    private String shop_address;
    private String shop_logo;
    private String status;

    public OrderDetails(Integer id, String order_id, Integer vendor_id, Integer customer_id, String date, String time, String orders_status, String delivery_address, String cancel_reason, String date_time, String shop_name, String seller_phone, String shop_address, String shop_logo, String status) {
        this.id = id;
        this.order_id = order_id;
        this.vendor_id = vendor_id;
        this.customer_id = customer_id;
        this.date = date;
        this.time = time;
        this.orders_status = orders_status;
        this.delivery_address = delivery_address;
        this.cancel_reason = cancel_reason;
        this.date_time = date_time;
        this.shop_name = shop_name;
        this.seller_phone = seller_phone;
        this.shop_address = shop_address;
        this.shop_logo = shop_logo;
        this.status = status;
    }

    public OrderDetails() {
    }

    public Integer getId() {
        return id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public Integer getVendor_id() {
        return vendor_id;
    }

    public Integer getCustomer_id() {
        return customer_id;
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

    public String getDelivery_address() {
        return delivery_address;
    }

    public String getCancel_reason() {
        return cancel_reason;
    }

    public String getDate_time() {
        return date_time;
    }

    public String getShop_name() {
        return shop_name;
    }

    public String getSeller_phone() {
        return seller_phone;
    }

    public String getShop_address() {
        return shop_address;
    }

    public String getShop_logo() {
        return shop_logo;
    }

    public String getStatus() {
        return status;
    }
}
