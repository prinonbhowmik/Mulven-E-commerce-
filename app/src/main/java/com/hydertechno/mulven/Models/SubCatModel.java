package com.hydertechno.mulven.Models;

public class SubCatModel {

    private int id;
    private String category_name;
    private String image;

    public SubCatModel(int id, String category_name, String image) {
        this.id = id;
        this.category_name = category_name;
        this.image = image;
    }

    public SubCatModel() {
    }

    public int getId() {
        return id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public String getImage() {
        return image;
    }
}
