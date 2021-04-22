package com.hydertechno.mulven.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hydertechno.mulven.R;

public class AddressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
    }

    public void addressBack(View view) {
        Intent intent=new Intent( AddressActivity.this,MainActivity.class);
        intent.putExtra("fragment","profile");
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent( AddressActivity.this,MainActivity.class);
        intent.putExtra("fragment","profile");
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }
}