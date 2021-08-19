package com.hydertechno.mulven.Activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.hydertechno.mulven.Api.ApiUtils;
import com.hydertechno.mulven.Fragments.LoadingDialog;
import com.hydertechno.mulven.Models.InvoiceDetailsModel;
import com.hydertechno.mulven.Models.RefundSettlementResponse;
import com.hydertechno.mulven.R;
import com.hydertechno.mulven.Utilities.BetterActivityResult;

import java.sql.Ref;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RefundSettlementActivity extends BaseActivity implements View.OnClickListener, BetterActivityResult.OnActivityResult<ActivityResult> {
    private static final String TAG = RefundSettlementActivity.class.getSimpleName();

    public RelativeLayout rootLayout;
    private Toolbar toolbar;
    private Snackbar snackbar;

    private LoadingDialog loadingDialog;
    private SharedPreferences sharedPreferences;
    private String token;

    private TextView nNumber;
    private TextView nEdit;

    private TextView bNumber;
    private TextView bEdit;

    private TextView bankNameTV;
    private TextView branchNameTV;
    private TextView routingNoTV;
    private TextView accountNameTV;
    private TextView accountNoTV;
    private TextView bankEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund_settlement);
        loadingDialog = LoadingDialog.instance();
        init();

        checkConnection();
        if (!isConnected) {
            snackBar(isConnected);
        }else {
            getRefundSettlements();
        }
    }

    public void init(){
        rootLayout = findViewById(R.id.refundRootLayout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        nNumber = findViewById(R.id.nNumber);
        nEdit = findViewById(R.id.nEdit);

        bNumber = findViewById(R.id.bNumber);
        bEdit = findViewById(R.id.bEdit);

        bankNameTV = findViewById(R.id.bankNameTV);
        branchNameTV = findViewById(R.id.branchNameTV);
        routingNoTV = findViewById(R.id.routingNoTV);
        accountNameTV = findViewById(R.id.accountNameTV);
        accountNoTV = findViewById(R.id.accountNoTV);
        bankEdit = findViewById(R.id.bankEdit);

        nEdit.setOnClickListener(this);
        bEdit.setOnClickListener(this);
        bankEdit.setOnClickListener(this);

        sharedPreferences = getSharedPreferences("MyRef", MODE_PRIVATE);
        token = sharedPreferences.getString("token", null);
    }

    private void getRefundSettlements() {
        if (!loadingDialog.isAdded())
            loadingDialog.show(getSupportFragmentManager(), null);

        Call<RefundSettlementResponse> call = ApiUtils.getUserService().getRefundSettlements(token);
        call.enqueue(new Callback<RefundSettlementResponse>() {
            @Override
            public void onResponse(Call<RefundSettlementResponse> call, Response<RefundSettlementResponse> response) {
                loadingDialog.dismiss();
                if (response.isSuccessful() && response.code() == 200) {
                    if (response.body()!= null) {
                        if (response.body().getNagad() != null) {
                            nNumber.setText(response.body().getNagad().getAccountNo());
                            nEdit.setText("Update");
                        }
                        if (response.body().getBkash() != null) {
                            bNumber.setText(response.body().getBkash().getAccountNo());
                            bEdit.setText("Update");
                        }
                        if (response.body().getBank() != null) {
                            bankNameTV.setText(response.body().getBank().getBankName());
                            branchNameTV.setText(response.body().getBank().getBranchName());
                            routingNoTV.setText(response.body().getBank().getRoutingNo());
                            accountNameTV.setText(response.body().getBank().getAccountName());
                            accountNoTV.setText(response.body().getBank().getAccountNo());
                            bankEdit.setText("Update");
                        }
                    }
                } else {
                    Toasty.error(RefundSettlementActivity.this, "Error with:" + response.code(), Toasty.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RefundSettlementResponse> call, Throwable t) {
                loadingDialog.dismiss();
                Toasty.error(RefundSettlementActivity.this, t.getMessage(), Toasty.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent=new Intent( RefundSettlementActivity.this,MainActivity.class);
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
        Intent intent=new Intent( RefundSettlementActivity.this,MainActivity.class);
        intent.putExtra("fragment","profile");
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        snackBar(isConnected);
    }


    public void snackBar(boolean isConnected) {
        if(!isConnected) {
            snackbar = Snackbar.make(rootLayout, "No Internet Connection! Please Try Again.", Snackbar.LENGTH_INDEFINITE);
            snackbar.setDuration(5000);
            snackbar.setActionTextColor(Color.WHITE);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(Color.RED);
            snackbar.show();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        checkConnection();
        if (!isConnected) {
            snackBar(isConnected);
            return;
        }
        Intent intent = new Intent( RefundSettlementActivity.this, RefundMethodFormActivity.class);
        switch (v.getId()) {
            case  R.id.nEdit:
                intent.putExtra("method", "nagad");
                intent.putExtra("accountNo", nNumber.getText().toString());
                activityLauncher.launch(intent, this);
                break;
            case  R.id.bEdit:
                intent.putExtra("method", "bkash");
                intent.putExtra("accountNo", bNumber.getText().toString());
                activityLauncher.launch(intent, this);
                break;
            case  R.id.bankEdit:
                intent.putExtra("method", "bank");
                intent.putExtra("bankName", bankNameTV.getText().toString());
                intent.putExtra("branchName", branchNameTV.getText().toString());
                intent.putExtra("routingNo", routingNoTV.getText().toString());
                intent.putExtra("accountName", accountNameTV.getText().toString());
                intent.putExtra("accountNo", accountNoTV.getText().toString());
                activityLauncher.launch(intent, this);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();

            if (data != null && data.getStringExtra("result").equals("ok")) {
                checkConnection();
                if (isConnected) {
                    getRefundSettlements();
                }
            }
        }
    }
}