package com.hydertechno.mulven.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.hydertechno.mulven.Adapters.OrderItemsAdapter;
import com.hydertechno.mulven.Adapters.OrderTimelineAdapter;
import com.hydertechno.mulven.Api.ApiUtils;
import com.hydertechno.mulven.Api.Config;
import com.hydertechno.mulven.Models.CategoriesModel;
import com.hydertechno.mulven.Models.InvoiceDetailsModel;
import com.hydertechno.mulven.Models.OrderDetails;
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
    private TextView invoiceIdTV, orderTimeTV, vendorNameTV, vendorPhoneTV, vendorAddressTV, customerNameTV,
            customerPhoneTV, customerAddressTV, customerAddressEditTV, totalPaidTV,makePaymentTV;
    public static TextView totalPriceTv, dueTV, orderStatusTV;
    public static int totalPay;
    private Dialog dialog;
    private RatingBar ratingBar;
    private String token, OrderId,paymentOrderStatus;
    private ImageView vendorImageIV, customerImageIV, moreIcon;
    private SharedPreferences sharedPreferences;
    private List<InvoiceDetailsModel> invoiceDetailsModelList;
    private FrameLayout frame_layout2;
    private RecyclerView timelineRecyclerView, orderItemListRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order_details);
        init();
        Intent intent = getIntent();
        OrderId = intent.getStringExtra("OrderId");

        getInvoiceDetails();
        ratingBar.getRating();
        moreIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(PlaceOrderDetailsActivity.this, moreIcon);
                popup.getMenuInflater().inflate(R.menu.cancel_product_menu, popup.getMenu());
                popup.show();
            }
        });

        Call<UserProfile> call = ApiUtils.getUserService().getUserData(token);
        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if (response.isSuccessful()) {
                    customerNameTV.setText(response.body().getFull_name());
                    customerPhoneTV.setText(response.body().getPhone());
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {

            }
        });

        customerAddressEditTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(PlaceOrderDetailsActivity.this);
                dialog.setContentView(R.layout.edit_address_popup_design);
                dialog.setCancelable(true);
                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                EditText delivery_ET = dialog.findViewById(R.id.delivery_ET);
                TextView saveAddressTV = dialog.findViewById(R.id.saveAddressTV);

                saveAddressTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String address = delivery_ET.getText().toString();
                        if (TextUtils.isEmpty(address)) {
                            Toast.makeText(PlaceOrderDetailsActivity.this, "Please provide delivery address!", Toast.LENGTH_LONG).show();
                        } else {
                            Call<OrderDetails> call = ApiUtils.getUserService().updateDeliverAddress(OrderId, token, address);
                            call.enqueue(new Callback<OrderDetails>() {
                                @Override
                                public void onResponse(Call<OrderDetails> call, Response<OrderDetails> response) {
                                    if (response.isSuccessful()) {
                                        String status = response.body().getStatus();
                                        if (status.equals("1")) {
                                            Toast.makeText(PlaceOrderDetailsActivity.this, "Delivery address updated!", Toast.LENGTH_SHORT).show();
                                            getInvoiceDetails();
                                            dialog.dismiss();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<OrderDetails> call, Throwable t) {

                                }
                            });
                        }
                    }
                });
            }
        });
    }

    private void init() {
        sharedPreferences = getSharedPreferences("MyRef", MODE_PRIVATE);
        token = sharedPreferences.getString("token", null);
        invoiceIdTV = findViewById(R.id.InvoiceTV);
        moreIcon = findViewById(R.id.moreIcon);
        orderTimeTV = findViewById(R.id.orderTimeTV);
        vendorNameTV = findViewById(R.id.vendorNameTV);
        vendorImageIV = findViewById(R.id.vendorImageTV);
        vendorPhoneTV = findViewById(R.id.vendorPhoneTV);
        vendorAddressTV = findViewById(R.id.vendorAddressTV);
        customerNameTV = findViewById(R.id.customerNameTV);
        customerImageIV = findViewById(R.id.customerImageTV);
        customerPhoneTV = findViewById(R.id.customerPhoneTV);
        customerAddressTV = findViewById(R.id.customerAddressTV);
        customerAddressEditTV = findViewById(R.id.customerAddressEditTV);
        totalPaidTV = findViewById(R.id.totalPaidTV);
        totalPriceTv = findViewById(R.id.totalPriceTv);
        orderStatusTV = findViewById(R.id.orderStatusTV);
        makePaymentTV = findViewById(R.id.makePaymentTV);
        paymentOrderStatus=orderStatusTV.getText().toString();
        switch (paymentOrderStatus) {
            case "Unpaid":
                makePaymentTV.setVisibility(View.VISIBLE);
                orderStatusTV.setTextColor(Color.parseColor("#DB4437"));
                break;
            case "Partial Paid":
                makePaymentTV.setVisibility(View.VISIBLE);
                orderStatusTV.setTextColor(Color.parseColor("#4285F4"));
                break;
            case "Paid":
                makePaymentTV.setVisibility(View.GONE);
                orderStatusTV.setTextColor(Color.parseColor("#0F9D58"));
                break;
        }
        dueTV = findViewById(R.id.dueTV);
        ratingBar = findViewById(R.id.ratingBar);
        timelineRecyclerView = findViewById(R.id.timelineRecyclerView);
        orderItemListRecyclerView = findViewById(R.id.orderItemListRecyclerView);
    }


    private void getInvoiceDetails() {
        invoiceIdTV.setText(OrderId);
        Call<InvoiceDetailsModel> call = ApiUtils.getUserService().getInvoiceDetails(OrderId, token);
        call.enqueue(new Callback<InvoiceDetailsModel>() {
            @Override
            public void onResponse(Call<InvoiceDetailsModel> call, Response<InvoiceDetailsModel> response) {
                InvoiceDetailsModel details = response.body();
                String shopName = details.getOrderDetails().getShop_name();
                String shopPhone = details.getOrderDetails().getSeller_phone();
                String shopAddress = details.getOrderDetails().getShop_address();
                String shopImage = details.getOrderDetails().getShop_logo();
                String orderTime = details.getOrderDetails().getTime();
                String orderDate = details.getOrderDetails().getDate();
                String customerAddress = details.getOrderDetails().getDelivery_address();
                vendorNameTV.setText(shopName);
                vendorPhoneTV.setText(shopPhone);
                vendorAddressTV.setText(shopAddress);
                orderTimeTV.setText(orderDate + " " + orderTime);
                customerAddressTV.setText(customerAddress);
                totalPaidTV.setText(details.getTotalPay());
                totalPay = Integer.parseInt(details.getTotalPay());

                try {
                    Picasso.get()
                            .load(Config.IMAGE_LINE + shopImage)
                            .into(vendorImageIV);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //Order Item
                List<OrderItemsModel> orderItemsModelList = response.body().getItems();

                OrderItemsAdapter orderItemsAdapter = new OrderItemsAdapter(orderItemsModelList, PlaceOrderDetailsActivity.this);
                LinearLayoutManager layoutManager = new LinearLayoutManager(PlaceOrderDetailsActivity.this, LinearLayoutManager.VERTICAL, false);
                orderItemListRecyclerView.setLayoutManager(layoutManager);
                orderItemListRecyclerView.setAdapter(orderItemsAdapter);
                orderItemsAdapter.notifyDataSetChanged();


                //Time Line
                List<OrderTimelineModel> orderTimelineModelList = details.getTimeline();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.cancelOrder) {
            Toast.makeText(this, "Item 1 Selected", Toast.LENGTH_SHORT).show();
        }
        return false;

    }

    public void placeOrderIdCopy(View view) {
        ClipboardManager cm = (ClipboardManager) PlaceOrderDetailsActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setText(invoiceIdTV.getText());
    }
}