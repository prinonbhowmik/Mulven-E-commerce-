package com.hydertechno.mulven.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.hydertechno.mulven.Adapters.FeatureAddAdapter;
import com.hydertechno.mulven.Adapters.HomePageSliderAdapter;
import com.hydertechno.mulven.Adapters.OrderListAdapter;
import com.hydertechno.mulven.Api.ApiInterface;
import com.hydertechno.mulven.Api.ApiUtils;
import com.hydertechno.mulven.Models.OrderListModel;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order_list);
        init();
        getOrderList();
    }

    private void getOrderList() {
        orderListModel.clear();
        Call<List<OrderListModel>> call = apiInterface.getOrderList(335);
        call.enqueue(new Callback<List<OrderListModel>>() {
            @Override
            public void onResponse(Call<List<OrderListModel>> call, Response<List<OrderListModel>> response) {
                if(response.isSuccessful()){
                    orderListModel = response.body();
                    orderListAdapter=new OrderListAdapter(orderListModel,getApplicationContext());
                    orderListRecyclerView.setAdapter(orderListAdapter);
                }
                Collections.reverse(orderListModel);
                orderListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<OrderListModel>> call, Throwable t) {
                Log.d("ErrorKi",t.getMessage());
            }
        });
    }

    private void init() {
        orderListRecyclerView=findViewById(R.id.orderListRecyclerView);
        orderListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        apiInterface = ApiUtils.getUserService();
        orderListModel=new ArrayList<>();

    }

    public void placeOrderBack(View view) {
        finish();
    }

}