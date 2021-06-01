package com.hydertechno.mulven.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.os.UserHandle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.hydertechno.mulven.Activities.AddressActivity;
import com.hydertechno.mulven.Activities.ChangePasswordActivity;
import com.hydertechno.mulven.Activities.MainActivity;
import com.hydertechno.mulven.Activities.PlaceOrderListActivity;
import com.hydertechno.mulven.Activities.ProfileActivity;
import com.hydertechno.mulven.Api.ApiUtils;
import com.hydertechno.mulven.Api.Config;
import com.hydertechno.mulven.Internet.Connection;
import com.hydertechno.mulven.Internet.ConnectivityReceiver;
import com.hydertechno.mulven.Models.UserProfile;
import com.hydertechno.mulven.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener {
    private LinearLayout checkBalanceLayout,addressLayout,profileLayout;
    private RelativeLayout bottomRL;
    private TextView username,phoneNo;
    private DrawerLayout drawerLayout;
    private ImageView navIcon;
    private CircleImageView userImageIv;
    private Dialog dialog;
    private Animation upAnimation,downAnimation;
    private RelativeLayout paymentHistoryRL,changePasswordRL;
    private SharedPreferences sharedPreferences;
    private String token,name,phone;
    private int id,userId;
    private RelativeLayout rootLayout;
    private Snackbar snackbar;
    private boolean isConnected;
    private ConnectivityReceiver connectivityReceiver;
    private IntentFilter intentFilter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        init(view);

        token = sharedPreferences.getString("token",null);
        name = sharedPreferences.getString("userName",null);
        phone = sharedPreferences.getString("userPhone",null);

        Log.d("ShowToken",token);
        username.setText(name);
        phoneNo.setText(phone);
        Call<UserProfile> call = ApiUtils.getUserService().getUserData(token);
        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if (response.isSuccessful()){
                    id = response.body().getId();
                    if(response.body().getUser_photo()!=null) {
                        try {
                            Picasso.get()
                                    .load(Config.IMAGE_LINE + response.body().getUser_photo())
                                    .into(userImageIv);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                else {

                    try {
                        Picasso.get()
                                .load(R.drawable.demo_profile)
                                .into(userImageIv);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {

            }
        });

        drawerLayout=getActivity().findViewById(R.id.drawerLayout);
        navIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
                hideKeyboardFrom(view.getContext());
            }
        });

        checkBalanceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnection();
                if (!isConnected) {
                    snackBar(isConnected);
                } else {
                    dialog = new Dialog(view.getContext());
                    dialog.setContentView(R.layout.check_balance_layout_design);

                    dialog.setCancelable(true);

                    dialog.show();
                    Window window = dialog.getWindow();
                    window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                }
            }
        });

        addressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnection();
                if (!isConnected) {
                    snackBar(isConnected);
                } else {
                    // startActivity(new Intent(getActivity(), AddressActivity.class));
                    Intent intent = new Intent(getActivity(), AddressActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    getActivity().finish();
                }
            }
        });

        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnection();
                if (!isConnected) {
                    snackBar(isConnected);
                } else {
                    Intent intent = new Intent(getActivity(), ProfileActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    getActivity().finish();
                }
            }
        });

        paymentHistoryRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnection();
                if (!isConnected) {
                    snackBar(isConnected);
                } else {
                Intent intent=new Intent(getActivity(), PlaceOrderListActivity.class);
                intent.putExtra("from","profile");
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                //getActivity().finish();
                //startActivity(new Intent(getActivity(), PlaceOrderListActivity.class).putExtra("id",id).putExtra("token",token));
            }
            }
        });

        changePasswordRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnection();
                if (!isConnected) {
                    snackBar(isConnected);
                } else {
                    Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
                    intent.putExtra("token", token);
                    intent.putExtra("activity", "profile");
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                /*startActivity(new Intent(getActivity(), ChangePasswordActivity.class).putExtra("id",id)
                        .putExtra("token",token).putExtra("activity","profile"));*/
                }
            }
        });


        return view;
    }

    private void init(View view) {
        rootLayout=view.findViewById(R.id.fragment_profile_rootLayout);
        intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        connectivityReceiver = new ConnectivityReceiver();
        navIcon=view.findViewById(R.id.navIcon);
        upAnimation=AnimationUtils.loadAnimation(getContext(),R.anim.slide_up);
        downAnimation=AnimationUtils.loadAnimation(getContext(),R.anim.slide_down);
        username = view.findViewById(R.id.userName);
        userImageIv = view.findViewById(R.id.userImageIV);
        phoneNo = view.findViewById(R.id.userPhoneNo);
        bottomRL=view.findViewById(R.id.bottomRL);
        bottomRL.setAnimation(upAnimation);
        checkBalanceLayout=view.findViewById(R.id.balanceLayout);
        addressLayout=view.findViewById(R.id.addressLayout);
        profileLayout=view.findViewById(R.id.profileLayout);
        paymentHistoryRL=view.findViewById(R.id.paymentHistoryRL);
        changePasswordRL=view.findViewById(R.id.changePasswordRL);
        sharedPreferences = getContext().getSharedPreferences("MyRef", MODE_PRIVATE);

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