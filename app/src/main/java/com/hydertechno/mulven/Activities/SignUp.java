package com.hydertechno.mulven.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hydertechno.mulven.Api.ApiUtils;
import com.hydertechno.mulven.Internet.Connection;
import com.hydertechno.mulven.Internet.ConnectivityReceiver;
import com.hydertechno.mulven.Models.UserProfile;
import com.hydertechno.mulven.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private TextInputLayout nameTIL, dobTIL, passTIL,addressTIL,phnTIL;
    private TextInputEditText nameTIET, dobTIET, passTIET, phnTIET, addressTIET;
    private TextView condition;
    private Button signUpBtn;
    private String currentDate, name, phone, dob, pass, address;
    private CheckBox termsCheckBox;
    private RelativeLayout rootLayout;
    private Snackbar snackbar;
    private boolean isConnected;
    private ConnectivityReceiver connectivityReceiver;
    private IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();
        checkConnection();
        if (!isConnected) {
            snackBar(isConnected);
        }
        dobTIET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate();
            }
        });
        condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnection();
                if (!isConnected) {
                    snackBar(isConnected);
                }else{
                startActivity(new Intent(SignUp.this, WebViewActivity.class).putExtra("url", "https://mulven.com/terms-conditions"));
                }
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnection();
                if (!isConnected) {
                    snackBar(isConnected);
                }else{
                name = nameTIET.getText().toString();
                phone = phnTIET.getText().toString();
                dob = dobTIET.getText().toString();
                pass = passTIET.getText().toString();
                address = addressTIET.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    nameTIL.setError("Please enter full name!");
                    nameTIET.requestFocus();
                }
                if (TextUtils.isEmpty(phone)) {
                    phnTIET.setError("Please enter phone number!");
                    phnTIET.requestFocus();
                } else if (TextUtils.isEmpty(dob)) {
                    dobTIL.setError("Please select Date of Birth!");
                    dobTIET.requestFocus();
                } else if (TextUtils.isEmpty(pass)) {
                    passTIL.setError("Please enter password!");
                    passTIET.requestFocus();
                } else if (TextUtils.isEmpty(address)) {
                    addressTIL.setError("Please enter your address!");
                    addressTIET.requestFocus();
                } else if (!termsCheckBox.isChecked()) {
                    Toast.makeText(SignUp.this, "Please accept terms & conditions!", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(name, phone, dob, pass, address);
                }
            }
            }
        });

    }

    private void registerUser(String name, String phone, String dob, String pass, String address) {
        Call<UserProfile> call = ApiUtils.getUserService().registerUser(name,phone,dob,pass,address);
        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if (response.isSuccessful()){
                    String status = response.body().getStatus();

                    if (status.equals("0")){
                        String msg = response.body().getMessage();
                        Toast.makeText(SignUp.this, ""+msg, Toast.LENGTH_LONG).show();
                    }else{
                        String token = response.body().getToken();
                        String msg = response.body().getMessage();
                        int userId=response.body().getId();
                        SharedPreferences sharedPreferences = getSharedPreferences("MyRef", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("token", token);
                        editor.putInt("loggedIn", 1);
                        editor.putInt("userId",userId);
                        editor.commit();
                        Log.d("ShowToken",token);
                        Toast.makeText(SignUp.this, ""+msg, Toast.LENGTH_LONG).show();
                        startActivity(new Intent(SignUp.this,MainActivity.class).putExtra("home","home"));
                        finish();
                    }

                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {

            }
        });
    }


    private void init() {
        rootLayout = findViewById(R.id.sign_up_rootLayout);
        intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        connectivityReceiver = new ConnectivityReceiver();
        nameTIL = findViewById(R.id.name_LT);
        dobTIL = findViewById(R.id.date_LT);
        passTIL = findViewById(R.id.password_LT);
        addressTIL = findViewById(R.id.address_LT);
        phnTIL = findViewById(R.id.phone_LT);
        nameTIET = findViewById(R.id.name_Et);
        dobTIET = findViewById(R.id.date_ET);
        phnTIET = findViewById(R.id.phone_Et);
        passTIET = findViewById(R.id.password_Et);
        addressTIET = findViewById(R.id.address_Et);
        termsCheckBox=findViewById(R.id.termsCheckBox);
        signUpBtn = findViewById(R.id.sign_upBtn);
        condition = findViewById(R.id.conditions);

    }

    private void getDate() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                currentDate = year + "-" + month + "-" + day;
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;

                try {
                    date = dateFormat.parse(currentDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                dobTIET.setText(dateFormat.format(date));
            }
        };

        Calendar calendar = Calendar.getInstance();
        // calendar.add(Calendar.YEAR, -18);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, dateSetListener, year, month, day);

        datePickerDialog.show();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialog2 = new AlertDialog.Builder(this);
        dialog2.setTitle("Exit!");
        dialog2.setIcon(R.drawable.applogo);
        dialog2.setMessage("Do you want to exit this page?");
        dialog2.setCancelable(false);
        dialog2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog2, int which) {
                finish();
            }
        });
        dialog2.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog2, int which) {
                dialog2.dismiss();
            }
        });
        AlertDialog alertDialog2 = dialog2.create();
        alertDialog2.show();

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        snackBar(isConnected);
    }

    private void checkConnection() {
        isConnected = ConnectivityReceiver.isConnected();
    }
    private void snackBar(boolean isConnected) {
        if(!isConnected) {
            snackbar = Snackbar.make(rootLayout, "No Internet Connection!", Snackbar.LENGTH_INDEFINITE).setAction("ReTry", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recreate();
                }
            });
            snackbar.setDuration(5000);
            snackbar.setActionTextColor(Color.WHITE);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(Color.RED);
            snackbar.show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(connectivityReceiver, intentFilter);
    }
    @Override
    protected void onResume() {

        // register connection status listener
        Connection.getInstance().setConnectivityListener(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try{
            if(connectivityReceiver!=null)
                unregisterReceiver(connectivityReceiver);

        }catch(Exception e){}

    }

    @Override
    protected void onStop() {
        try{
            if(connectivityReceiver!=null)
                unregisterReceiver(connectivityReceiver);

        }catch(Exception e){}

        super.onStop();
    }

}