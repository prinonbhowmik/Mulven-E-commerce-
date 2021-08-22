package com.hydertechno.mulven.Activities;

import androidx.activity.result.ActivityResult;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.hydertechno.mulven.Adapters.PaymentMethodsAdapter;
import com.hydertechno.mulven.Api.ApiUtils;
import com.hydertechno.mulven.Fragments.LoadingDialog;
import com.hydertechno.mulven.Interface.OnPMethodItemClickListener;
import com.hydertechno.mulven.Models.InvoiceDetailsModel;
import com.hydertechno.mulven.Models.PaymentMethodModel;
import com.hydertechno.mulven.Models.RequiredDataModel;
import com.hydertechno.mulven.Models.WalletPayStatus;
import com.hydertechno.mulven.R;
import com.hydertechno.mulven.Utilities.BetterActivityResult;
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

public class PaymentMethodsActivity extends BaseActivity implements OnPMethodItemClickListener, BetterActivityResult.OnActivityResult<ActivityResult> {
    private TextView payInvoiceIdTV,payAmountTV;
    private RecyclerView methodsRecyclerView;
    private PaymentMethodsAdapter methodsAdapter;
    private SharedPreferences sharedPreferences;
    Toolbar toolbar;
    private double fullAmount;
    private Dialog  makePaymentDialog;
    private String orderId,token;
    private int userId;
    private boolean isCampaignAvailable;
    private double amount;

    private List<PaymentMethodModel> methodModelsList = new ArrayList<>();

    private Dialog bankPaymentDialog;

    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_methods);
        init();

        Intent getInt=getIntent();
        orderId=getInt.getStringExtra("orderId");
        fullAmount=getInt.getDoubleExtra("FAmount",0.0);
        isCampaignAvailable=getInt.getBooleanExtra("isCampaign", false);
        payAmountTV.setText(""+fullAmount);
        payInvoiceIdTV.setText(orderId);

        getData();

    }

    private void getData() {
        methodModelsList=new ArrayList<>();
        methodModelsList.add(new PaymentMethodModel(1,"Bank", "Pay by bank account", R.drawable.bank_transfer));
        methodModelsList.add(new PaymentMethodModel(2, "Nagad", "Pay from your Nagad account", R.drawable.nagad));
        methodModelsList.add(new PaymentMethodModel(3,"Shurjo Pay", "Choose your desire payment method by Shurjo Pay", R.drawable.shurjo_pay));
        if (!isCampaignAvailable) {
            methodModelsList.add(new PaymentMethodModel(4,"Account", "Pay by Mulven Account Wallet", R.drawable.mulven_wallet));
            methodModelsList.add(new PaymentMethodModel(5,"Voucher", "Pay by Mulven Voucher Wallet", R.drawable.mulven_wallet));
            methodModelsList.add(new PaymentMethodModel(6,"Cashback", "Pay by Mulven Cashback Wallet", R.drawable.mulven_wallet));
        }

        methodsAdapter.updateData(methodModelsList);
    }

    public void init(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
//        toolbar.setTitleTextAppearance(PaymentMethodsActivity.this,R.style.Comfortaa_bold);

        sharedPreferences = getSharedPreferences("MyRef", MODE_PRIVATE);
        token = sharedPreferences.getString("token", null);
        userId = sharedPreferences.getInt("userId",0);
        payInvoiceIdTV=findViewById(R.id.payInvoiceIdTV);
        payAmountTV=findViewById(R.id.payAmountTV);
        methodsRecyclerView=findViewById(R.id.methodsRecyclerView);

        methodsAdapter = new PaymentMethodsAdapter(methodModelsList,this);
        methodsRecyclerView.setLayoutManager(new LinearLayoutManager(PaymentMethodsActivity.this, LinearLayoutManager.VERTICAL, false));
        methodsRecyclerView.setAdapter(methodsAdapter);

        loadingDialog = LoadingDialog.instance();
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
        checkConnection();
        if (item.getId() != 1 && !isConnected) {
            showToast("No Internet Connection!");
        }
        switch (item.getId()) {
            case 1:
                bankPaymentDialog = new Dialog(context);
                bankPaymentDialog.setContentView(R.layout.bank_payment_layout_design);
                bankPaymentDialog.setCancelable(true);
                TextView bankAmount;
                ImageView bCloseIV;
                bCloseIV=bankPaymentDialog.findViewById(R.id.bCloseIV);
                bankAmount=bankPaymentDialog.findViewById(R.id.bb1);
                bankAmount.setText(" "+fullAmount);
                bankPaymentDialog.show();
                bCloseIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bankPaymentDialog.dismiss();
                    }
                });
                break;
            case 2:
                //nagad
                paymentDialog("a",2);
                break;
            case 3:
                paymentDialog("a",3);
                break;
            case 4:
                paymentDialog(item.getTitle(),4);
                break;
            case 5:
                paymentDialog(item.getTitle(),5);
                break;
            case 6:
                paymentDialog(item.getTitle(),6);
                break;
        }
    }

    private void checkWalletPay(String pay_method, String amount, String order_id) {
        loadingDialog.show(getSupportFragmentManager(), null);
        Call<WalletPayStatus> call = ApiUtils.getUserService().checkWalletPay(token, pay_method, amount, order_id);
        call.enqueue(new Callback<WalletPayStatus>() {
            @Override
            public void onResponse(Call<WalletPayStatus> call, Response<WalletPayStatus> response) {
                if (response.isSuccessful()) {
                    Log.e("Response ===> ", response.toString());
                    if (response.code() == 200) {
                        assert response.body() != null;
                        if (response.body().getStatus().equals("0")) {
                            String msg = amount + "৳ is not available on your " + pay_method + " wallet";
                            Toasty.error(context, msg, Toasty.LENGTH_LONG).show();
                        } else {
                            setIntentSuccess();
                        }
                    }
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(Call<WalletPayStatus> call, Throwable t) {
                loadingDialog.dismiss();
                Toasty.error(context, t.getMessage(), Toasty.LENGTH_LONG).show();
            }
        });
    }

    private void setIntentSuccess() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("resultPay", "Success");
        setResult(Activity.RESULT_OK, resultIntent);
        PaymentMethodsActivity.this.finish();
    }
    private void paymentDialog(String title,int indexNumber){
                    makePaymentDialog = new Dialog(PaymentMethodsActivity.this);
                    makePaymentDialog.setContentView(R.layout.make_payment_layout_design);
                    makePaymentDialog.setCancelable(false);
                    makePaymentDialog.show();
                    Window window = makePaymentDialog.getWindow();
                    window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    EditText paymentAmount= makePaymentDialog.findViewById(R.id.makePayET);
                    TextView continuePayTV= makePaymentDialog.findViewById(R.id.continuePayTV);
                    TextView closePayTV= makePaymentDialog.findViewById(R.id.closePayTV);
                    paymentAmount.setHint(""+fullAmount);
                    closePayTV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            makePaymentDialog.dismiss();
                        }
                    });

                    continuePayTV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                String am=paymentAmount.getText().toString();
                                if(!TextUtils.isEmpty(am)){
                                    try {
                                        double value=Double.parseDouble(am);
                                        if(value>=10){
                                            if(indexNumber==2){
                                                Intent intent = new Intent(PaymentMethodsActivity.this, WebViewActivity.class);
                                                intent.putExtra("isTerms", false);
                                                intent.putExtra("url","https://mulven.com/app_nagad_payment?order_id="+ orderId +"&amount="+ value +"&user_id=" + userId);
//                                              startActivity(intent);
                                                activityLauncher.launch(intent, PaymentMethodsActivity.this);
                                            }else if(indexNumber==3){
                                                getShurjoPayment(value);
                                            }else if(indexNumber==4||indexNumber==5||indexNumber==6){

                                                checkWalletPay(title, String.valueOf(value), orderId);
                                            }
                                            makePaymentDialog.dismiss();
                                        }else
                                            Toast.makeText(PaymentMethodsActivity.this, "Minimum amount 10Tk", Toast.LENGTH_SHORT).show();

                                    }catch (Exception e){
                                        Toast.makeText(PaymentMethodsActivity.this, "Amount must be number", Toast.LENGTH_SHORT).show();
                                    }


                                }else{
                                    Toast.makeText(PaymentMethodsActivity.this, "Please enter your amount", Toast.LENGTH_SHORT).show();
                                }

                            }
                    });
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
//                                    recreate();
                                    setIntentSuccess();
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

    @Override
    public void onActivityResult(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            if (data != null && data.getStringExtra("result") != null) {
                if (data.getStringExtra("result").equals("Success")) {
                    Intent intent = new Intent();
                    intent.putExtra("resultPay", data.getStringExtra("result"));
                    setResult(Activity.RESULT_OK, intent);
                    PaymentMethodsActivity.this.finish();
                }
            }
        }
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





