package com.hydertechno.mulven.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hydertechno.mulven.R;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class AboutActivity extends AppCompatActivity {
    private RelativeLayout aboutRelativeLayout,helpRelativeLayout;
    private TextView toolbarTitleTV;
    private CircleImageView mv_facebookIV,mv_emailIV,mv_mapIV,mv_phoneIV,mv_instagramIV;
    private String layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        init();
        Intent intent=getIntent();
        layout=intent.getStringExtra("layout").toString();
        toolbarTitleTV.setText(layout);
        if(layout.equals("About")){
            aboutRelativeLayout.setVisibility(View.VISIBLE);
        }else if(layout.equals("Contact")){
            helpRelativeLayout.setVisibility(View.VISIBLE);
        }
        mv_facebookIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOpenFacebookIntent();
            }
        });

        mv_instagramIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://instagram.com/mulven.com.bd");
                Intent i= new Intent(Intent.ACTION_VIEW,uri);
                i.setPackage("com.instagram.android");
                try {
                    startActivity(i);
                } catch (ActivityNotFoundException e) {

                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/mulven.com.bd")));
                }
            }
        });mv_mapIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(Intent.ACTION_VIEW);
                intent1.setData(Uri.parse("geo:23.80681,90.4254013"));
                Intent c=Intent.createChooser(intent1,"Launch Maps");
                startActivity(c);
            }
        });
        mv_emailIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "support@mulven.com" });
                /*intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
                intent.putExtra(Intent.EXTRA_TEXT, "mail body");*/
                startActivity(Intent.createChooser(intent, ""));
            }
        });
        mv_phoneIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:"+"+880 9638 443344"));
                    startActivity(callIntent);

                } catch (ActivityNotFoundException activityException) {
                    Toasty.error(AboutActivity.this,""+activityException.getMessage(), Toasty.LENGTH_SHORT).show();
                }
            }
        });


    }
    public void getOpenFacebookIntent() {
        try {
            Intent fb=new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/mulven.com.bd"));
            startActivity(fb);
        } catch (ActivityNotFoundException e) {
            Intent fb=new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.facebook.com/mulven.com.bd/?ref=page_internal"));
            startActivity(fb);
        }
    }

    private void init() {
        aboutRelativeLayout=findViewById(R.id.aboutRelativeLayout);
        helpRelativeLayout=findViewById(R.id.helpRelativeLayout);
        toolbarTitleTV=findViewById(R.id.toolbarTitleTV);
        mv_facebookIV=findViewById(R.id.mv_facebookIV);
        mv_instagramIV=findViewById(R.id.mv_instagramIV);
        mv_emailIV=findViewById(R.id.mv_emailIV);
        mv_mapIV=findViewById(R.id.mv_mapIV);
        mv_phoneIV=findViewById(R.id.mv_phoneIV);
    }

    public void aboutBack(View view) {
        finish();
    }
}