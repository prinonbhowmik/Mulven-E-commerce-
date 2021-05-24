package com.hydertechno.mulven.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hydertechno.mulven.Activities.MainActivity;
import com.hydertechno.mulven.Activities.PhoneNumber;
import com.hydertechno.mulven.Activities.ProfileActivity;
import com.hydertechno.mulven.Activities.SignUp;
import com.hydertechno.mulven.Api.ApiInterface;
import com.hydertechno.mulven.Api.ApiUtils;
import com.hydertechno.mulven.Internet.ConnectivityReceiver;
import com.hydertechno.mulven.Models.UserProfile;
import com.hydertechno.mulven.R;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class AccountFragment extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener {
    private RelativeLayout loginRelative;
    private TextInputLayout phoneTIL,passwordTIL;
    private TextInputEditText phoneTIET,passwordTIET;
    private TextView forgetPasswordTV,newRegisterTV;
    private Button logInBtn;
    private String phone,password;
    private DrawerLayout drawerLayout;
    private ImageView navIcon;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private RelativeLayout rootLayout;
    private Snackbar snackbar;
    private boolean isConnected;
    private ConnectivityReceiver connectivityReceiver;
    private IntentFilter intentFilter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_account, container, false);
        init(view);
        drawerLayout=getActivity().findViewById(R.id.drawerLayout);
        newRegisterTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnection();
                if (!isConnected) {
                    snackBar(isConnected);
                } else {
                    startActivity(new Intent(getContext(), SignUp.class));
                }
            }
        });
        navIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
                hideKeyboardFrom(view.getContext());
            }
        });
        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnection();
                if (!isConnected) {
                    snackBar(isConnected);
                } else {
                phone = phoneTIET.getText().toString();
                password = passwordTIET.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    hideKeyboardFrom(getContext());
                    phoneTIL.setError("Please Enter Phone Number");
                    phoneTIET.requestFocus();
                } /*else if (phone.length() != 11) {
                    hideKeyboardFrom(getContext());
                    phoneTIL.setError("Please Provide Correct Phone Number");
                    phoneTIET.requestFocus();
                }*/ else if (TextUtils.isEmpty(password)) {
                    hideKeyboardFrom(getContext());
                    passwordTIL.setError("Please Enter Password");
                    passwordTIET.requestFocus();
                } else if (password.length() < 5) {
                    hideKeyboardFrom(getContext());
                    passwordTIL.setError("Minimum 6 digits password");
                    passwordTIET.requestFocus();
                } else {
                    phoneTIL.setErrorEnabled(false);
                    passwordTIL.setErrorEnabled(false);
                    String phone = phoneTIET.getText().toString();
                    String password = passwordTIET.getText().toString();
                    hideKeyboardFrom(getContext());
                    userLogin(phone, password);
                }
                 /*if (phone.length() == 11) {
                    hideKeyboardFrom(getContext());
                    phoneTIL.setErrorEnabled(false);
                }*/
            }
        }
        });

        forgetPasswordTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), PhoneNumber.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                //getActivity().finish();
            }
        });
        return view;
    }

    private void userLogin(String phone, String password) {
        Call<UserProfile> call = ApiUtils.getUserService().userLogin(phone,password);
        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if (response.isSuccessful()){
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();
                    Log.d("CheckStatus",status);
                    if (status.equals("1")){
                        String token = response.body().getToken();
                        Call<UserProfile> call2 = ApiUtils.getUserService().getUserData(token);
                        call2.enqueue(new Callback<UserProfile>() {
                            @Override
                            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                                String name = response.body().getFull_name();
                                String phone = response.body().getPhone();
                                int userId=response.body().getId();
                                SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyRef", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("token", token);
                                editor.putString("userName",name);
                                editor.putString("userPhone",phone);
                                editor.putInt("loggedIn", 1);
                                editor.putInt("userId",userId);
                                editor.apply();
                                Log.d("ShowToken",token);
                                Log.d("ShowToken",name+","+phone);
                                Toasty.success(getContext(), ""+message).show();
                                Intent intent = new Intent(getContext(), MainActivity.class);
                                intent.putExtra("fragment","home");
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                getActivity().finish();
                            }
                            @Override
                            public void onFailure(Call<UserProfile> call, Throwable t) {

                            }
                        });

                    }else if(status.equals("0")){
                        Toasty.success(getContext(), ""+message).show();
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        intent.putExtra("fragment","home");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        getActivity().finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                Log.d("ErrorKi",t.getMessage());
            }
        });
    }

    private void init(View view) {
        rootLayout=view.findViewById(R.id.account_rootLayout);
        intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        connectivityReceiver = new ConnectivityReceiver();
        loginRelative=view.findViewById(R.id.logInRelative);
        phoneTIL=view.findViewById(R.id.phone_LT);
        passwordTIL=view.findViewById(R.id.password_LT);
        phoneTIET=view.findViewById(R.id.phone_ET);
        passwordTIET=view.findViewById(R.id.password_ET);
        forgetPasswordTV=view.findViewById(R.id.forget_pass_TV);

        newRegisterTV=view.findViewById(R.id.registerTV);
        logInBtn=view.findViewById(R.id.loginBtn);
        navIcon=view.findViewById(R.id.navIcon);
        sharedPreferences = getContext().getSharedPreferences("MyRef", MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }

    private void hideKeyboardFrom(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getActivity().getWindow().getDecorView().getRootView().getWindowToken(), 0);
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
                    //recreate();
                }
            });
            snackbar.setDuration(5000);
            snackbar.setActionTextColor(Color.WHITE);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(Color.RED);
            snackbar.show();
        }
    }
/*
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
    }*/
}