package com.hydertechno.mulven.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hydertechno.mulven.Internet.Connection;
import com.hydertechno.mulven.Internet.ConnectivityReceiver;
import com.hydertechno.mulven.R;

import es.dmoral.toasty.Toasty;

public class OTP extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private TextInputLayout otpTIL;
    private TextInputEditText otpTIET;
    private Button nextBtn2;
    private String phone;
    private int otp;
    private RelativeLayout rootLayout;
    private Snackbar snackbar;
    private boolean isConnected;
    private ConnectivityReceiver connectivityReceiver;
    private IntentFilter intentFilter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p);
        init();
        checkConnection();
        if (!isConnected) {
            snackBar(isConnected);
        }
        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");
        otp = intent.getIntExtra("otp",0);

        nextBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isConnected) {
                    snackBar(isConnected);
                }else{
                int inputOtp = Integer.parseInt(otpTIET.getText().toString());
                if (inputOtp == otp) {
                    startActivity(new Intent(OTP.this, ChangePasswordActivity.class)
                            .putExtra("phone", phone).putExtra("activity", "forgot"));
                    finish();
                } else {
                    Toasty.error(OTP.this, "OTP doesn't match!", Toasty.LENGTH_LONG).show();
                }
            }
            }
        });
    }

    private void init() {
        rootLayout = findViewById(R.id.otp_rootLayout);
        intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        connectivityReceiver = new ConnectivityReceiver();
        otpTIL=findViewById(R.id.otp_LT);
        otpTIET=findViewById(R.id.otp_Et);
        nextBtn2=findViewById(R.id.nextBtn2);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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
            snackbar = Snackbar.make(rootLayout, "No Internet Connection!", Snackbar.LENGTH_INDEFINITE).setAction("ReTry", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recreate();
                }
            });
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
}