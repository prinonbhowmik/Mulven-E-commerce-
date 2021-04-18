package com.hydertechno.mulven.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.hydertechno.mulven.Adapters.FeatureAddAdapter;
import com.hydertechno.mulven.Adapters.HomePageSliderAdapter;
import com.hydertechno.mulven.Adapters.OrderListAdapter;
import com.hydertechno.mulven.Api.ApiInterface;
import com.hydertechno.mulven.Api.ApiUtils;
import com.hydertechno.mulven.Internet.Connection;
import com.hydertechno.mulven.Internet.ConnectivityReceiver;
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

public class PlaceOrderListActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{
    private RecyclerView orderListRecyclerView;
    private ApiInterface apiInterface;
    private List<OrderListModel> orderListModel;
    private List<OrderListModel> orderList;
    private OrderListAdapter orderListAdapter;
    private int id;
    private String token;
    private RelativeLayout noOrderLayout;
    private boolean isConnected;
    private Snackbar snackbar;
    private RelativeLayout rootLayout;
    private ConnectivityReceiver connectivityReceiver;
    private IntentFilter intentFilter;
    private TextView sAll,sProcessing, sDelivered, sShipped, sPartialPaid, sPending, sCanceled;

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
        checkConnection();
        if (!isConnected) {
            snackBar(isConnected);
        }


        sAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderListModel.clear();
                sAll.setTextColor(Color.parseColor("#FFFFFF"));
                sAll.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag_all));

                sProcessing.setTextColor(Color.parseColor("#0F9D58"));
                sProcessing.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));
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

        sProcessing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderListModel.clear();
                sProcessing.setTextColor(Color.parseColor("#FFFFFF"));
                sProcessing.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_processing));

                sAll.setTextColor(Color.parseColor("#FF03DAC5"));
                sAll.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));
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
                Call<OrderModel> call = apiInterface.getOrderList(id, token);
                call.enqueue(new Callback<OrderModel>() {
                    @Override
                    public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
                        if (response.isSuccessful()) {
                            String status = response.body().getStatus();
                            if (status.equals("1")) {
                                orderListModel = response.body().getItems();
                                orderListAdapter = new OrderListAdapter(orderListModel, PlaceOrderListActivity.this);
                                orderListAdapter.getFilter().filter("Processing");
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

        sDelivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderListModel.clear();
                sDelivered.setTextColor(Color.parseColor("#FFFFFF"));
                sDelivered.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_delivered));

                sProcessing.setTextColor(Color.parseColor("#0F9D58"));
                sProcessing.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));
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

                sProcessing.setTextColor(Color.parseColor("#0F9D58"));
                sProcessing.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));
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

                sProcessing.setTextColor(Color.parseColor("#0F9D58"));
                sProcessing.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));
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

                sProcessing.setTextColor(Color.parseColor("#0F9D58"));
                sProcessing.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));
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

                sProcessing.setTextColor(Color.parseColor("#0F9D58"));
                sProcessing.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));
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
        sProcessing = findViewById(R.id.sProcessing);
        sDelivered = findViewById(R.id.sDelivered);
        sShipped = findViewById(R.id.sShipped);
        sPartialPaid = findViewById(R.id.sPartialPaid);
        sPending = findViewById(R.id.sPending);
        sCanceled = findViewById(R.id.sCanceled);
        rootLayout = findViewById(R.id.placeOrderListRootLayout);

        intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        connectivityReceiver = new ConnectivityReceiver();
        noOrderLayout = findViewById(R.id.noOrderLayout);
        orderListRecyclerView = findViewById(R.id.orderListRecyclerView);
        orderListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        apiInterface = ApiUtils.getUserService();
        orderListModel = new ArrayList<>();

        sAll.setTextColor(Color.parseColor("#FFFFFF"));
        sAll.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag_all));

    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        snackBar(isConnected);
    }
    private void checkConnection() {
        isConnected = ConnectivityReceiver.isConnected();
    }
    private void snackBar(boolean isConnected) {
        if(!isConnected) {
            snackbar = Snackbar.make(rootLayout, "No Internet Connection!", Snackbar.LENGTH_INDEFINITE).setAction("ReTry", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recreate();
                }
            });
            snackbar.setDuration(5000);
            snackbar.setActionTextColor(Color.WHITE);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(Color.RED);
        /*TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        } else {
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
        }*/
            snackbar.show();
            /*final Snackbar.SnackbarLayout snackBarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
            for (int i = 0; i < snackBarLayout.getChildCount(); i++) {
                View parent = snackBarLayout.getChildAt(i);
                if (parent instanceof LinearLayout) {
                    ((LinearLayout) parent).setRotation(180);
                    break;
                }
            }*/
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(connectivityReceiver, intentFilter);
    }
    @Override
    protected void onResume() {
        // register connection status listener
        Connection.getInstance().setConnectivityListener(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try{
            if(connectivityReceiver!=null)
                unregisterReceiver(connectivityReceiver);

        }catch(Exception e){}

    }

    @Override
    protected void onStop() {
        try{
            if(connectivityReceiver!=null)
                unregisterReceiver(connectivityReceiver);

        }catch(Exception e){}

        super.onStop();
    }



    public void placeOrderBack(View view) {
        finish();
    }

}