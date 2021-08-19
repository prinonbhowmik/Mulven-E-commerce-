package com.hydertechno.mulven.Activities;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.hydertechno.mulven.R;

import java.util.Objects;

public class RefundRequestActivity extends BaseActivity {
    private RecyclerView requestRecycler;
    private EditText commentBox;
    private TextView applyRequestTV;
    private Toolbar toolbar;
    private Snackbar snackbar;
    private FloatingActionButton addSettlementBtn;
    private RelativeLayout refundRequestRootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund_request);
        init();
        checkConnection();
        if (!isConnected) {
            snackBar(isConnected);
        }else {
            getRefundSettlementData();

        }
        applyRequestTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        addSettlementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RefundRequestActivity.this, RefundSettlementActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });
    }

    private void getRefundSettlementData() {

    }

    private void init() {
        requestRecycler=findViewById(R.id.refundMethodRecycler);
        addSettlementBtn=findViewById(R.id.addSettlementBtn);
        commentBox=findViewById(R.id.refundCommentET);
        refundRequestRootLayout=findViewById(R.id.refundRequestRootLayout);
        applyRequestTV=findViewById(R.id.applyRequestTV);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        snackBar(isConnected);
    }
    public void snackBar(boolean isConnected) {
        if(!isConnected) {
            snackbar = Snackbar.make(refundRequestRootLayout, "No Internet Connection! Please Try Again.", Snackbar.LENGTH_INDEFINITE);
            snackbar.setDuration(5000);
            snackbar.setActionTextColor(Color.WHITE);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(Color.RED);
            snackbar.show();
        }
    }
}