package com.hydertechno.mulven.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.hydertechno.mulven.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Objects;

public class DepositorFormActivity extends BaseActivity {
    private TextInputEditText depositorNameET,depositorPhoneET,depositorBankET;
    private TextView submit_bankPaymentTV;
    private RelativeLayout depositorRootLayout,uploadImageBtn;
    private Toolbar toolbar;
    private String orderId;
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dipositor_form);
        init();
        checkConnection();
        if (!isConnected) {
            snackBar(isConnected);
        }
        uploadImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkConnection();
                if (!isConnected) {
                    snackBar(isConnected);
                }else {
                    CropImage.activity()
                            .setFixAspectRatio(true)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setCropShape(CropImageView.CropShape.OVAL)
                            .start(DepositorFormActivity.this);
                }
            }
        });

        submit_bankPaymentTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnection();
                if (!isConnected) {
                    snackBar(isConnected);
                }else {


                }

            }
        });

    }



    public void init(){
        Intent getInt=getIntent();
        orderId=getInt.getStringExtra("orderId");

        depositorNameET=findViewById(R.id.depositorNameET);
        depositorPhoneET=findViewById(R.id.depositorPhoneET);
        depositorBankET=findViewById(R.id.depositorBankET);
        submit_bankPaymentTV=findViewById(R.id.submit_bankPaymentTV);
        uploadImageBtn=findViewById(R.id.uploadBtn);
        depositorRootLayout=findViewById(R.id.depositorRootLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

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

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        snackBar(isConnected);
    }


    public void snackBar(boolean isConnected) {
        if(!isConnected) {
            snackbar = Snackbar.make(depositorRootLayout, "No Internet Connection! Please Try Again.", Snackbar.LENGTH_INDEFINITE);
            snackbar.setDuration(5000);
            snackbar.setActionTextColor(Color.WHITE);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(Color.RED);
            snackbar.show();
        }
    }
}