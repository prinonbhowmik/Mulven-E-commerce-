package com.hydertechno.mulven.Models;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailsModel {
    private int id;
    private int category_id;
    private String sku;
    private String product_name;
    private int mrp_price;
    private int unit_price;
    private String shop_name;
    private String shop_logo;
    private String shop_address;
    private String brand;
    private String feacher_image;
    private List<ProductFeatureModel> product_feature = new ArrayList<>();
    private List<ImageGalleryModel> image_gallery = new ArrayList<>();
    private List<ProductSizeModel> product_size = new ArrayList<>();
    private List<ProductColorModel> product_color = new ArrayList<>();
    private List<ProductVariantModel> variant = new ArrayList<>();

    public int getId() {
        return id;
    }

    public int getCategory_id() {
        return category_id;
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

    public String getShop_name() {
        return shop_name;
    }

    public String getShop_logo() {
        return shop_logo;
    }

    public String getShop_address() {
        return shop_address;
    }

    public String getBrand() {
        return brand;
    }

    public String getFeacher_image() {
        return feacher_image;
    }

    public List<ProductFeatureModel> getProduct_feature() {
        return product_feature;
    }

    public List<ImageGalleryModel> getImage_gallery() {
        return image_gallery;
    }

    public List<ProductSizeModel> getProduct_size() {
        return product_size;
    }

    public List<ProductColorModel> getProduct_color() {
        return product_color;
    }

    public List<ProductVariantModel> getProductVariantModel() {
        return variant;
    }
}
