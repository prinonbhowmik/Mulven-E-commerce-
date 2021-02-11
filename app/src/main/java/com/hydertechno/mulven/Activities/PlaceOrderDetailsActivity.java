package com.hydertechno.mulven.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.hydertechno.mulven.R;

public class PlaceOrderDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order_details);
    }

    public void placeOrderDetailsBack(View view) {
        finish();
    }
}