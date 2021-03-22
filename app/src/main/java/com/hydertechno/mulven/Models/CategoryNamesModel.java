package com.hydertechno.mulven.Models;

public class CategoryNamesModel {
    private String id;
    private String category_name;
    private String image;

    public CategoryNamesModel(String id, String category_name, String image) {
        this.id = id;
        this.category_name = category_name;
        this.image = image;
    }

    public CategoryNamesModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
