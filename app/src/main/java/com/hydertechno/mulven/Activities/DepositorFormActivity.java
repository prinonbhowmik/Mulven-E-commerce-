package com.hydertechno.mulven.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hydertechno.mulven.Api.ApiUtils;
import com.hydertechno.mulven.Models.BankDepositModel;
import com.hydertechno.mulven.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DepositorFormActivity extends BaseActivity {
    private TextInputLayout depositorNameTIL,depositorPhoneTIL,depositorBankTIL,depositorAmountTIL;
    private TextInputEditText depositorNameET,depositorPhoneET,depositorBankET,depositorAmountET;
    private TextView submit_bankPaymentTV;
    private RelativeLayout depositorRootLayout,uploadImageBtn,progressRL;
    private Uri imageUri;
    private ImageView bankReceiptIV;
    private Toolbar toolbar;
    private String depositorName,depositorPhone,depositorBankName,depositAmount,orderId;
    private Snackbar snackbar;
    private SharedPreferences sharedPreferences;
    private String userToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dipositor_form);
        init();
        checkConnection();
        if (!isConnected) {
            snackBar(isConnected);
        }
        uploadImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkConnection();
                if (!isConnected) {
                    snackBar(isConnected);
                }else {
                    CropImage.activity()
                            .setFixAspectRatio(false)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setCropShape(CropImageView.CropShape.RECTANGLE)
                            .start(DepositorFormActivity.this);
                }
            }
        });

        submit_bankPaymentTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnection();
                if (!isConnected) {
                    snackBar(isConnected);
                }else {
                    depositorName = depositorNameET.getText().toString();
                    depositorPhone = depositorPhoneET.getText().toString();
                    depositorBankName = depositorBankET.getText().toString();
                    depositAmount = depositorAmountET.getText().toString();
                    if (TextUtils.isEmpty(depositorName)) {
                        depositorNameTIL.setError("Please enter name");
                        depositorNameET.requestFocus();
                    } else if (TextUtils.isEmpty(depositorPhone)) {
                        depositorPhoneTIL.setError("Please enter phone number");
                        depositorPhoneET.requestFocus();
                    } else if (TextUtils.isEmpty(depositorBankName)) {
                        depositorBankTIL.setError("Please enter bank name");
                        depositorBankET.requestFocus();
                    } else if (TextUtils.isEmpty(depositAmount)) {
                        depositorAmountTIL.setError("Please enter amount");
                        depositorAmountET.requestFocus();
                    }else if(imageUri ==null){
                        Toasty.normal(DepositorFormActivity.this, "Upload Bank Receipt ", Toasty.LENGTH_SHORT).show();
                    } else{

                        progressRL.setVisibility(View.VISIBLE);
                        File image_file = new File(imageUri.getPath());

                        Log.d("checkToken", userToken + " , " + imageUri.toString());

                        RequestBody userImage = RequestBody.create(MediaType.parse("image/*"), image_file);

                        MultipartBody.Part depo_slip = MultipartBody.Part.createFormData("depo_slip", image_file.getName(), userImage);
                        RequestBody depo_name = RequestBody.create(MediaType.parse("text/plain"), depositorName);
                        RequestBody depo_phone = RequestBody.create(MediaType.parse("text/plain"), depositorPhone);
                        RequestBody bank_name = RequestBody.create(MediaType.parse("text/plain"), depositorBankName);
                        RequestBody pay_am = RequestBody.create(MediaType.parse("text/plain"), depositAmount);
                        RequestBody order_id = RequestBody.create(MediaType.parse("text/plain"), orderId);
                        RequestBody token = RequestBody.create(MediaType.parse("text/plain"), userToken);

                        Call<BankDepositModel> call= ApiUtils.getUserService().sendBankDeposit(depo_name,
                                depo_phone,order_id, bank_name, pay_am, token, depo_slip);
                        call.enqueue(new Callback<BankDepositModel>() {
                            @Override
                            public void onResponse(Call<BankDepositModel> call, Response<BankDepositModel> response) {
                                if (response.isSuccessful() && response.code() == 200) {
                                    String status = response.body().getStatus();
                                    if (status.equals("1")) {
                                        progressRL.setVisibility(View.GONE);
                                        Toasty.success(DepositorFormActivity.this,""+response.body().getText(),Toasty.LENGTH_SHORT).show();
                                    }else{
                                        progressRL.setVisibility(View.GONE);
                                        Toasty.error(DepositorFormActivity.this,"Something went wrong!",Toasty.LENGTH_SHORT).show();
                                    }
                                }
                                else{
                                    progressRL.setVisibility(View.GONE);
                                    Toasty.error(DepositorFormActivity.this,"Something went wrong!",Toasty.LENGTH_SHORT).show();
                                }

                                Log.e("OnResponse=====>", response.body().getStatus() + " , " + response.body().getText());
                            }

                            @Override
                            public void onFailure(Call<BankDepositModel> call, Throwable t) {
                                Toasty.error(DepositorFormActivity.this,""+t.getMessage(),Toasty.LENGTH_SHORT).show();
                                progressRL.setVisibility(View.GONE);

                                Log.d("checkToken", "" + t.getMessage());
                            }
                        });
                    }

                }

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                imageUri = resultUri;
                bankReceiptIV.setImageURI(imageUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(DepositorFormActivity.this, "Failed"+error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void init(){
        Intent getInt=getIntent();
        orderId=getInt.getStringExtra("orderId");

        sharedPreferences = getSharedPreferences("MyRef", MODE_PRIVATE);
        userToken = sharedPreferences.getString("token",null);

        depositorNameET=findViewById(R.id.depositorNameET);
        depositorNameTIL=findViewById(R.id.depositorNameTIL);
        depositorPhoneET=findViewById(R.id.depositorPhoneET);
        depositorPhoneTIL=findViewById(R.id.depositorPhoneTIL);
        depositorBankET=findViewById(R.id.depositorBankET);
        depositorBankTIL=findViewById(R.id.depositorBankTIL);
        depositorAmountTIL=findViewById(R.id.depositorAmountTIL);
        depositorAmountET=findViewById(R.id.depositorAmountET);

        submit_bankPaymentTV=findViewById(R.id.submit_bankPaymentTV);
        uploadImageBtn=findViewById(R.id.uploadBtn);
        bankReceiptIV=findViewById(R.id.bankReceiptIV);
        depositorRootLayout=findViewById(R.id.depositorRootLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        progressRL=findViewById(R.id.progressRL);
        progressRL.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

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
    public void onNetworkConnectionChanged(boolean isConnected) {
        snackBar(isConnected);
    }


    public void snackBar(boolean isConnected) {
        if(!isConnected) {
            snackbar = Snackbar.make(depositorRootLayout, "No Internet Connection! Please Try Again.", Snackbar.LENGTH_INDEFINITE);
            snackbar.setDuration(5000);
            snackbar.setActionTextColor(Color.WHITE);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(Color.RED);
            snackbar.show();
        }
    }
}