package com.hydertechno.mulven.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hydertechno.mulven.R;

public class PlaceOrderDetailsActivity extends AppCompatActivity {
    private TextView orderIDTV,orderTimeTV,vendorNameTV,vendorPhoneTV,vendorAddressTV,customerNameTV,customerPhoneTV,customerAddressTV,customerAddressEditTV;
    private Dialog dialog;
    private RatingBar ratingBar;
    private String OrderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order_details);
        init();
        Intent intent = getIntent();
        OrderId = intent.getStringExtra("OrderId");

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
    }

    private void init() {
        orderIDTV=findViewById(R.id.orderIDTV);
        orderTimeTV=findViewById(R.id.orderTimeTV);
        vendorNameTV=findViewById(R.id.vendorNameTV);
        vendorPhoneTV=findViewById(R.id.vendorPhoneTV);
        vendorAddressTV=findViewById(R.id.vendorAddressTV);
        customerNameTV=findViewById(R.id.customerNameTV);
        customerPhoneTV=findViewById(R.id.customerPhoneTV);
        customerAddressTV=findViewById(R.id.customerAddressTV);
        customerAddressEditTV=findViewById(R.id.customerAddressEditTV);
        ratingBar=findViewById(R.id.ratingBar);
    }

    public void placeOrderDetailsBack(View view) {
        finish();
    }
}