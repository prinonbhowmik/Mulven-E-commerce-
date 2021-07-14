package com.hydertechno.mulven.Api;

import com.hydertechno.mulven.Models.Campaign;
import com.hydertechno.mulven.Models.CampaignProductsModel;
import com.hydertechno.mulven.Models.CancellationReasonModel;
import com.hydertechno.mulven.Models.CategoriesModel;
import com.hydertechno.mulven.Models.CategoryNamesModel;
import com.hydertechno.mulven.Models.ChangePasswordModel;
import com.hydertechno.mulven.Models.ExistingIssueModel;
import com.hydertechno.mulven.Models.InvoiceDetailsModel;
import com.hydertechno.mulven.Models.NotificationModel;
import com.hydertechno.mulven.Models.OrderDetails;
import com.hydertechno.mulven.Models.OrderListModel;
import com.hydertechno.mulven.Models.PlaceOrderModel;
import com.hydertechno.mulven.Models.PostRefundSettlementResponse;
import com.hydertechno.mulven.Models.ProductDetailsModel;
import com.hydertechno.mulven.Models.RefundSettlementResponse;
import com.hydertechno.mulven.Models.RequestReportBody;
import com.hydertechno.mulven.Models.ResponseUpdate;
import com.hydertechno.mulven.Models.SettlementModel;
import com.hydertechno.mulven.Models.Sliderimage;
import com.hydertechno.mulven.Models.SubCatModel;
import com.hydertechno.mulven.Models.TransactionModel;
import com.hydertechno.mulven.Models.UserProfile;
import com.hydertechno.mulven.Models.WalletPayStatus;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("version-check")
    Call<ResponseUpdate> versionCheck();

    @GET("slider")
    Call<List<Sliderimage>> getSliderImage(@Query("post_type") String post_type);

    @GET("categories")
    Call<List<CategoryNamesModel>> getProductsCategories();

    @GET("home?")
    Call<List<CategoriesModel>> getCategories(@Query("category") int categoryId);

    @GET("product-detais?")
    Call<ProductDetailsModel> getProd_details(@Query("id") int id);

    @GET("campaigns-product-detais?")
    Call<ProductDetailsModel> getCampaignProd_details(@Query("id") int id,
                                                       @Query("sku") String  sku);

    @GET("order-list?")
    Call<List<OrderListModel>> getOrderList(@Query("token") String token);

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

    @GET("campaigns-all-item")
    Call<CampaignProductsModel> getCampaignItem(@Query("id") int order_id);

    @POST("order")
    @FormUrlEncoded
    Call<PlaceOrderModel> placeOrder(@Query("token") String token,
                                     @Field("item") ArrayList<JSONArray> item);



    @GET("order-cancel")
    Call<CancellationReasonModel> setCancelReason(@Query("token") String token,
                                                  @Query("order_id") String order_id,
                                                  @Query("cancel_reason") String cancel_reason);

    @GET("order-delivered")
    Call<CancellationReasonModel> setOrderDelivered(@Query("token") String token,
                                                  @Query("order_id") String order_id);

    @GET("sub-categories?categorie_id=4")
    Call<List<SubCatModel>> getSubCat(@Query("categorie_id") int categorie_id);


    @GET("shurjo-pay")
    Call<ResponseBody> setShurjo_Pay(@Query("token") String token,
                                     @Query("order_id") String order_id,
                                     @Query("amount") String amount,
                                     @Query("sp_payment_option") String sp_payment_option,
                                     @Query("bank_tx_id") String bank_tx_id,
                                     @Query("tx_id") String tx_id);

    @GET("notification")
    Call<List<NotificationModel>> getNotification(@Query("token") String token);

    @GET("transaction")
    Call<TransactionModel> getTransactions(@Query("token") String token);

    @GET("wallet-pay")
    Call<WalletPayStatus> checkWalletPay(
            @Query("token") String token,
            @Query("pay_method") String pay_method,
            @Query("pay_am") String pay_am,
            @Query("order_id") String order_id
    );


    @GET("get-refund-settlements")
    Call<RefundSettlementResponse> getRefundSettlements(
            @Query("token") String token
    );

    @GET("refund-settlements-otp")
    Call<PostRefundSettlementResponse> getRefundOTP(
            @Query("token") String token
    );

    @POST("post-refund-settlements")
    Call<PostRefundSettlementResponse> postRefundSettlement(
            @Query("token") String token,
            @Body SettlementModel body
    );

    @POST("post-report-issue")
    Call<PostRefundSettlementResponse> postReportIssue(
            @Query("token") String token,
            @Body RequestReportBody body
    );

    @GET("get-report-issue")
    Call<List<ExistingIssueModel>> getReportIssue(
            @Query("token") String token,
            @Query("order_id") String orderId
    );
}
