package com.hydertechno.mulven.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.hanks.htextview.base.HTextView;
import com.hydertechno.mulven.Fragments.HomeFragment;
import com.hydertechno.mulven.R;

public class SplashActivity extends AppCompatActivity {
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
        imageLogo=findViewById(R.id.imageLogo);
        textViewLine= findViewById(R.id.textViewLine1);
        logoanim = AnimationUtils.loadAnimation(this,R.anim.blink);
        imageLogo.setAnimation(logoanim);
        new Handler(Looper.myLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class).putExtra("fragment","home"));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        },splash_time_out);
/*
        handler = new Handler();
        handler.postDelayed(new Runnable(){
            public void run(){

                handler.postDelayed(this, 2500);
                textViewLine.animateText("Your Online Shopping Partner");

            }
        }, 100);*/
        textViewLine.animateText("Your Online Shopping Partner");


    }
}