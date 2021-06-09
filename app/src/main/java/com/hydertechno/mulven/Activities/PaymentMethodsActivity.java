package com.hydertechno.mulven.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.hydertechno.mulven.Adapters.PaymentMethodsAdapter;
import com.hydertechno.mulven.Api.ApiUtils;
import com.hydertechno.mulven.Interface.OnPMethodItemClickListener;
import com.hydertechno.mulven.Models.PaymentMethodModel;
import com.hydertechno.mulven.Models.RequiredDataModel;
import com.hydertechno.mulven.R;
import com.sm.shurjopaysdk.listener.PaymentResultListener;
import com.sm.shurjopaysdk.model.TransactionInfo;
import com.sm.shurjopaysdk.payment.ShurjoPaySDK;
import com.sm.shurjopaysdk.utils.SPayConstants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentMethodsActivity extends BaseActivity implements OnPMethodItemClickListener {
    private TextView payInvoiceIdTV,payAmountTV;
    private RecyclerView methodsRecyclerView;
    private PaymentMethodsAdapter methodsAdapter;
    private SharedPreferences sharedPreferences;
    Toolbar toolbar;
    private String orderId,fullAmount,token;
    private double amount;

    private List<PaymentMethodModel> methodModelsList = new ArrayList<>();

    private Dialog bankPaymentDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_methods);
        init();
        Intent getInt=getIntent();
        amount=getInt.getDoubleExtra("amount",0.0);
        orderId=getInt.getStringExtra("orderId");
        fullAmount=getInt.getStringExtra("FAmount");
        payAmountTV.setText(""+amount);
        payInvoiceIdTV.setText(orderId);

        getData();

    }

    private void getData() {
        methodModelsList=new ArrayList<>();
        //methodModelsList.add(new PaymentMethodModel("Nagad", "Pay from your Nagad account", R.drawable.nagad));
        methodModelsList.add(new PaymentMethodModel("Shurjo Pay", "Choose your desire payment method by Shurjo Pay", R.drawable.shurjo_pay));
        methodModelsList.add(new PaymentMethodModel("Bank", "Pay by bank account", R.drawable.bank_transfer));

        methodsAdapter.updateData(methodModelsList);
    }

    public void init(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
//        toolbar.setTitleTextAppearance(PaymentMethodsActivity.this,R.style.Comfortaa_bold);

        sharedPreferences = getSharedPreferences("MyRef", MODE_PRIVATE);
        token = sharedPreferences.getString("token", null);
        payInvoiceIdTV=findViewById(R.id.payInvoiceIdTV);
        payAmountTV=findViewById(R.id.payAmountTV);
        methodsRecyclerView=findViewById(R.id.methodsRecyclerView);

        methodsAdapter = new PaymentMethodsAdapter(methodModelsList,this);
        methodsRecyclerView.setLayoutManager(new LinearLayoutManager(PaymentMethodsActivity.this, LinearLayoutManager.VERTICAL, false));
        methodsRecyclerView.setAdapter(methodsAdapter);
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
    public void onClick(PaymentMethodModel item, int position) {
        switch (position) {

            case 0:
                getShurjoPayment(amount);
                break;
            case 1:
                bankPaymentDialog = new Dialog(context);
                bankPaymentDialog.setContentView(R.layout.bank_payment_layout_design);
                bankPaymentDialog.setCancelable(true);
                TextView bankAmount;
                ImageView bCloseIV;
                bCloseIV=bankPaymentDialog.findViewById(R.id.bCloseIV);
                bankAmount=bankPaymentDialog.findViewById(R.id.bb1);
                bankAmount.setText(fullAmount);
                bankPaymentDialog.show();
                bCloseIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bankPaymentDialog.dismiss();
                    }
                });
                break;
        }
    }

    private void getShurjoPayment(double amount) {
        int unique_id=(int)((new Date().getTime()/1000L)% Integer.MAX_VALUE);
        String Test_Username = "spaytest";
        String Test_Password ="JehPNXF58rXs";
        String Test_Transaction_Prefix=" NOK";
        String testToken="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJrZXkiOiJzcGF5dGVzdCIsImlhdCI6MTU5ODM2MTI1Nn0.cwkvdTDI6_K430xq7Iqapaknbqjm9J3Th1EiXePIEcY";
        String liveToken="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6Im11bHZlbiIsImtleSI6ImpPYmdQRFdvcjFEcyJ9.Ie4mUEkQ-6WW1nyPg7FOverSWRfUs7IXZkCItKyvimI";
        RequiredDataModel dataModel=new RequiredDataModel("mulven",
                "m8zPxlA4Ews9","MLV"+orderId+"$"+unique_id,amount, liveToken);
        ShurjoPaySDK.getInstance().makePayment(PaymentMethodsActivity.this,
                SPayConstants.SdkType.LIVE, dataModel, new PaymentResultListener() {
                    @Override
                    public void onSuccess(TransactionInfo transactionInfo) {
                        double amount=transactionInfo.getTxnAmount();

                        Call<ResponseBody> call = ApiUtils.getUserService().setShurjo_Pay(token,orderId,""+amount,transactionInfo.getMethod(),transactionInfo.getBankTxID(),transactionInfo.getTxID());
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if(response.isSuccessful() && response.code() == 200){
                                    recreate();
                                    Intent resultIntent = new Intent();
                                    resultIntent.putExtra("success", true);
                                    setResult(PlaceOrderListActivity.Place_Order_Request_Code ,resultIntent);
                                }
                            }
                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toasty.error(PaymentMethodsActivity.this, "Something went wrong", Toasty.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onFailed(String s) {
                        Log.d("ss", ""+s);
                    }
                });
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

    }


//    public void snackBar(boolean isConnected) {
//        if(!isConnected) {
//            snackbar = Snackbar.make(rootLayout, "No Internet Connection! Please Try Again.", Snackbar.LENGTH_INDEFINITE);
//            snackbar.setDuration(5000);
//            snackbar.setActionTextColor(Color.WHITE);
//            View sbView = snackbar.getView();
//            sbView.setBackgroundColor(Color.RED);
//            snackbar.show();
//        }
//    }
}