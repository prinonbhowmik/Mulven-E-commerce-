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
import com.smarteist.autoimageslider.Transformations.TossTransformation;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {
    private TextInputLayout currentPasswordTl,newPasswordTl,confirmPasswordTl;
    private TextInputEditText currentPassword,newPassword,confirmPassword;
    private TextView changePasswordTV;
    private String activity,phone;
    private int id,status;
    private String old_pass,new_pass,con_pass, token,message;
    private ApiInterface api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        init();

        Intent intent = getIntent();
        activity = intent.getStringExtra("activity");

        if (activity.equals("profile")){
            id = intent.getIntExtra("id", 0);
            token = intent.getStringExtra("token");
        }else if(activity.equals("forgot")){
            currentPasswordTl.setVisibility(View.GONE);
            phone = intent.getStringExtra("phone");
        }

        changePasswordTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activity.equals("profile")){
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
                else if (activity.equals("forgot")){
                    new_pass = newPassword.getText().toString();
                    con_pass = confirmPassword.getText().toString();
                    if (TextUtils.isEmpty(new_pass)) {
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
                        forgotPass(new_pass, con_pass);
                    }
                }
            }
        });
    }

    private void forgotPass(String new_pass, String con_pass) {
        if (new_pass.equals(con_pass)){
            Call<ChangePasswordModel> call = api.changePassword(phone,con_pass);
            call.enqueue(new Callback<ChangePasswordModel>() {
                @Override
                public void onResponse(Call<ChangePasswordModel> call, Response<ChangePasswordModel> response) {
                    if (response.isSuccessful()){
                        int status = response.body().getStatus();
                        if(status==1){
                            Toast.makeText(ChangePasswordActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ChangePasswordActivity.this,MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("fragment","login");
                            startActivity(intent);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ChangePasswordModel> call, Throwable t) {

                }
            });
        }else{
            Toast.makeText(this, "Confirm password doesn't match!", Toast.LENGTH_SHORT).show();
        }
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
                        finish();
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