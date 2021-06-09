package com.hydertechno.mulven.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hydertechno.mulven.Api.Config;
import com.hydertechno.mulven.R;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class NotificationDetailsActivity extends AppCompatActivity {
    private TextView NTitle,NBody;
    private ImageView NImage;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_details);
        init();

        Intent intent=getIntent();
        NTitle.setText(intent.getStringExtra("title"));
        NBody.setText(intent.getStringExtra("body"));
        String image=intent.getStringExtra("image");
        if (image != null)
            Picasso.get().load(image).placeholder(R.drawable.status_tag).error(R.drawable.mv_logo_black).into(NImage);
    }
    public void init(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        NImage=findViewById(R.id.notificationImage);
        NTitle=findViewById(R.id.titleTv);
        NBody=findViewById(R.id.bodyTv);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}