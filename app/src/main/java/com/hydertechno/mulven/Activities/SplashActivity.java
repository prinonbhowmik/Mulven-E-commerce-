package com.hydertechno.mulven.Activities;

import androidx.appcompat.app.AlertDialog;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.material.snackbar.Snackbar;
import com.hanks.htextview.base.HTextView;
import com.hydertechno.mulven.Api.ApiUtils;
import com.hydertechno.mulven.BuildConfig;
import com.hydertechno.mulven.Models.ResponseUpdate;
import com.hydertechno.mulven.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends BaseActivity {

    public RelativeLayout rootLayout;
    private Snackbar snackbar;

    private ImageView imageLogo;
    Animation logoanim;
    private  static int splash_time_out=2600;
    private HTextView textViewLine;
    int delay = 2500; //milliseconds
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        rootLayout = findViewById(R.id.rootLayout);
        imageLogo=findViewById(R.id.imageLogo);
        textViewLine= findViewById(R.id.textViewLine1);
        logoanim = AnimationUtils.loadAnimation(this,R.anim.blink);
        imageLogo.setAnimation(logoanim);
/*
        handler = new Handler();
        handler.postDelayed(new Runnable(){
            public void run(){

                handler.postDelayed(this, 2500);
                textViewLine.animateText("Your Online Shopping Partner");

            }
        }, 100);*/
        textViewLine.animateText("Your Online Shopping Partner");

        checkConnection();
        if (!isConnected) {
            gotToHome();
        } else {
            checkUpdate();
        }
    }

    private void checkUpdate() {
        Call<ResponseUpdate> call = ApiUtils.getUserService().versionCheck();
        call.enqueue(new Callback<ResponseUpdate>() {
            @Override
            public void onResponse(Call<ResponseUpdate> call, Response<ResponseUpdate> response) {
                if (response.isSuccessful() && response.code() == 200){
                    String versionName = BuildConfig.VERSION_NAME;
                    String vName= response.body().getApkVersion();

                    if (!versionName.equals(vName)) {
                        showAlert();
                    } else {
                        gotToHome();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseUpdate> call, Throwable t) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(SplashActivity.this);
                dialog.setTitle("Error!");
                dialog.setIcon(R.drawable.applogo);
                dialog.setMessage("Could not connect to server for update App, Please contact support!");
                dialog.setCancelable(false);
                dialog.setPositiveButton("Ok", new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(android.content.DialogInterface dialogInterface, int i) {
                        gotToHome();
                    }
                });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
        });
    }

    private void showAlert() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(SplashActivity.this);
        dialog.setTitle("New Version!");
        dialog.setIcon(R.drawable.applogo);
        dialog.setMessage("New version is available. Please update for latest features.");
        dialog.setCancelable(false);
        dialog.setPositiveButton("Ok", new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(android.content.DialogInterface dialogInterface, int i) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                System.exit(0);
            }
        });
        dialog.setNegativeButton("Later", new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(android.content.DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                System.exit(0);
            }
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    private void gotToHome() {
        new Handler(Looper.myLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class).putExtra("fragment","home"));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        },splash_time_out);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        snackBar(isConnected);
    }

    public void snackBar(boolean isConnected) {
        if(!isConnected) {
            snackbar = Snackbar.make(rootLayout, "No Internet Connection! Please Try Again.", Snackbar.LENGTH_INDEFINITE);
            snackbar.setDuration(5000);
            snackbar.setActionTextColor(Color.WHITE);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(Color.RED);
            snackbar.show();
        }
    }
}