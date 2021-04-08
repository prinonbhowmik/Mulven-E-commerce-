package com.hydertechno.mulven.Models;

import android.widget.SearchView;

public class OrderItemsModel {
    private Integer id;
    private String session_id;
    private Integer item_it;
    private String pro_name;
    private Integer store_id;
    private Integer quantity;
    private String  variant;
    private Integer price;
    private String size;
    private String color;
    private String date;
    private String status;
    private String feacher_image;

    public OrderItemsModel(Integer id, String session_id, Integer item_it, String pro_name,
                           Integer store_id, Integer quantity, String variant, Integer price,
                           String size, String color, String date, String status, String feacher_image) {
        this.id = id;
        this.session_id = session_id;
        this.item_it = item_it;
        this.pro_name = pro_name;
        this.store_id = store_id;
        this.quantity = quantity;
        this.variant = variant;
        this.price = price;
        this.size = size;
        this.color = color;
        this.date = date;
        this.status = status;
        this.feacher_image = feacher_image;
    }

    public OrderItemsModel() {
    }

    public Integer getId() {
        return id;
    }

    public String getSession_id() {
        return session_id;
    }

    public Integer getItem_it() {
        return item_it;
    }

    public String getPro_name() {
        return pro_name;
    }

    public Integer getStore_id() {
        return store_id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getVariant() {
        return variant;
    }

    public Integer getPrice() {
        return price;
    }

    public String getSize() {
        return size;
    }

    public String getColor() {
        return color;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public String getFeacher_image() {
        return feacher_image;
    }
}
