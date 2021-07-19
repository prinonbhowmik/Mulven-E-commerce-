package com.hydertechno.mulven.Models;

import java.util.List;

public class CampaignProductsModel {

    private List<CampaignCategoriesModel> category;
    private List<CategoriesModel> allitems;

    public CampaignProductsModel(List<CampaignCategoriesModel> category, List<CategoriesModel> allitems) {
        this.category = category;
        this.allitems = allitems;
    }

    public List<CampaignCategoriesModel> getCategory() {
        return category;
    }

    public List<CategoriesModel> getAllitems() {
        return allitems;
    }
}
