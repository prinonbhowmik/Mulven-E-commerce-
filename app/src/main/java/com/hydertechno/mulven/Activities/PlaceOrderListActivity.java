package com.hydertechno.mulven.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.hydertechno.mulven.Adapters.OrderListAdapter;
import com.hydertechno.mulven.Api.ApiInterface;
import com.hydertechno.mulven.Api.ApiUtils;
import com.hydertechno.mulven.Interface.OnClickOrderListener;
import com.hydertechno.mulven.Internet.Connection;
import com.hydertechno.mulven.Internet.ConnectivityReceiver;
import com.hydertechno.mulven.Models.OrderListModel;
import com.hydertechno.mulven.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceOrderListActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener, OnClickOrderListener {
    public static final int Place_Order_Request_Code=1111;
    private RecyclerView orderListRecyclerView;
    private ApiInterface apiInterface;
    private List<OrderListModel> orderListModel;
    private List<OrderListModel> orderList;
    private OrderListAdapter orderListAdapter;
    private int id;
    private String token,from;
    private RelativeLayout noOrderLayout,progressRL;
    private boolean isConnected;
    private Snackbar snackbar;
    private SharedPreferences sharedPreferences;
    public static LinearLayout rootLayout;
    private ConnectivityReceiver connectivityReceiver;
    private IntentFilter intentFilter;
    private TextView sAll,sProcessing, sDelivered, sShipped, sPartialPaid, sPending,sPicked, sCanceled,sRefunded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order_list);
        init();
        Intent intent=getIntent();
        from=intent.getStringExtra("from");

        token = sharedPreferences.getString("token",null);
        //token = intent.getStringExtra("token");
        getOrderList();
        checkConnection();
        if (!isConnected) {
            snackBar(isConnected);
        }


        sAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnection();
                if (!isConnected) {
                    snackBar(isConnected);
                } else {
                    orderListModel.clear();
                    sAll.setTextColor(Color.parseColor("#FFFFFF"));
                    sAll.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag_all));

                    sProcessing.setTextColor(Color.parseColor("#0F9D58"));
                    sProcessing.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));
                    sPicked.setTextColor(Color.parseColor("#FF6200EE"));
                    sPicked.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));
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
                    sRefunded.setTextColor(Color.parseColor("#9C27B0"));
                    sRefunded.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));
                    getOrderList();
                }
            }
        });

        sProcessing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnection();
                if (!isConnected) {
                    snackBar(isConnected);
                } else {
                    progressRL.setVisibility(View.VISIBLE);
                    orderListModel.clear();
                    sProcessing.setTextColor(Color.parseColor("#FFFFFF"));
                    sProcessing.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_processing));

                    sAll.setTextColor(Color.parseColor("#FF03DAC5"));
                    sAll.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));
                    sPicked.setTextColor(Color.parseColor("#FF6200EE"));
                    sPicked.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));
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
                    sRefunded.setTextColor(Color.parseColor("#9C27B0"));
                    sRefunded.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));

                    Call<List<OrderListModel>> call = apiInterface.getOrderList(token);
                    call.enqueue(new Callback<List<OrderListModel>>() {
                        @Override
                        public void onResponse(Call<List<OrderListModel>> call, Response<List<OrderListModel>> response) {
                            if (response.isSuccessful()) {
                                progressRL.setVisibility(View.GONE);
                                orderListModel = response.body();
                                orderListAdapter = new OrderListAdapter(orderListModel, PlaceOrderListActivity.this, PlaceOrderListActivity.this);
                                orderListRecyclerView.setAdapter(orderListAdapter);
                                orderListAdapter.getFilter().filter("Processing");
                                Collections.reverse(orderListModel);
                                orderListAdapter.notifyDataSetChanged();
                                Log.e("order",""+orderListModel.size());
                                if (orderListModel.size() == 0) {
                                    noOrderLayout.setVisibility(View.VISIBLE);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<List<OrderListModel>> call, Throwable t) {
                            progressRL.setVisibility(View.GONE);
                            noOrderLayout.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });

        sDelivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnection();
                if (!isConnected) {
                    snackBar(isConnected);
                } else {
                    progressRL.setVisibility(View.VISIBLE);
                    orderListModel.clear();
                    sDelivered.setTextColor(Color.parseColor("#FFFFFF"));
                    sDelivered.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_delivered));

                    sProcessing.setTextColor(Color.parseColor("#0F9D58"));
                    sProcessing.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));
                    sPicked.setTextColor(Color.parseColor("#FF6200EE"));
                    sPicked.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));
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
                    sRefunded.setTextColor(Color.parseColor("#9C27B0"));
                    sRefunded.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));

                    Call<List<OrderListModel>> call = apiInterface.getOrderList(token);
                    call.enqueue(new Callback<List<OrderListModel>>() {
                        @Override
                        public void onResponse(Call<List<OrderListModel>> call, Response<List<OrderListModel>> response) {
                            if (response.isSuccessful()) {
                                progressRL.setVisibility(View.GONE);
                                orderListModel = response.body();
                                orderListAdapter = new OrderListAdapter(orderListModel, PlaceOrderListActivity.this, PlaceOrderListActivity.this);
                                orderListRecyclerView.setAdapter(orderListAdapter);
                                orderListAdapter.getFilter().filter("Delivered");
                                Collections.reverse(orderListModel);
                                orderListAdapter.notifyDataSetChanged();
                                if (orderListModel.size() == 0) {
                                    noOrderLayout.setVisibility(View.VISIBLE);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<List<OrderListModel>> call, Throwable t) {
                            progressRL.setVisibility(View.GONE);
                            noOrderLayout.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });

        sShipped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnection();
                if (!isConnected) {
                    snackBar(isConnected);
                } else {
                    progressRL.setVisibility(View.VISIBLE);
                    orderListModel.clear();
                    sShipped.setTextColor(Color.parseColor("#FFFFFF"));
                    sShipped.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_shipped));

                    sProcessing.setTextColor(Color.parseColor("#0F9D58"));
                    sProcessing.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));
                    sPicked.setTextColor(Color.parseColor("#FF6200EE"));
                    sPicked.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));
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
                    sRefunded.setTextColor(Color.parseColor("#9C27B0"));
                    sRefunded.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));

                    Call<List<OrderListModel>> call = apiInterface.getOrderList(token);
                    call.enqueue(new Callback<List<OrderListModel>>() {
                        @Override
                        public void onResponse(Call<List<OrderListModel>> call, Response<List<OrderListModel>> response) {
                            if (response.isSuccessful()) {
                                progressRL.setVisibility(View.GONE);
                                orderListModel = response.body();
                                orderListAdapter = new OrderListAdapter(orderListModel, PlaceOrderListActivity.this, PlaceOrderListActivity.this);
                                orderListRecyclerView.setAdapter(orderListAdapter);
                                orderListAdapter.getFilter().filter("Shipped");
                                Collections.reverse(orderListModel);
                                orderListAdapter.notifyDataSetChanged();
                                if (orderListModel.size() == 0) {
                                    noOrderLayout.setVisibility(View.VISIBLE);
                                    orderListRecyclerView.setVisibility(View.GONE);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<List<OrderListModel>> call, Throwable t) {
                            progressRL.setVisibility(View.GONE);
                            noOrderLayout.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });

        sPartialPaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnection();
                if (!isConnected) {
                    snackBar(isConnected);
                } else {
                    progressRL.setVisibility(View.VISIBLE);
                    orderListModel.clear();
                    sPartialPaid.setTextColor(Color.parseColor("#FFFFFF"));
                    sPartialPaid.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_partial_paid));

                    sProcessing.setTextColor(Color.parseColor("#0F9D58"));
                    sProcessing.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));
                    sPicked.setTextColor(Color.parseColor("#FF6200EE"));
                    sPicked.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));
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
                    sRefunded.setTextColor(Color.parseColor("#9C27B0"));
                    sRefunded.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));

                    Call<List<OrderListModel>> call = apiInterface.getOrderList(token);
                    call.enqueue(new Callback<List<OrderListModel>>() {
                        @Override
                        public void onResponse(Call<List<OrderListModel>> call, Response<List<OrderListModel>> response) {
                            if (response.isSuccessful()) {
                                progressRL.setVisibility(View.GONE);
                                orderListModel = response.body();
                                orderListAdapter = new OrderListAdapter(orderListModel, PlaceOrderListActivity.this, PlaceOrderListActivity.this);
                                orderListRecyclerView.setAdapter(orderListAdapter);
                                orderListAdapter.getFilter().filter("Partial Paid");
                                Collections.reverse(orderListModel);
                                orderListAdapter.notifyDataSetChanged();
                                if (orderListModel.size() == 0) {
                                    noOrderLayout.setVisibility(View.VISIBLE);
                                    orderListRecyclerView.setVisibility(View.GONE);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<List<OrderListModel>> call, Throwable t) {
                            progressRL.setVisibility(View.GONE);
                            noOrderLayout.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });


        sPicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnection();
                if (!isConnected) {
                    snackBar(isConnected);
                } else {
                    progressRL.setVisibility(View.VISIBLE);
                    orderListModel.clear();
                    sPicked.setTextColor(Color.parseColor("#FFFFFF"));
                    sPicked.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_picked));

                    sProcessing.setTextColor(Color.parseColor("#0F9D58"));
                    sProcessing.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));
                    sPartialPaid.setTextColor(Color.parseColor("#4285F4"));
                    sPartialPaid.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));
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
                    sRefunded.setTextColor(Color.parseColor("#9C27B0"));
                    sRefunded.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));

                    Call<List<OrderListModel>> call = apiInterface.getOrderList(token);
                    call.enqueue(new Callback<List<OrderListModel>>() {
                        @Override
                        public void onResponse(Call<List<OrderListModel>> call, Response<List<OrderListModel>> response) {
                            if (response.isSuccessful()) {
                                progressRL.setVisibility(View.GONE);
                                orderListModel = response.body();
                                orderListAdapter = new OrderListAdapter(orderListModel, PlaceOrderListActivity.this, PlaceOrderListActivity.this);
                                orderListRecyclerView.setAdapter(orderListAdapter);
                                orderListAdapter.getFilter().filter("Picked");
                                Collections.reverse(orderListModel);
                                orderListAdapter.notifyDataSetChanged();
                                if (orderListModel.size() == 0) {
                                    noOrderLayout.setVisibility(View.VISIBLE);
                                    orderListRecyclerView.setVisibility(View.GONE);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<List<OrderListModel>> call, Throwable t) {
                            progressRL.setVisibility(View.GONE);
                            noOrderLayout.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });

        sPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnection();
                if (!isConnected) {
                    snackBar(isConnected);
                } else {
                    progressRL.setVisibility(View.VISIBLE);
                    orderListModel.clear();
                    sPending.setTextColor(Color.parseColor("#FFFFFF"));
                    sPending.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_pending));

                    sProcessing.setTextColor(Color.parseColor("#0F9D58"));
                    sProcessing.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));
                    sPicked.setTextColor(Color.parseColor("#FF6200EE"));
                    sPicked.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));
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
                    sRefunded.setTextColor(Color.parseColor("#9C27B0"));
                    sRefunded.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));

                    Call<List<OrderListModel>> call = apiInterface.getOrderList(token);
                    call.enqueue(new Callback<List<OrderListModel>>() {
                        @Override
                        public void onResponse(Call<List<OrderListModel>> call, Response<List<OrderListModel>> response) {
                            if (response.isSuccessful()) {
                                progressRL.setVisibility(View.GONE);
                                orderListModel = response.body();
                                orderListAdapter = new OrderListAdapter(orderListModel, PlaceOrderListActivity.this, PlaceOrderListActivity.this);
                                orderListRecyclerView.setAdapter(orderListAdapter);
                                orderListAdapter.getFilter().filter("Pending");
                                Collections.reverse(orderListModel);
                                orderListAdapter.notifyDataSetChanged();
                                if (orderListModel.size() == 0) {
                                    noOrderLayout.setVisibility(View.VISIBLE);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<List<OrderListModel>> call, Throwable t) {
                            progressRL.setVisibility(View.GONE);
                            noOrderLayout.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });

        sCanceled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnection();
                if (!isConnected) {
                    snackBar(isConnected);
                } else {
                    progressRL.setVisibility(View.VISIBLE);
                    orderListModel.clear();
                    sCanceled.setTextColor(Color.parseColor("#FFFFFF"));
                    sCanceled.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_cancel));

                    sProcessing.setTextColor(Color.parseColor("#0F9D58"));
                    sProcessing.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));
                    sPicked.setTextColor(Color.parseColor("#FF6200EE"));
                    sPicked.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));
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
                    sRefunded.setTextColor(Color.parseColor("#9C27B0"));
                    sRefunded.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));

                    Call<List<OrderListModel>> call = apiInterface.getOrderList(token);
                    call.enqueue(new Callback<List<OrderListModel>>() {
                        @Override
                        public void onResponse(Call<List<OrderListModel>> call, Response<List<OrderListModel>> response) {
                            if (response.isSuccessful()) {
                                progressRL.setVisibility(View.GONE);
                                orderListModel = response.body();
                                orderListAdapter = new OrderListAdapter(orderListModel, PlaceOrderListActivity.this, PlaceOrderListActivity.this);
                                orderListRecyclerView.setAdapter(orderListAdapter);
                                orderListAdapter.getFilter().filter("Cancel");
                                Collections.reverse(orderListModel);
                                orderListAdapter.notifyDataSetChanged();
                                if (orderListModel.size() == 0) {
                                    noOrderLayout.setVisibility(View.VISIBLE);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<List<OrderListModel>> call, Throwable t) {
                            progressRL.setVisibility(View.GONE);
                            noOrderLayout.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });


        sRefunded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnection();
                if (!isConnected) {
                    snackBar(isConnected);
                } else {
                    progressRL.setVisibility(View.VISIBLE);
                    orderListModel.clear();
                    sRefunded.setTextColor(Color.parseColor("#FFFFFF"));
                    sRefunded.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_refunded));

                    sCanceled.setTextColor(Color.parseColor("#DB4437"));
                    sCanceled.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));
                    sProcessing.setTextColor(Color.parseColor("#0F9D58"));
                    sProcessing.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));
                    sPicked.setTextColor(Color.parseColor("#FF6200EE"));
                    sPicked.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag));
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

                    Call<List<OrderListModel>> call = apiInterface.getOrderList(token);
                    call.enqueue(new Callback<List<OrderListModel>>() {
                        @Override
                        public void onResponse(Call<List<OrderListModel>> call, Response<List<OrderListModel>> response) {
                            if (response.isSuccessful()) {
                                progressRL.setVisibility(View.GONE);
                                orderListModel = response.body();
                                orderListAdapter = new OrderListAdapter(orderListModel, PlaceOrderListActivity.this, PlaceOrderListActivity.this);
                                orderListRecyclerView.setAdapter(orderListAdapter);
                                orderListAdapter.getFilter().filter("Refund");
                                Collections.reverse(orderListModel);
                                orderListAdapter.notifyDataSetChanged();
                                if (orderListModel.size() == 0) {
                                    noOrderLayout.setVisibility(View.VISIBLE);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<List<OrderListModel>> call, Throwable t) {
                            progressRL.setVisibility(View.GONE);
                            noOrderLayout.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });

    }

    private void getOrderList() {
        progressRL.setVisibility(View.VISIBLE);
        orderListModel.clear();
        Call<List<OrderListModel>> call = apiInterface.getOrderList(token);
        call.enqueue(new Callback<List<OrderListModel>>() {
            @Override
            public void onResponse(Call<List<OrderListModel>> call, Response<List<OrderListModel>> response) {
                if (response.isSuccessful()) {
                    progressRL.setVisibility(View.GONE);
                    orderListModel = response.body();
                    orderListAdapter = new OrderListAdapter(orderListModel, PlaceOrderListActivity.this, PlaceOrderListActivity.this);
                    orderListRecyclerView.setAdapter(orderListAdapter);
                    Collections.reverse(orderListModel);
                    orderListAdapter.notifyDataSetChanged();
                    if (orderListModel.size() == 0) {
                        noOrderLayout.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<OrderListModel>> call, Throwable t) {
                progressRL.setVisibility(View.GONE);
                noOrderLayout.setVisibility(View.VISIBLE);
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
        sPicked = findViewById(R.id.sPicked);
        sCanceled = findViewById(R.id.sCanceled);
        sRefunded = findViewById(R.id.sRefunded);
        rootLayout = findViewById(R.id.placeOrderListRootLayout);

        sharedPreferences =getSharedPreferences("MyRef", MODE_PRIVATE);

        intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        connectivityReceiver = new ConnectivityReceiver();
        noOrderLayout = findViewById(R.id.noOrderLayout);
        progressRL = findViewById(R.id.progressRL);

        orderListRecyclerView = findViewById(R.id.orderListRecyclerView);
        orderListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        apiInterface = ApiUtils.getUserService();
        orderListModel = new ArrayList<>();

        sAll.setTextColor(Color.parseColor("#FFFFFF"));
        sAll.setBackground(ContextCompat.getDrawable(PlaceOrderListActivity.this, R.drawable.status_tag_all));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Place_Order_Request_Code) {
            if (data != null && data.getBooleanExtra("success", false))
                recreate();
        }
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
            snackbar = Snackbar.make(rootLayout, "No Internet Connection! Please Try Again.", Snackbar.LENGTH_INDEFINITE);
            snackbar.setDuration(5000);
            snackbar.setActionTextColor(Color.WHITE);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(Color.RED);
            snackbar.show();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(from.equals("cart")) {
            startActivity(new Intent(PlaceOrderListActivity.this, MainActivity.class).putExtra("fragment", "home"));
        }else if(from.equals("profile")) {
            startActivity(new Intent(PlaceOrderListActivity.this, MainActivity.class).putExtra("fragment", "profile"));
        }
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }

    public void placeOrderBack(View view) {
        if(from.equals("cart")) {
            startActivity(new Intent(PlaceOrderListActivity.this, MainActivity.class).putExtra("fragment", "home"));
        }else if(from.equals("profile")) {
            startActivity(new Intent(PlaceOrderListActivity.this, MainActivity.class).putExtra("fragment", "profile"));
        }
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }

    @Override
    public void onClick(OrderListModel item) {
        Intent intent = new Intent(PlaceOrderListActivity.this, PlaceOrderDetailsActivity.class);
        intent.putExtra("OrderId", item.getOrder_id());
        intent.putExtra("PaymentStatus", item.getPay_status());
        intent.putExtra("OrderStatus", item.getOrders_status());
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //context.startActivity(intent);
        startActivityForResult(intent, Place_Order_Request_Code);
        //((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        //((Activity)context).finish();

    }
}