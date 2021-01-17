package com.hydertechno.mulven.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hydertechno.mulven.R;

public class PhoneNumber extends AppCompatActivity {
    private TextInputLayout phoneNoTIL;
    private TextInputEditText phoneNoTIET;
    private Button nextBtn1;

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
    }

    private void init() {
        phoneNoTIL=findViewById(R.id.phone_no_LT);
        phoneNoTIET=findViewById(R.id.phone_no_Et);
        nextBtn1=findViewById(R.id.nextBtn1);
    }
}