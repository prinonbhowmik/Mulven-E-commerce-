package com.hydertechno.mulven.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hydertechno.mulven.R;

public class OTP extends AppCompatActivity {
    private TextInputLayout otpTIL;
    private TextInputEditText otpTIET;
    private Button nextBtn2;
    private String phone;
    private int otp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p);
        init();

        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");
        otp = intent.getIntExtra("otp",0);

        nextBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int inputOtp = Integer.parseInt(otpTIET.getText().toString());
                if (inputOtp==otp){
                    startActivity(new Intent(OTP.this,ChangePasswordActivity.class)
                            .putExtra("phone",phone).putExtra("activity","forgot"));
                    finish();
                }else{
                    Toast.makeText(OTP.this, "OTP doesn't match!", Toast.LENGTH_LONG).show();
                }
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