package com.hydertechno.mulven.Models;

public class CategoriesModel {
    private String id;
    private String image;
    private String categoriesName;
    private int icon;


    public CategoriesModel(String id, String categoriesName, int icon) {
        this.id = id;
        this.categoriesName = categoriesName;
        this.icon = icon;
    }



    public CategoriesModel() {
    }

    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getCategoriesName() {
        return categoriesName;
    }

    public int getIcon() {
        return icon;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setCategoriesName(String categoriesName) {
        this.categoriesName = categoriesName;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
