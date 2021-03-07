package com.hydertechno.mulven.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hanks.htextview.base.HTextView;
import com.hydertechno.mulven.R;

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
                startActivity(new Intent(PhoneNumber.this,OTP.class));
                finish();
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

    private void init() {
        phoneNoTIL=findViewById(R.id.phone_no_LT);
        phoneNoTIET=findViewById(R.id.phone_no_Et);
        nextBtn1=findViewById(R.id.nextBtn1);
    }
}