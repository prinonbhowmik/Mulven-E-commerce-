package com.hydertechno.mulven.Models;

public class CartProductModel {
    private int id;
    private String product_name;
    private int mrp_price;
    private int unit_price;
    private String size;
    private String color;
    private String variant;
    private String shop_name;
    private int quantity;
    private String image;

    public CartProductModel(int id, String product_name, int mrp_price, int unit_price, String size, String color, String variant, String shop_name, int quantity, String image) {
        this.id = id;
        this.product_name = product_name;
        this.mrp_price = mrp_price;
        this.unit_price = unit_price;
        this.size = size;
        this.color = color;
        this.variant = variant;
        this.shop_name = shop_name;
        this.quantity = quantity;
        this.image = image;
    }

    public CartProductModel() {
    }

    public int getId() {
        return id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public int getMrp_price() {
        return mrp_price;
    }

    public int getUnit_price() {
        return unit_price;
    }

    public String getSize() {
        return size;
    }

    public String getColor() {
        return color;
    }

    public String getVariant() {
        return variant;
    }

    public String getShop_name() {
        return shop_name;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getImage() {
        return image;
    }
}
