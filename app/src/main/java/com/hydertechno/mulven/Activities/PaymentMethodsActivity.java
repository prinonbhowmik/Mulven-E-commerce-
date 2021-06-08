package com.hydertechno.mulven.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.hydertechno.mulven.R;

import java.util.Objects;

public class PaymentMethodsActivity extends AppCompatActivity {
    private TextView payInvoiceIdTV,payAmountTV;
    Toolbar toolbar;
    private String orderId;
    private double amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_methods);
        init();
        Intent getInt=getIntent();
        amount=getInt.getDoubleExtra("amount",0.0);
        orderId=getInt.getStringExtra("orderId");
        payAmountTV.setText(""+amount);
        payInvoiceIdTV.setText(orderId);

    }
    public void init(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
//        toolbar.setTitleTextAppearance(PaymentMethodsActivity.this,R.style.Comfortaa_bold);

        payInvoiceIdTV=findViewById(R.id.payInvoiceIdTV);
        payAmountTV=findViewById(R.id.payAmountTV);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}