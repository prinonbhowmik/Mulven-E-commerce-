package com.hydertechno.mulven.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hydertechno.mulven.Adapters.OrderItemsAdapter;
import com.hydertechno.mulven.Adapters.OrderTimelineAdapter;
import com.hydertechno.mulven.Api.ApiUtils;
import com.hydertechno.mulven.Api.Config;
import com.hydertechno.mulven.Models.CategoriesModel;
import com.hydertechno.mulven.Models.InvoiceDetailsModel;
import com.hydertechno.mulven.Models.OrderItemsModel;
import com.hydertechno.mulven.Models.OrderTimelineModel;
import com.hydertechno.mulven.Models.UserProfile;
import com.hydertechno.mulven.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceOrderDetailsActivity extends AppCompatActivity {
    private TextView invoiceIdTV,orderTimeTV,vendorNameTV,vendorPhoneTV,vendorAddressTV,customerNameTV,
            customerPhoneTV,customerAddressTV,customerAddressEditTV,totalPaidTV;
    private Dialog dialog;
    private RatingBar ratingBar;
    private String token,OrderId;
    private ImageView vendorImageIV,customerImageIV;
    private SharedPreferences sharedPreferences;
    private List<InvoiceDetailsModel> invoiceDetailsModelList;
    private RecyclerView timelineRecyclerView,orderItemListRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order_details);
        init();
        Intent intent = getIntent();
        OrderId = intent.getStringExtra("OrderId");
        getInvoiceDetails();
        ratingBar.getRating();
        customerAddressEditTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(PlaceOrderDetailsActivity.this);
                dialog.setContentView(R.layout.edit_address_popup_design);
                dialog.setCancelable(true);
                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        });

        Call<UserProfile> call = ApiUtils.getUserService().getUserData(token);
        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if (response.isSuccessful()){
                    customerNameTV.setText(response.body().getFull_name());
                    customerPhoneTV.setText(response.body().getPhone());
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {

            }
        });
    }

    private void init() {
        sharedPreferences = getSharedPreferences("MyRef", MODE_PRIVATE);
        token = sharedPreferences.getString("token",null);
        invoiceIdTV=findViewById(R.id.InvoiceTV);
        orderTimeTV=findViewById(R.id.orderTimeTV);
        vendorNameTV=findViewById(R.id.vendorNameTV);
        vendorImageIV = findViewById(R.id.vendorImageTV);
        vendorPhoneTV=findViewById(R.id.vendorPhoneTV);
        vendorAddressTV=findViewById(R.id.vendorAddressTV);
        customerNameTV=findViewById(R.id.customerNameTV);
        customerImageIV=findViewById(R.id.customerImageTV);
        customerPhoneTV=findViewById(R.id.customerPhoneTV);
        customerAddressTV=findViewById(R.id.customerAddressTV);
        customerAddressEditTV=findViewById(R.id.customerAddressEditTV);
        totalPaidTV=findViewById(R.id.totalPaidTV);
        ratingBar=findViewById(R.id.ratingBar);
        timelineRecyclerView=findViewById(R.id.timelineRecyclerView);
        orderItemListRecyclerView=findViewById(R.id.orderItemListRecyclerView);
    }


    private void getInvoiceDetails() {
        invoiceIdTV.setText(OrderId);
        Call<InvoiceDetailsModel> call= ApiUtils.getUserService().getInvoiceDetails(OrderId,token);
        call.enqueue(new Callback<InvoiceDetailsModel>() {
            @Override
            public void onResponse(Call<InvoiceDetailsModel> call, Response<InvoiceDetailsModel> response) {
                InvoiceDetailsModel details=response.body();
                String shopName=details.getOrderDetails().getShop_name();
                String shopPhone=details.getOrderDetails().getSeller_phone();
                String shopAddress=details.getOrderDetails().getShop_address();
                String shopImage=details.getOrderDetails().getShop_logo();
                String orderTime=details.getOrderDetails().getTime();
                String orderDate=details.getOrderDetails().getDate();
                String customerAddress=details.getOrderDetails().getDelivery_address();
                vendorNameTV.setText(shopName);
                vendorPhoneTV.setText(shopPhone);
                vendorAddressTV.setText(shopAddress);
                orderTimeTV.setText(orderDate+" "+orderTime);
                customerAddressTV.setText(customerAddress);
                totalPaidTV.setText(details.getTotalPay());

                try{
                    Picasso.get()
                            .load(Config.IMAGE_LINE+shopImage)
                            .into(vendorImageIV);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //Order Item
                List<OrderItemsModel> orderItemsModelList=response.body().getItems();

                    OrderItemsAdapter orderItemsAdapter = new OrderItemsAdapter(orderItemsModelList, PlaceOrderDetailsActivity.this);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(PlaceOrderDetailsActivity.this, LinearLayoutManager.VERTICAL, false);
                    orderItemListRecyclerView.setLayoutManager(layoutManager);
                    orderItemListRecyclerView.setAdapter(orderItemsAdapter);
                    orderItemsAdapter.notifyDataSetChanged();



                //Time Line
                List<OrderTimelineModel> orderTimelineModelList=details.getTimeline();
                    OrderTimelineAdapter orderTimelineAdapter = new OrderTimelineAdapter(orderTimelineModelList, getApplicationContext());
                    LinearLayoutManager layoutManager2 = new LinearLayoutManager(PlaceOrderDetailsActivity.this, LinearLayoutManager.VERTICAL, false);
                    timelineRecyclerView.setLayoutManager(layoutManager2);
                    timelineRecyclerView.setAdapter(orderTimelineAdapter);
                    Collections.reverse(orderTimelineModelList);
                    orderTimelineAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<InvoiceDetailsModel> call, Throwable t) {

            }
        });
    }


    public void placeOrderDetailsBack(View view) {
        finish();
    }
}