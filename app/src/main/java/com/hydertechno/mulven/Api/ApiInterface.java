package com.hydertechno.mulven.Api;

import com.hydertechno.mulven.Models.CategoryNamesModel;
import com.hydertechno.mulven.Models.Sliderimage;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("slider")
    Call<List<Sliderimage>> getSliderImage();

    @GET("categories")
    Call<List<CategoryNamesModel>> getProductsCategories();
}
