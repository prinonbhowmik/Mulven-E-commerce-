package com.hydertechno.mulven.Models;

public class CategoryNamesModel {
    private String id;
    private String category_name;
    private int status;

    public CategoryNamesModel(String id, String category_name, int status) {
        this.id = id;
        this.category_name = category_name;
        this.status = status;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
