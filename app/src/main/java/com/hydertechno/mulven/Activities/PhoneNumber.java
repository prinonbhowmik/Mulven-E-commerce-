package com.hydertechno.mulven.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hanks.htextview.base.HTextView;
import com.hydertechno.mulven.Api.ApiUtils;
import com.hydertechno.mulven.Models.UserProfile;
import com.hydertechno.mulven.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhoneNumber extends AppCompatActivity {
    private TextInputLayout phoneNoTIL;
    private TextInputEditText phoneNoTIET;
    private Button nextBtn1;
    private HTextView textViewLine;
    int delay = 2000; //milliseconds
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);
        init();
        nextBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = phoneNoTIET.getText().toString();
                getOtp(phone);
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
                if (response.isSuccessful()){
                   String status = response.body().getStatus();
                   if (status.equals("1")){
                       int otp  = response.body().getOtp();
                       Log.d("checkOTP", String.valueOf(otp));
                       startActivity(new Intent(PhoneNumber.this,OTP.class)
                               .putExtra("otp",otp).putExtra("phone",phone));
                       Toast.makeText(PhoneNumber.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                   }else if(status.equals("0")){
                       Toast.makeText(PhoneNumber.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                   }
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {

            }
        });
    }

    private void init() {
        phoneNoTIL=findViewById(R.id.phone_no_LT);
        phoneNoTIET=findViewById(R.id.phone_no_Et);
        nextBtn1=findViewById(R.id.nextBtn1);
    }
}