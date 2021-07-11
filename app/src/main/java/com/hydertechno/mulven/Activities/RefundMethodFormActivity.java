package com.hydertechno.mulven.Activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hydertechno.mulven.Api.ApiUtils;
import com.hydertechno.mulven.Fragments.LoadingDialog;
import com.hydertechno.mulven.Models.PostRefundSettlementResponse;
import com.hydertechno.mulven.Models.RefundSettlementResponse;
import com.hydertechno.mulven.Models.SettlementModel;
import com.hydertechno.mulven.R;
import com.hydertechno.mulven.Utilities.BetterActivityResult;

import java.util.Objects;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RefundMethodFormActivity extends BaseActivity implements View.OnClickListener, BetterActivityResult.OnActivityResult<ActivityResult> {

    private Toolbar toolbar;
    private String method;

    private LoadingDialog loadingDialog;
    private SharedPreferences sharedPreferences;
    private String token;

    private LinearLayout bankFormLayout;
    private EditText bankNameTV;
    private EditText branchNameTV;
    private EditText routingNoTV;
    private EditText accountNameTV;
    private EditText accountNoTV;
    private TextView bankSave;

    private LinearLayout nagadFormLayout;
    private EditText nagadAccountET;
    private TextView nagadSaveTV;

    private LinearLayout bkashFormLayout;
    private EditText bkashAccountET;
    private TextView bkashSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund_method_form);
        loadingDialog = LoadingDialog.instance();
        init();

        Intent intent = getIntent();
        method = intent.getStringExtra("method");
        switch (method) {
            case "nagad":
                nagadFormLayout.setVisibility(View.VISIBLE);
                setString(nagadAccountET, intent.getStringExtra("accountNo"));
                break;
            case "bkash":
                bkashFormLayout.setVisibility(View.VISIBLE);
                setString(bkashAccountET, intent.getStringExtra("accountNo"));
                break;
            case "bank":
                bankFormLayout.setVisibility(View.VISIBLE);
                setString(bankNameTV, intent.getStringExtra("bankName"));
                setString(branchNameTV, intent.getStringExtra("branchName"));
                setString(routingNoTV, intent.getStringExtra("routingNo"));
                setString(accountNameTV, intent.getStringExtra("accountName"));
                setString(accountNoTV, intent.getStringExtra("accountNo"));
                break;
            default:
                break;
        }
    }

    private void setString(EditText editText, String str) {
        if ((!str.equals("N/A"))) {
            editText.setText(str);
        } else {
            editText.setText("");
        }
    }

    public void init(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        bankFormLayout = findViewById(R.id.bankFormLayout);
        bankNameTV = findViewById(R.id.bankNameTV);
        branchNameTV = findViewById(R.id.branchNameTV);
        routingNoTV = findViewById(R.id.routingNoTV);
        accountNameTV = findViewById(R.id.accountNameTV);
        accountNoTV = findViewById(R.id.accountNoTV);
        bankSave = findViewById(R.id.bankSave);

        nagadFormLayout = findViewById(R.id.nagadFormLayout);
        nagadAccountET = findViewById(R.id.nagadAccountET);
        nagadSaveTV = findViewById(R.id.nagadSaveTV);

        bkashFormLayout = findViewById(R.id.bkashFormLayout);
        bkashAccountET = findViewById(R.id.bkashAccountET);
        bkashSave = findViewById(R.id.bkashSave);

        bankSave.setOnClickListener(this);
        nagadSaveTV.setOnClickListener(this);
        bkashSave.setOnClickListener(this);

        sharedPreferences = getSharedPreferences("MyRef", MODE_PRIVATE);
        token = sharedPreferences.getString("token", null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View v) {
        if (isValid()) {
            Intent intent = new Intent(RefundMethodFormActivity.this, RefundOTPActivity.class);
            activityLauncher.launch(intent, this);
        }
    }

    private boolean isValid() {
        switch (method) {
            case "nagad":
                if (nagadAccountET.getText().toString().isEmpty()) {
                    nagadAccountET.setError("Field is required!");
                    return false;
                }
                return true;
            case "bkash":
                if (bkashAccountET.getText().toString().isEmpty()) {
                    bkashAccountET.setError("Field is required!");
                    return false;
                }
                return true;
            case "bank":
                if (bankNameTV.getText().toString().isEmpty()) {
                    bankNameTV.setError("Field is required!");
                    return false;
                }

                if (branchNameTV.getText().toString().isEmpty()) {
                    branchNameTV.setError("Field is required!");
                    return false;
                }

                if (routingNoTV.getText().toString().isEmpty()) {
                    routingNoTV.setError("Field is required!");
                    return false;
                }

                if (accountNameTV.getText().toString().isEmpty()) {
                    accountNameTV.setError("Field is required!");
                    return false;
                }

                if (accountNoTV.getText().toString().isEmpty()) {
                    accountNoTV.setError("Field is required!");
                    return false;
                }
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onActivityResult(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();

            if (data != null && data.getStringExtra("result").equals("matched")) {
                checkConnection();
                if (isConnected) {
                    postRefundSettlement();
                } else {
                    Toasty.error(RefundMethodFormActivity.this, "No internet connection!", Toasty.LENGTH_LONG).show();
                }
            }
        }
    }

    private void postRefundSettlement() {
        if (!loadingDialog.isAdded())
            loadingDialog.show(getSupportFragmentManager(), null);

        SettlementModel body = getSettlementBody(method);
        Call<PostRefundSettlementResponse> call = ApiUtils.getUserService().postRefundSettlement(token, body);
        call.enqueue(new Callback<PostRefundSettlementResponse>() {
            @Override
            public void onResponse(Call<PostRefundSettlementResponse> call, Response<PostRefundSettlementResponse> response) {
                loadingDialog.dismiss();
                if (response.isSuccessful() && response.code() == 200) {
                    if (response.body().getStatus().equals("1")) {
                        Toasty.success(RefundMethodFormActivity.this, response.body().getMessage(), Toasty.LENGTH_LONG).show();

                        Intent intent = new Intent();
                        intent.putExtra("result", "ok");
                        setResult(Activity.RESULT_OK, intent);
                        RefundMethodFormActivity.this.finish();
                    } else {
                        Toasty.error(RefundMethodFormActivity.this, response.body().getMessage(), Toasty.LENGTH_LONG).show();
                    }
                } else {
                    Toasty.error(RefundMethodFormActivity.this, "Error with: " + response.code(), Toasty.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<PostRefundSettlementResponse> call, Throwable t) {
                loadingDialog.dismiss();
                Toasty.error(RefundMethodFormActivity.this, Objects.requireNonNull(t.getMessage()), Toasty.LENGTH_LONG).show();
            }
        });
    }

    private SettlementModel getSettlementBody(String method) {
        SettlementModel body = new SettlementModel();
        body.setAccountType(method);
        body.setBankName(method);
        body.setBranchName(method);
        body.setRoutingNo(method);
        body.setAccountName(method);
        switch (method) {
            case "nagad":
                body.setAccountNo(nagadAccountET.getText().toString());
                break;
            case "bkash":
                body.setAccountNo(bkashAccountET.getText().toString());
                break;
            case "bank":
                body.setAccountType(method);
                body.setBankName(bankNameTV.getText().toString());
                body.setBranchName(branchNameTV.getText().toString());
                body.setRoutingNo(routingNoTV.getText().toString());
                body.setAccountName(accountNameTV.getText().toString());
                body.setAccountNo(accountNoTV.getText().toString());
                break;
        }
        return body;
    }



    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
    }
}