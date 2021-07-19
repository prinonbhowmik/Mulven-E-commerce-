package com.hydertechno.mulven.Models;

import android.widget.SearchView;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderItemsModel {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("session_id")
    @Expose
    private String sessionId;
    @SerializedName("item_it")
    @Expose
    private Integer itemIt;
    @SerializedName("pro_name")
    @Expose
    private String proName;
    @SerializedName("order_from")
    @Expose
    private String orderFrom;
    @SerializedName("store_id")
    @Expose
    private Integer storeId;
    @SerializedName("category_id")
    @Expose
    private Integer categoryId;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("variant")
    @Expose
    private String variant;
    @SerializedName("price")
    @Expose
    private Double price;
    @SerializedName("size")
    @Expose
    private String size;
    @SerializedName("color")
    @Expose
    private String color;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("feacher_image")
    @Expose
    private String feacherImage;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Integer getItemIt() {
        return itemIt;
    }

    public void setItemIt(Integer itemIt) {
        this.itemIt = itemIt;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getOrderFrom() {
        return orderFrom;
    }

    public void setOrderFrom(String orderFrom) {
        this.orderFrom = orderFrom;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFeacherImage() {
        return feacherImage;
    }

    public void setFeacherImage(String feacherImage) {
        this.feacherImage = feacherImage;
    }
}
