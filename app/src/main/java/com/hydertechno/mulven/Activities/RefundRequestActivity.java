package com.hydertechno.mulven.Activities;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.hydertechno.mulven.Api.ApiUtils;
import com.hydertechno.mulven.Models.PaymentMethodModel;
import com.hydertechno.mulven.Models.ProfileUpdateResponse;
import com.hydertechno.mulven.Models.RefundSettlementResponse;
import com.hydertechno.mulven.Models.SettlementRequestResponse;
import com.hydertechno.mulven.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RefundRequestActivity extends BaseActivity {
    private CardView nagadCV, bkashCV, bankCV;
    private TextView nagad_Number, bkash_Number, bank_Number;
    private CheckBox nagad_CheckBox, bkash_CheckBox, bank_CheckBox;
    private EditText commentBox;
    private TextView applyRequestTV;
    private Toolbar toolbar;
    private Snackbar snackbar;
    private FloatingActionButton addSettlementBtn;
    private RelativeLayout refundRequestRootLayout;
    private SharedPreferences sharedPreferences;

    private String orderId, refundMethod, comment;
    private double refundAmount;
    private String token;

    private List<PaymentMethodModel> methodModelsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund_request);
        init();
        checkConnection();
        if (!isConnected) {
            snackBar(isConnected);
        } else {
            getRefundSettlementData();

        }


        nagad_CheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (nagad_CheckBox.isChecked()) {
                    bkash_CheckBox.setChecked(false);
                    bank_CheckBox.setChecked(false);
                }
            }
        });
        bkash_CheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (bkash_CheckBox.isChecked()) {
                    nagad_CheckBox.setChecked(false);
                    bank_CheckBox.setChecked(false);
                }
            }
        });
        bank_CheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (bank_CheckBox.isChecked()) {
                    bkash_CheckBox.setChecked(false);
                    nagad_CheckBox.setChecked(false);
                }
            }
        });


        applyRequestTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment = commentBox.getText().toString();

                    if (!nagad_CheckBox.isChecked() && !bkash_CheckBox.isChecked() && !bank_CheckBox.isChecked()) {
                        Toasty.normal(RefundRequestActivity.this, "Please select settlement method first", Toasty.LENGTH_SHORT).show();
                    } else {
                        if (nagad_CheckBox.isChecked()) {
                            refundMethod = "Nagad";
                        } else if (bkash_CheckBox.isChecked()) {
                            refundMethod = "Bkash";
                        } else if (bank_CheckBox.isChecked()) {
                            refundMethod = "Bank";
                        }

                        Call<SettlementRequestResponse> call1 = ApiUtils.getUserService().sendRefundRequest(
                                token, orderId, refundAmount, refundMethod, comment);
                        call1.enqueue(new Callback<SettlementRequestResponse>() {
                            @Override
                            public void onResponse(Call<SettlementRequestResponse> call, Response<SettlementRequestResponse> response) {

                                if (response.isSuccessful() && response.code() == 200) {
                                    String status = response.body().getStatus();
                                    if (status.equals("1")) {
                                        Toasty.success(RefundRequestActivity.this, ""+response.body().getText(), Toast.LENGTH_SHORT, true).show();
                                        finish();
                                    } else {
                                        Toasty.error(RefundRequestActivity.this, "" + response.body().getText(), Toast.LENGTH_LONG, true).show();
                                    }
                                    Log.e("onResponse===>", ""+token+"\n"+orderId+"\n"+refundAmount+"\n"+refundMethod+
                                            "\n"+comment+"\n"+response.body().getText()+"");
                                }
                            }

                            @Override
                            public void onFailure(Call<SettlementRequestResponse> call, Throwable t) {
                                Toasty.error(RefundRequestActivity.this, "" + t.getMessage(), Toast.LENGTH_LONG, true).show();
                                Log.e("FailureResponse===>", t.getMessage());

                            }
                        });
                    }



            }
        });

        addSettlementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RefundRequestActivity.this, RefundSettlementActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void getRefundSettlementData() {
        Call<RefundSettlementResponse> call = ApiUtils.getUserService().getRefundSettlements(token);
        call.enqueue(new Callback<RefundSettlementResponse>() {
            @Override
            public void onResponse(Call<RefundSettlementResponse> call, Response<RefundSettlementResponse> response) {

                if (response.isSuccessful() && response.code() == 200) {
                    if (response.body() != null) {
                        if (response.body().getNagad() != null) {
                            nagad_Number.setText(response.body().getNagad().getAccountNo());
                            nagadCV.setVisibility(View.VISIBLE);
                        }
                        if (response.body().getBkash() != null) {
                            bkash_Number.setText(response.body().getBkash().getAccountNo());
                            bkashCV.setVisibility(View.VISIBLE);
                        }
                        if (response.body().getBank() != null) {
                            bank_Number.setText(response.body().getBank().getBankName());
                            bankCV.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    Toasty.error(RefundRequestActivity.this, "Error with:" + response.code(), Toasty.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RefundSettlementResponse> call, Throwable t) {
            }
        });
    }

    private void init() {
        Intent getInt = getIntent();
        orderId = getInt.getStringExtra("orderId");
        refundAmount = getInt.getDoubleExtra("refundAmount", 0.0);

        nagadCV = findViewById(R.id.nagadCV);
        bkashCV = findViewById(R.id.bkashCV);
        bankCV = findViewById(R.id.bankCV);
        nagad_Number = findViewById(R.id.nagad_Number);
        bkash_Number = findViewById(R.id.bkash_Number);
        bank_Number = findViewById(R.id.bank_Number);
        nagad_CheckBox = findViewById(R.id.nagad_CheckBox);
        bkash_CheckBox = findViewById(R.id.bkash_CheckBox);
        bank_CheckBox = findViewById(R.id.bank_CheckBox);

        addSettlementBtn = findViewById(R.id.addSettlementBtn);
        commentBox = findViewById(R.id.refundCommentET);
        refundRequestRootLayout = findViewById(R.id.refundRequestRootLayout);
        applyRequestTV = findViewById(R.id.applyRequestTV);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        sharedPreferences = getSharedPreferences("MyRef", MODE_PRIVATE);
        token = sharedPreferences.getString("token", null);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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
        if (!isConnected) {
            snackbar = Snackbar.make(refundRequestRootLayout, "No Internet Connection! Please Try Again.", Snackbar.LENGTH_INDEFINITE);
            snackbar.setDuration(5000);
            snackbar.setActionTextColor(Color.WHITE);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(Color.RED);
            snackbar.show();
        }
    }
}