package com.hydertechno.mulven.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.hydertechno.mulven.Adapters.NotificationAdapter;
import com.hydertechno.mulven.Adapters.PaymentHistoryAdapter;
import com.hydertechno.mulven.Api.ApiUtils;
import com.hydertechno.mulven.Models.NotificationModel;
import com.hydertechno.mulven.Models.Transaction;
import com.hydertechno.mulven.Models.TransactionModel;
import com.hydertechno.mulven.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentHistoryActivity extends BaseActivity {

    public RelativeLayout rootLayout;

    private Toolbar toolbar;

    private TextView account_amountTV;
    private TextView holding_amountTV;
    private TextView gift_amountTV;
    private TextView cashBack_amountTV;

    private RecyclerView recentPaymentHistoryRecycler;
    private RelativeLayout progressRL;

    private Snackbar snackbar;

    private SharedPreferences sharedPreferences;
    private String  token;

    private PaymentHistoryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_history);

        init();

        initRecyclerView();

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        checkConnection();
        if (isConnected) {
            getTransactionsHistory();
        }
    }

    private void init() {
        rootLayout = findViewById(R.id.pHRoot);
        toolbar = findViewById(R.id.toolbar);
        account_amountTV = findViewById(R.id.account_amountTV);
        holding_amountTV = findViewById(R.id.holding_amountTV);
        gift_amountTV = findViewById(R.id.gift_amountTV);
        cashBack_amountTV = findViewById(R.id.cashBack_amountTV);
        recentPaymentHistoryRecycler = findViewById(R.id.recentPaymentHistoryRecycler);
        progressRL = findViewById(R.id.progressRL);

        sharedPreferences = getSharedPreferences("MyRef", MODE_PRIVATE);
        token = sharedPreferences.getString("token",null);
    }

    private void initRecyclerView() {
        mAdapter = new PaymentHistoryAdapter(new ArrayList<Transaction>(), this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recentPaymentHistoryRecycler.setLayoutManager(layoutManager);
        recentPaymentHistoryRecycler.setAdapter(mAdapter);
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
        Intent intent=new Intent( PaymentHistoryActivity.this,MainActivity.class);
        intent.putExtra("fragment","profile");
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }

    private void getTransactionsHistory() {
        progressRL.setVisibility(View.VISIBLE);
        Call<TransactionModel> call = ApiUtils.getUserService().getTransactions(token);
        call.enqueue(new Callback<TransactionModel>() {
            @Override
            public void onResponse(Call<TransactionModel> call, Response<TransactionModel> response) {
                progressRL.setVisibility(View.GONE);
                if (response.isSuccessful() && response.code() == 200) {
                    if (response.body() != null) {

                        if (response.body().getActiveAccountBal() != null && response.body().getActiveAccountWithBal() != null) {
                            double accountBalance = response.body().getActiveAccountBal() - response.body().getActiveAccountWithBal();
                            account_amountTV.setText(accountBalance + "");
                        }

                        if (response.body().getHoldingBal() != null) {
                            holding_amountTV.setText(response.body().getHoldingBal() + "");
                        }

                        if (response.body().getActiveVoucherBal() != null && response.body().getActiveVoucherWithBal() != null) {
                            double voucherBalance = response.body().getActiveVoucherBal() - response.body().getActiveVoucherWithBal();
                            gift_amountTV.setText(voucherBalance + "");
                        }

                        if (response.body().getActiveCashbackBal() != null && response.body().getActiveCashbackWithBal() != null) {
                            double cashBackBalance = response.body().getActiveCashbackBal() - response.body().getActiveCashbackWithBal();
                            cashBack_amountTV.setText(cashBackBalance + "");
                        }

                        if (response.body().getTransaction() != null) {
                            mAdapter.updateData(response.body().getTransaction());
                        }
                    }
                } else {
                    showToast("Network Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<TransactionModel> call, Throwable t) {
                progressRL.setVisibility(View.GONE);
                showToast(t.getMessage());
            }
        });
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        snackBar(isConnected);
    }

    private void snackBar(boolean isConnected) {
        if(!isConnected) {
            snackbar = Snackbar.make(rootLayout, "No Internet Connection!", Snackbar.LENGTH_INDEFINITE);
            snackbar.setDuration(5000);
            snackbar.setActionTextColor(Color.WHITE);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(Color.RED);
            snackbar.show();
        }
    }
}