package com.hydertechno.mulven.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.hydertechno.mulven.Fragments.LoadingDialog;
import com.hydertechno.mulven.R;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class RewardsActivity extends BaseActivity {
    private static final String TAG = RewardsActivity.class.getSimpleName();

    public LinearLayout rootLayout;
    private Toolbar toolbar;
    private Snackbar snackbar;

    private LoadingDialog loadingDialog;
    private SharedPreferences sharedPreferences;
    private String token;
    private int userId;

    TextView inviteLinkTV;
    Button buttonSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards);
        loadingDialog = LoadingDialog.instance();
        init();

        checkConnection();
        if (!isConnected) {
            snackBar(isConnected);
        }

        generateContentLink();
    }

    public void init(){
        loadingDialog = LoadingDialog.instance();
        rootLayout = findViewById(R.id.rootLayout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        inviteLinkTV = findViewById(R.id.inviteLinkTV);
        buttonSend = findViewById(R.id.buttonSend);

        sharedPreferences = getSharedPreferences("MyRef", MODE_PRIVATE);
        token = sharedPreferences.getString("token", null);
        userId = sharedPreferences.getInt("userId",0);
    }

    public void generateContentLink() {
        Uri baseUrl = Uri.parse("https://invites.mulven.com/?uId=" + userId);
        String domain = "https://mulven.page.link";

        Task<ShortDynamicLink> link = FirebaseDynamicLinks.getInstance()
                .createDynamicLink()
                .setLink(baseUrl)
                .setDomainUriPrefix(domain)
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder(RewardsActivity.this.getPackageName()).build())
                .buildShortDynamicLink()
                .addOnCompleteListener(new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            Uri link = task.getResult().getShortLink();
                            inviteLinkTV.setText(link.toString());

                            if (inviteLinkTV.getText().toString().isEmpty()) {
                                Toasty.error(RewardsActivity.this, "Links not generated, Please try again later!", Toasty.LENGTH_LONG).show();
                            } else {
                                buttonSend.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(Intent.ACTION_SEND);
                                        intent.setType("text/plain");
                                        intent.putExtra(Intent.EXTRA_TEXT, link.toString());

                                        startActivity(Intent.createChooser(intent, "Share Link"));
                                    }
                                });
                            }
                        }
                    }
                });
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent=new Intent( RewardsActivity.this,MainActivity.class);
                intent.putExtra("fragment","profile");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent( RewardsActivity.this,MainActivity.class);
        intent.putExtra("fragment","profile");
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }
}