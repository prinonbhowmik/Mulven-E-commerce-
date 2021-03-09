package com.hydertechno.mulven.Models;

public class CategoriesModel {
    private int id;
    private String sku;
    private String product_name;
    private int mrp_price;
    private int unit_price;
    private String feacher_image;

    public CategoriesModel(int id, String sku, String product_name, int mrp_price, int unit_price, String feacher_image) {
        this.id = id;
        this.sku = sku;
        this.product_name = product_name;
        this.mrp_price = mrp_price;
        this.unit_price = unit_price;
        this.feacher_image = feacher_image;
    }

    public CategoriesModel() {
    }

    public int getId() {
        return id;
    }

    public String getSku() {
        return sku;
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

    public String getFeacher_image() {
        return feacher_image;
    }
}
