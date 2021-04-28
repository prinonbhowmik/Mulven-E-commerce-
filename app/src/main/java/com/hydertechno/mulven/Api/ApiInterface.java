package com.hydertechno.mulven.Api;

import com.hydertechno.mulven.Models.Campaign;
import com.hydertechno.mulven.Models.CampaignModel;
import com.hydertechno.mulven.Models.CancellationReasonModel;
import com.hydertechno.mulven.Models.CategoriesModel;
import com.hydertechno.mulven.Models.CategoryNamesModel;
import com.hydertechno.mulven.Models.ChangePasswordModel;
import com.hydertechno.mulven.Models.InvoiceDetailsModel;
import com.hydertechno.mulven.Models.OrderDetails;
import com.hydertechno.mulven.Models.OrderListModel;
import com.hydertechno.mulven.Models.OrderModel;
import com.hydertechno.mulven.Models.PlaceItemModel;
import com.hydertechno.mulven.Models.PlaceOrderModel;
import com.hydertechno.mulven.Models.ProductDetailsModel;
import com.hydertechno.mulven.Models.Sliderimage;
import com.hydertechno.mulven.Models.UserProfile;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
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
    Call<OrderModel> getOrderList(@Query("customer_id") int customerId,
                                  @Query("token") String token);

    @POST("login")
    @FormUrlEncoded
    Call<UserProfile> userLogin(@Field("phone") String phone,
                                @Field("password") String password);

    @POST("register")
    @FormUrlEncoded
    Call<UserProfile> registerUser(@Field("full_name") String name,
                                   @Field("phone") String phone,
                                   @Field("birthday") String dob,
                                   @Field("password") String pass,
                                   @Field("address") String address);

    @GET("logout?")
    Call<UserProfile> logoutUser(@Query("token") String token);

    @GET("user?")
    Call<UserProfile> getUserData(@Query("token") String token);

    @GET("order-details?")
    Call<InvoiceDetailsModel> getInvoiceDetails(@Query("invoice_id") String invoice_id,
                                                @Query("token") String token);

    @POST("delivery-address?")
    @FormUrlEncoded
    Call<OrderDetails> updateDeliverAddress(@Query("invoice_id") String name,
                                            @Query("token") String token,
                                            @Field("delivery_address") String address);

    @POST("profile-update")
    @FormUrlEncoded
    Call<UserProfile> updateProfileData(@Query("token") String token,
                                         @Field("full_name") String name,
                                         @Field("email") String email,
                                         @Field("address") String address);
    @Multipart
    @POST("profile-update")
    Call<UserProfile> updateProfileDataWithImage(@Part("token") RequestBody token,
                                                 @Part("full_name") RequestBody full_name,
                                                 @Part("email") RequestBody email,
                                                 @Part("address") RequestBody address,
                                                 @Part MultipartBody.Part user_photo);

    @GET("all-product")
    Call<List<CategoriesModel>> searchProduct();


    @POST("change-password")
    @FormUrlEncoded
    Call<ChangePasswordModel> changePassword(@Query("token") String token,
                                             @Field("old_pass") String old_pass,
                                             @Field("new_psss") String new_pass,
                                             @Field("con_pass") String con_pass);

    @GET("forgot-password-otp?phone")
    Call<UserProfile> matchOTP(@Query("phone") String phone);

    @POST("forgot-password")
    @FormUrlEncoded
    Call<ChangePasswordModel> changePassword(@Query("phone") String phone,
                                     @Field("password") String password);

    @GET("campaigns")
    Call<Campaign> getAllCampaigns();

    @POST("order")
    @FormUrlEncoded
    Call<PlaceOrderModel> placeOrder(@Query("token") String token,
                                     @Field("item") Map<String,String> item);



    @POST("order-cancel")
    Call<CancellationReasonModel> setCancelReason(@Query("token") String token,
                                                  @Query("order_id") String order_id,
                                                  @Query("cancel_reason") String cancel_reason);

}
