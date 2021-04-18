package com.hydertechno.mulven.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hydertechno.mulven.Api.ApiInterface;
import com.hydertechno.mulven.Api.ApiUtils;
import com.hydertechno.mulven.Models.ChangePasswordModel;
import com.hydertechno.mulven.Models.UserProfile;
import com.hydertechno.mulven.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {
    private TextInputLayout currentPasswordTl,newPasswordTl,confirmPasswordTl;
    private TextInputEditText currentPassword,newPassword,confirmPassword;
    private TextView changePasswordTV;
    private int id,status;
    private String old_pass,new_pass,con_pass, token,message;
    private ApiInterface api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        init();

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        token = intent.getStringExtra("token");

        changePasswordTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                old_pass = currentPassword.getText().toString();
                new_pass = newPassword.getText().toString();
                con_pass = confirmPassword.getText().toString();
                hideKeyboardFrom(ChangePasswordActivity.this);
                if (TextUtils.isEmpty(old_pass)) {
                    currentPasswordTl.setError("Enter Current Password!");
                    currentPassword.requestFocus();
                }/* else if (currentPassword.length() < 6) {
                    currentPasswordTl.setError("At least 6 characters!");
                    currentPassword.requestFocus();
                }*/ else if (TextUtils.isEmpty(new_pass)) {
                    newPasswordTl.setError("Enter New Password!");
                    newPassword.requestFocus();
                } else if (new_pass.length() < 6) {
                    newPasswordTl.setError("At least 6 characters!");
                    newPassword.requestFocus();
                } else if (TextUtils.isEmpty(con_pass)) {
                    confirmPasswordTl.setError("Enter Re-Password!");
                    confirmPassword.requestFocus();
                } else if (con_pass.length() < 6) {
                    confirmPasswordTl.setError("At least 6 characters!");
                    confirmPassword.requestFocus();
                }/* else if (!confirmPassword.getText().toString().equals(newPassword.getText().toString())) {
                    confirmPasswordTl.setError("New password doesn't matched!");
                }*/ else {
                    changePassword(old_pass, new_pass, con_pass);
                }
            }
        });
    }

    private void init() {
        currentPasswordTl=findViewById(R.id.currentPasswordLT);
        newPasswordTl=findViewById(R.id.newPasswordLT);
        confirmPasswordTl=findViewById(R.id.confirmPasswordLT);
        currentPassword=findViewById(R.id.currentPasswordEt);
        newPassword=findViewById(R.id.newPasswordEt);
        confirmPassword=findViewById(R.id.confirmPasswordEt);
        changePasswordTV=findViewById(R.id.changePasswordTV);
        api= ApiUtils.getUserService();
    }

    private void changePassword(String pass1, String pass2, String pass3) {
        Call<ChangePasswordModel> call = api.changePassword(token,pass1,pass2,pass3);
        call.enqueue(new Callback<ChangePasswordModel>() {
            @Override
            public void onResponse(Call<ChangePasswordModel> call, Response<ChangePasswordModel> response) {
                if (response.isSuccessful()) {
                    status = response.body().getStatus();
                    message=response.body().getMessage();
                    if(status==1){
                        Toast.makeText(ChangePasswordActivity.this, ""+message, Toast.LENGTH_SHORT).show();
                    }else if(status==0){
                        Toast.makeText(ChangePasswordActivity.this, ""+message, Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(ChangePasswordActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChangePasswordModel> call, Throwable t) {

            }
        });
    }


    public void changePasswordBack(View view) {
        finish();
    }

    private void hideKeyboardFrom(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getWindow().getDecorView().getRootView().getWindowToken(), 0);
    }
}