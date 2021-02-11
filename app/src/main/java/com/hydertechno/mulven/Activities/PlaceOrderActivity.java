package com.hydertechno.mulven.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hydertechno.mulven.R;

public class PlaceOrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);
    }

    public void placeOrderBack(View view) {
        finish();
    }

    public void test(View view) {
        startActivity(new Intent(PlaceOrderActivity.this,PlaceOrderDetailsActivity.class));
    }
}