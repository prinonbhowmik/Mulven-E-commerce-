package com.hydertechno.mulven.Models;

public class Variant {
    private String feature_name;
    private String price;

    public Variant(String feature_name, String price) {
        this.feature_name = feature_name;
        this.price = price;
    }

    public String getFeature_name() {
        return feature_name;
    }

    public String getPrice() {
        return price;
    }
}