/*else{
                    String amount=dueTV.getText().toString().substring(2);
                    makePaymentDialog = new Dialog(PlaceOrderDetailsActivity.this);
                    makePaymentDialog.setContentView(R.layout.make_payment_layout_design);
                    makePaymentDialog.setCancelable(false);
                    makePaymentDialog.show();
                    Window window = makePaymentDialog.getWindow();
                    window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    EditText paymentAmount= makePaymentDialog.findViewById(R.id.makePayET);
                    TextView continuePayTV= makePaymentDialog.findViewById(R.id.continuePayTV);
                    TextView closePayTV= makePaymentDialog.findViewById(R.id.closePayTV);
                    paymentAmount.setHint(amount);
                    closePayTV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            makePaymentDialog.dismiss();
                        }
                    });

                    continuePayTV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                String am=paymentAmount.getText().toString();
                                if(!TextUtils.isEmpty(am)){
                                    try {
                                        double value=Double.parseDouble(am);
                                        if(value>0){
                                            Intent intent = new Intent(PlaceOrderDetailsActivity.this, PaymentMethodsActivity.class);
                                            intent.putExtra("amount", value);
                                            intent.putExtra("FAmount", dueTV.getText().toString());
                                            intent.putExtra("orderId", OrderId);
                                            intent.putExtra("isCampaign", isCampaignAvailable);
                                            activityLauncher.launch(intent, PlaceOrderDetailsActivity.this);
                                            makePaymentDialog.dismiss();
                                        }else
                                            Toast.makeText(PlaceOrderDetailsActivity.this, "Amount can not 0", Toast.LENGTH_SHORT).show();

                                    }catch (Exception e){
                                        Toast.makeText(PlaceOrderDetailsActivity.this, "Amount must be number", Toast.LENGTH_SHORT).show();
                                    }


                                }else{
                                    Toast.makeText(PlaceOrderDetailsActivity.this, "Please enter your amount", Toast.LENGTH_SHORT).show();
                                }

                            }
                    });

                }
*/