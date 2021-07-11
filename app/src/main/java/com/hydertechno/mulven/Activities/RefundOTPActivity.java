package com.hydertechno.mulven.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hydertechno.mulven.Api.ApiUtils;
import com.hydertechno.mulven.Fragments.LoadingDialog;
import com.hydertechno.mulven.Models.PostRefundSettlementResponse;
import com.hydertechno.mulven.R;

import java.util.Objects;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RefundOTPActivity extends BaseActivity {
    private static final String TAG = RefundOTPActivity.class.getSimpleName();
    private Toolbar toolbar;

    private EditText optET;
    private TextView baEdit;

    private LoadingDialog loadingDialog;
    private SharedPreferences sharedPreferences;
    private String token;

    private String otpCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund_o_t_p);
        loadingDialog = LoadingDialog.instance();
        init();

        checkConnection();
        if (isConnected) {
            getOtpRefund();
        } else {
            Toasty.error(RefundOTPActivity.this, "No internet connection!", Toasty.LENGTH_LONG).show();
        }
    }

    private void getOtpRefund() {
        if (!loadingDialog.isAdded())
            loadingDialog.show(getSupportFragmentManager(), null);

        Call<PostRefundSettlementResponse> call = ApiUtils.getUserService().getRefundOTP(token);
        call.enqueue(new Callback<PostRefundSettlementResponse>() {
            @Override
            public void onResponse(Call<PostRefundSettlementResponse> call, Response<PostRefundSettlementResponse> response) {
                loadingDialog.dismiss();

                if (response.isSuccessful() && response.code() == 200) {
                    if (response.body().getStatus().equals("1")) {
                        Toasty.success(RefundOTPActivity.this, "OTP sent to your number", Toasty.LENGTH_LONG).show();
                        otpCode = response.body().getMessage();
                        Log.e(TAG, otpCode);
                    } else {
                        Toasty.error(RefundOTPActivity.this, "OTP couldn't sent!", Toasty.LENGTH_LONG).show();
                    }
                } else {
                    Toasty.error(RefundOTPActivity.this, "Error with: " + response.code(), Toasty.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<PostRefundSettlementResponse> call, Throwable t) {
                loadingDialog.dismiss();
                Toasty.error(RefundOTPActivity.this, Objects.requireNonNull(t.getMessage()), Toasty.LENGTH_LONG).show();
            }
        });
    }

    public void init(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        optET = findViewById(R.id.optET);
        baEdit=findViewById(R.id.baEdit);

        sharedPreferences = getSharedPreferences("MyRef", MODE_PRIVATE);
        token = sharedPreferences.getString("token", null);


        baEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (optET.getText().toString().isEmpty()) {
                    optET.setError("OTP required!");
                } else if (optET.getText().toString().equals(otpCode)) {
                    Toasty.success(RefundOTPActivity.this, "OTP matched!", Toasty.LENGTH_LONG).show();
                    Intent intent = new Intent();
                    intent.putExtra("result", "matched");
                    setResult(Activity.RESULT_OK, intent);
                    RefundOTPActivity.this.finish();
                } else {
                    Toasty.error(RefundOTPActivity.this, "OTP doesn't matched!", Toasty.LENGTH_LONG).show();
                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent=new Intent( RefundOTPActivity.this,MainActivity.class);
                intent.putExtra("fragment","profile");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent( RefundOTPActivity.this,MainActivity.class);
        intent.putExtra("fragment","profile");
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
    }
}