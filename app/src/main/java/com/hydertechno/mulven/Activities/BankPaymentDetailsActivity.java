package com.hydertechno.mulven.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.hydertechno.mulven.R;

import java.util.Objects;

public class BankPaymentDetailsActivity extends AppCompatActivity {
    private TextView bankPaymentTV,fullAmountTV;
    private double fullAmount;
    private String orderId;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_payment_details);
        init();
        Intent getInt=getIntent();
        fullAmount=getInt.getDoubleExtra("fullAmount",0.0);
        orderId=getInt.getStringExtra("orderId");

        fullAmountTV.setText(" "+fullAmount+"BDT");
        bankPaymentTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(BankPaymentDetailsActivity.this, DepositorFormActivity.class);
                intent.putExtra("orderId",orderId);
                startActivity(intent);
            }
        });
    }

    public void init(){

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        bankPaymentTV=findViewById(R.id.bankPaymentTV);
        fullAmountTV=findViewById(R.id.fullAmountTV);

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