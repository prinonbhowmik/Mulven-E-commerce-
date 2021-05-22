package com.hydertechno.mulven.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hydertechno.mulven.R;

import org.w3c.dom.Text;

public class AboutActivity extends AppCompatActivity {
    private RelativeLayout aboutRelativeLayout,helpRelativeLayout;
    private TextView toolbarTitleTV;
    private String layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        init();
        Intent intent=getIntent();
        layout=intent.getStringExtra("layout").toString();
        if(layout.equals("about")){
            aboutRelativeLayout.setVisibility(View.VISIBLE);
            toolbarTitleTV.setText("About");
        }else if(layout.equals("help")){
            helpRelativeLayout.setVisibility(View.VISIBLE);
            toolbarTitleTV.setText("Help");
        }
    }

    private void init() {
        aboutRelativeLayout=findViewById(R.id.aboutRelativeLayout);
        helpRelativeLayout=findViewById(R.id.helpRelativeLayout);
        toolbarTitleTV=findViewById(R.id.toolbarTitleTV);
    }

    public void aboutBack(View view) {
        finish();
    }
}