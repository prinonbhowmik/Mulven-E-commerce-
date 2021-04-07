package com.hydertechno.mulven.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order_list);
        init();
        Intent intent = getIntent();
        id = intent.getIntExtra("id",0);
        token = intent.getStringExtra("token");
        Log.d("checkId", String.valueOf(id));
        getOrderList(id);
    }

    private void getOrderList(int id) {
        orderListModel.clear();
        Call<OrderModel> call = apiInterface.getOrderList(id,token);
        call.enqueue(new Callback<OrderModel>() {
            @Override
            public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
                if (response.isSuccessful()){
                    String status = response.body().getStatus();
                    if (status.equals("1")){
                        orderListModel = response.body().getItems();
                        orderListAdapter = new OrderListAdapter(orderListModel,PlaceOrderListActivity.this);
                        orderListRecyclerView.setAdapter(orderListAdapter);
                    }
                    Collections.reverse(orderListModel);
                    orderListAdapter.notifyDataSetChanged();
                    if(orderListModel.size()==0){
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
        noOrderLayout=findViewById(R.id.noOrderLayout);
        orderListRecyclerView=findViewById(R.id.orderListRecyclerView);
        orderListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        apiInterface = ApiUtils.getUserService();
        orderListModel=new ArrayList<>();

    }

    public void placeOrderBack(View view) {
        finish();
    }

}