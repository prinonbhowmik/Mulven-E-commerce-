package com.hydertechno.mulven.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hanks.htextview.base.HTextView;
import com.hydertechno.mulven.Api.ApiUtils;
import com.hydertechno.mulven.Internet.Connection;
import com.hydertechno.mulven.Internet.ConnectivityReceiver;
import com.hydertechno.mulven.Models.UserProfile;
import com.hydertechno.mulven.R;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhoneNumber extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{
    private TextInputLayout phoneNoTIL;
    private TextInputEditText phoneNoTIET;
    private Button nextBtn1;
    private HTextView textViewLine;
    int delay = 2000; //milliseconds
    Handler handler;

    private RelativeLayout rootLayout;
    private Snackbar snackbar;
    private boolean isConnected;
    private ConnectivityReceiver connectivityReceiver;
    private IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);
        init();
        checkConnection();
        if (!isConnected) {
            snackBar(isConnected);
        }
        nextBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnection();
                if (!isConnected) {
                    snackBar(isConnected);
                }else {
                    String phone = phoneNoTIET.getText().toString();
                    getOtp(phone);
                }
            }
        });

        textViewLine= findViewById(R.id.textViewLine);
        handler = new Handler();
        handler.postDelayed(new Runnable(){
            public void run(){

                handler.postDelayed(this, delay);
                textViewLine.animateText("Sign Up");

            }
        }, delay);
    }

    private void getOtp(String phone) {
        Call<UserProfile> call = ApiUtils.getUserService().matchOTP(phone);
        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                Log.e("TAG", response.toString());
                if (response.isSuccessful()){
                   String status = response.body().getStatus();
                   if (status.equals("1")){
                       int otp  = response.body().getOtp();
                       Log.d("checkOTP", String.valueOf(otp));
                       startActivity(new Intent(PhoneNumber.this,OTP.class)
                               .putExtra("otp",otp).putExtra("phone",phone));
                       PhoneNumber.this.finish();
                       Toasty.success(PhoneNumber.this, ""+response.body().getMessage(), Toasty.LENGTH_SHORT).show();
                   }else if(status.equals("0")){
                       Toasty.error(PhoneNumber.this, ""+response.body().getMessage(), Toasty.LENGTH_SHORT).show();
                   }
                } else {
                    Toasty.error(PhoneNumber.this, "Response error: " + response.code(), Toasty.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                Log.e("TAG", t.getMessage());
                Toasty.error(PhoneNumber.this, t.getMessage(), Toasty.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {
        rootLayout = findViewById(R.id.phone_number_rootLayout);
        intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        connectivityReceiver = new ConnectivityReceiver();
        phoneNoTIL=findViewById(R.id.phone_no_LT);
        phoneNoTIET=findViewById(R.id.phone_no_Et);
        nextBtn1=findViewById(R.id.nextBtn1);
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