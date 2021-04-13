package com.hydertechno.mulven.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hydertechno.mulven.Adapters.FeatureAddAdapter;
import com.hydertechno.mulven.Adapters.HomePageSliderAdapter;
import com.hydertechno.mulven.Adapters.OrderListAdapter;
import com.hydertechno.mulven.Api.ApiInterface;
import com.hydertechno.mulven.Api.ApiUtils;
import com.hydertechno.mulven.Models.OrderListModel;
import com.hydertechno.mulven.Models.OrderModel;
import com.hydertechno.mulven.Models.Sliderimage;
import com.hydertechno.mulven.R;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceOrderListActivity extends AppCompatActivity {
    private RecyclerView orderListRecyclerView;
    private ApiInterface apiInterface;
    private List<OrderListModel> orderListModel;
    private OrderListAdapter orderListAdapter;
    private int id;
    private String token;
    private RelativeLayout noOrderLayout;
    private TextView sAll, sDelivered, sShipped, sPartialPaid, sPending, sCanceled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order_list);
        init();
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        token = intent.getStringExtra("token");
        Log.d("checkId", String.valueOf(id));
        getOrderList(id);


        sAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderListModel.clear();
                sAll.setTextColor(Color.parseColor("#FFFFFF"));
                sAll.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag_all));

                sDelivered.setTextColor(Color.parseColor("#808080"));
                sDelivered.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));
                sShipped.setTextColor(Color.parseColor("#FF5722"));
                sShipped.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));
                sPartialPaid.setTextColor(Color.parseColor("#4285F4"));
                sPartialPaid.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));
                sPending.setTextColor(Color.parseColor("#F4B400"));
                sPending.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));
                sCanceled.setTextColor(Color.parseColor("#DB4437"));
                sCanceled.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));
                getOrderList(id);
            }
        });

        sDelivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderListModel.clear();
                sDelivered.setTextColor(Color.parseColor("#FFFFFF"));
                sDelivered.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_delivered));

                sAll.setTextColor(Color.parseColor("#FF03DAC5"));
                sAll.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));
                sShipped.setTextColor(Color.parseColor("#FF5722"));
                sShipped.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));
                sPartialPaid.setTextColor(Color.parseColor("#4285F4"));
                sPartialPaid.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));
                sPending.setTextColor(Color.parseColor("#F4B400"));
                sPending.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));
                sCanceled.setTextColor(Color.parseColor("#DB4437"));
                sCanceled.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));
                Call<OrderModel> call = apiInterface.getOrderList(id, token);
                call.enqueue(new Callback<OrderModel>() {
                    @Override
                    public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
                        if (response.isSuccessful()) {
                            String status = response.body().getStatus();
                            if (status.equals("1")) {
                                orderListModel = response.body().getItems();
                                orderListAdapter = new OrderListAdapter(orderListModel, PlaceOrderListActivity.this);
                                orderListRecyclerView.setAdapter(orderListAdapter);
                                orderListAdapter.getFilter().filter("Delivered");
                            }
                            Collections.reverse(orderListModel);
                            orderListAdapter.notifyDataSetChanged();
                            if (orderListModel.size() == 0) {
                                noOrderLayout.setVisibility(View.VISIBLE);
                                orderListRecyclerView.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<OrderModel> call, Throwable t) {

                    }
                });
            }
        });

        sShipped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderListModel.clear();
                sShipped.setTextColor(Color.parseColor("#FFFFFF"));
                sShipped.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_shipped));

                sAll.setTextColor(Color.parseColor("#FF03DAC5"));
                sAll.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));
                sDelivered.setTextColor(Color.parseColor("#808080"));
                sDelivered.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));
                sPartialPaid.setTextColor(Color.parseColor("#4285F4"));
                sPartialPaid.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));
                sPending.setTextColor(Color.parseColor("#F4B400"));
                sPending.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));
                sCanceled.setTextColor(Color.parseColor("#DB4437"));
                sCanceled.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));
                Call<OrderModel> call = apiInterface.getOrderList(id, token);
                call.enqueue(new Callback<OrderModel>() {
                    @Override
                    public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
                        if (response.isSuccessful()) {
                            String status = response.body().getStatus();
                            if (status.equals("1")) {
                                orderListModel = response.body().getItems();
                                orderListAdapter = new OrderListAdapter(orderListModel, PlaceOrderListActivity.this);
                                orderListRecyclerView.setAdapter(orderListAdapter);
                                orderListAdapter.getFilter().filter("Shipped");
                            }
                            Collections.reverse(orderListModel);
                            orderListAdapter.notifyDataSetChanged();
                            if (orderListModel.size() == 0) {
                                noOrderLayout.setVisibility(View.VISIBLE);
                                orderListRecyclerView.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<OrderModel> call, Throwable t) {

                    }
                });
            }
        });

        sPartialPaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderListModel.clear();
                sPartialPaid.setTextColor(Color.parseColor("#FFFFFF"));
                sPartialPaid.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_partial_paid));

                sAll.setTextColor(Color.parseColor("#FF03DAC5"));
                sAll.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));
                sDelivered.setTextColor(Color.parseColor("#808080"));
                sDelivered.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));
                sShipped.setTextColor(Color.parseColor("#FF5722"));
                sShipped.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));
                sPending.setTextColor(Color.parseColor("#F4B400"));
                sPending.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));
                sCanceled.setTextColor(Color.parseColor("#DB4437"));
                sCanceled.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));
                Call<OrderModel> call = apiInterface.getOrderList(id, token);
                call.enqueue(new Callback<OrderModel>() {
                    @Override
                    public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
                        if (response.isSuccessful()) {
                            String status = response.body().getStatus();
                            if (status.equals("1")) {
                                orderListModel = response.body().getItems();
                                orderListAdapter = new OrderListAdapter(orderListModel, PlaceOrderListActivity.this);
                                orderListRecyclerView.setAdapter(orderListAdapter);
                                orderListAdapter.getFilter().filter("Partial Paid");
                            }
                            Collections.reverse(orderListModel);
                            orderListAdapter.notifyDataSetChanged();
                            if (orderListModel.size() == 0) {
                                noOrderLayout.setVisibility(View.VISIBLE);
                                orderListRecyclerView.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<OrderModel> call, Throwable t) {

                    }
                });
            }
        });

        sPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderListModel.clear();
                sPending.setTextColor(Color.parseColor("#FFFFFF"));
                sPending.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_pending));

                sAll.setTextColor(Color.parseColor("#FF03DAC5"));
                sAll.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));
                sDelivered.setTextColor(Color.parseColor("#808080"));
                sDelivered.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));
                sPartialPaid.setTextColor(Color.parseColor("#4285F4"));
                sPartialPaid.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));
                sShipped.setTextColor(Color.parseColor("#FF5722"));
                sShipped.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));
                sCanceled.setTextColor(Color.parseColor("#DB4437"));
                sCanceled.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));

                Call<OrderModel> call = apiInterface.getOrderList(id, token);
                call.enqueue(new Callback<OrderModel>() {
                    @Override
                    public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
                        if (response.isSuccessful()) {
                            String status = response.body().getStatus();
                            if (status.equals("1")) {
                                orderListModel = response.body().getItems();
                                orderListAdapter = new OrderListAdapter(orderListModel, PlaceOrderListActivity.this);
                                orderListAdapter.getFilter().filter("Pending");
                                orderListRecyclerView.setAdapter(orderListAdapter);

                            }
                            Collections.reverse(orderListModel);
                            orderListAdapter.notifyDataSetChanged();
                            if (orderListModel.size() == 0) {
                                noOrderLayout.setVisibility(View.VISIBLE);
                                orderListRecyclerView.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<OrderModel> call, Throwable t) {

                    }
                });
            }
        });

        sCanceled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderListModel.clear();
                sCanceled.setTextColor(Color.parseColor("#FFFFFF"));
                sCanceled.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_cancel));
                sAll.setTextColor(Color.parseColor("#FF03DAC5"));
                sAll.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));
                sDelivered.setTextColor(Color.parseColor("#808080"));
                sDelivered.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));
                sPartialPaid.setTextColor(Color.parseColor("#4285F4"));
                sPartialPaid.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));
                sShipped.setTextColor(Color.parseColor("#FF5722"));
                sShipped.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));
                sPending.setTextColor(Color.parseColor("#F4B400"));
                sPending.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));

                Call<OrderModel> call = apiInterface.getOrderList(id, token);
                call.enqueue(new Callback<OrderModel>() {
                    @Override
                    public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
                        if (response.isSuccessful()) {
                            String status = response.body().getStatus();
                            if (status.equals("1")) {
                                orderListModel = response.body().getItems();
                                orderListAdapter = new OrderListAdapter(orderListModel, PlaceOrderListActivity.this);
                                orderListAdapter.getFilter().filter("Cancel");
                                orderListRecyclerView.setAdapter(orderListAdapter);
                            }
                            Collections.reverse(orderListModel);
                            orderListAdapter.notifyDataSetChanged();
                            if (orderListModel.size() == 0) {
                                noOrderLayout.setVisibility(View.VISIBLE);
                                orderListRecyclerView.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<OrderModel> call, Throwable t) {

                    }
                });

            }
        });

    }

    private void getOrderList(int id) {
        orderListModel.clear();
        Call<OrderModel> call = apiInterface.getOrderList(id, token);
        call.enqueue(new Callback<OrderModel>() {
            @Override
            public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
                if (response.isSuccessful()) {
                    String status = response.body().getStatus();
                    if (status.equals("1")) {
                        orderListModel = response.body().getItems();
                        orderListAdapter = new OrderListAdapter(orderListModel, PlaceOrderListActivity.this);
                        orderListRecyclerView.setAdapter(orderListAdapter);
                    }
                    Collections.reverse(orderListModel);
                    orderListAdapter.notifyDataSetChanged();
                    if (orderListModel.size() == 0) {
                        noOrderLayout.setVisibility(View.VISIBLE);
                        orderListRecyclerView.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<OrderModel> call, Throwable t) {

            }
        });
    }

    private void init() {
        sAll = findViewById(R.id.sAll);
        sDelivered = findViewById(R.id.sDelivered);
        sShipped = findViewById(R.id.sShipped);
        sPartialPaid = findViewById(R.id.sPartialPaid);
        sPending = findViewById(R.id.sPending);
        sCanceled = findViewById(R.id.sCanceled);

        noOrderLayout = findViewById(R.id.noOrderLayout);
        orderListRecyclerView = findViewById(R.id.orderListRecyclerView);
        orderListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        apiInterface = ApiUtils.getUserService();
        orderListModel = new ArrayList<>();

        sAll.setTextColor(Color.parseColor("#FFFFFF"));
        sAll.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag_all));

    }

    public void placeOrderBack(View view) {
        finish();
    }

}