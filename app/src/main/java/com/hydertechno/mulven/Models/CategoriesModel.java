package com.hydertechno.mulven.Models;

public class CategoriesModel {
    private int id;
    private String sku;
    private String category;
    private Integer sub_category;
    private Integer sub_sub_category;
    private String product_name;
    private Integer mrp_price;
    private Integer unit_price;
    private String feacher_image;

    public CategoriesModel(int id, String sku, String category, Integer sub_category, Integer sub_sub_category, String product_name, Integer mrp_price, Integer unit_price, String feacher_image) {
        this.id = id;
        this.sku = sku;
        this.category = category;
        this.sub_category = sub_category;
        this.sub_sub_category = sub_sub_category;
        this.product_name = product_name;
        this.mrp_price = mrp_price;
        this.unit_price = unit_price;
        this.feacher_image = feacher_image;
    }

    public CategoriesModel(int id, String sku, String product_name, Integer mrp_price, Integer unit_price, String feacher_image) {
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

    public Integer getMrp_price() {
        return mrp_price;
    }

    public Integer getUnit_price() {
        return unit_price;
    }

    public String getFeacher_image() {
        return feacher_image;
    }

    public String getCategory() {
        return category;
    }

    public Integer getSub_category() {
        return sub_category;
    }

    public Integer getSub_sub_category() {
        return sub_sub_category;
    }
}
