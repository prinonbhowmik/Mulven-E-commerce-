package com.hydertechno.mulven.Models;

public class ProductImagesModel {
    private String id;
    private int image;

    public ProductImagesModel(String id, int image) {
        this.id = id;
        this.image = image;
    }

    public ProductImagesModel() {
    }

    public String getId() {
        return id;
    }

    public int getImage() {
        return image;
    }
}
