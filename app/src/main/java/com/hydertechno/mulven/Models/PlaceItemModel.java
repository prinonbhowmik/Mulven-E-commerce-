package com.hydertechno.mulven.Models;

public class PlaceItemModel {
    private String item_id;
    private String pro_name;
    private String variant;
    private String size;
    private String color;
    private String price;
    private String order_from;
    private String store_id;
    private String quantity;

    public PlaceItemModel(String item_id, String pro_name, String variant, String size, String color, String price, String order_from, String store_id, String quantity) {
        this.item_id = item_id;
        this.pro_name = pro_name;
        this.variant = variant;
        this.size = size;
        this.color = color;
        this.price = price;
        this.order_from = order_from;
        this.store_id = store_id;
        this.quantity = quantity;
    }

    public String getItem_id() {
        return item_id;
    }

    public String getPro_name() {
        return pro_name;
    }

    public String getVariant() {
        return variant;
    }

    public String getSize() {
        return size;
    }

    public String getColor() {
        return color;
    }

    public String getPrice() {
        return price;
    }

    public String getOrder_from() {
        return order_from;
    }

    public String getStore_id() {
        return store_id;
    }

    public String getQuantity() {
        return quantity;
    }
}
