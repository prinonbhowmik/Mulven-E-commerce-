package com.hydertechno.mulven.Api;

import com.hydertechno.mulven.Models.CategoriesModel;
import com.hydertechno.mulven.Models.CategoryNamesModel;
import com.hydertechno.mulven.Models.OrderListModel;
import com.hydertechno.mulven.Models.ProductDetailsModel;
import com.hydertechno.mulven.Models.Sliderimage;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("slider")
    Call<List<Sliderimage>> getSliderImage(@Query("post_type") String post_type);

    @GET("categories")
    Call<List<CategoryNamesModel>> getProductsCategories();

    @GET("home?")
    Call<List<CategoriesModel>> getCategories(@Query("category") int categoryId);

     @GET("product-detais?")
    Call<ProductDetailsModel> getProd_details(@Query("id") int id);

    @GET("order-list?")
    Call<List<OrderListModel>> getOrderList(@Query("customer_id") int customerId);


}
