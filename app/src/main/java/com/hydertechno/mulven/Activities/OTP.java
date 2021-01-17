package com.hydertechno.mulven.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hydertechno.mulven.R;

public class OTP extends AppCompatActivity {
    private TextInputLayout otpTIL;
    private TextInputEditText otpTIET;
    private Button nextBtn2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p);
        init();
        nextBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OTP.this,SignUp.class));
                finish();
            }
        });
    }

    private void init() {
        otpTIL=findViewById(R.id.otp_LT);
        otpTIET=findViewById(R.id.otp_Et);
        nextBtn2=findViewById(R.id.nextBtn2);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}