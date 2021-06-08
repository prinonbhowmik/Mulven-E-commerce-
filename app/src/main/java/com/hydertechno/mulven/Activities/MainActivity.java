package com.hydertechno.mulven.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hydertechno.mulven.BuildConfig;
import com.hydertechno.mulven.Models.ResponseUpdate;
import com.hydertechno.mulven.Notification.APIService;
import com.hydertechno.mulven.Notification.Client;
import com.hydertechno.mulven.Notification.Data;
import com.hydertechno.mulven.Notification.MyResponse;
import com.hydertechno.mulven.Notification.Sender;
import com.hydertechno.mulven.Notification.Token;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hydertechno.mulven.Api.ApiUtils;
import com.hydertechno.mulven.DatabaseHelper.Database_Helper;
import com.hydertechno.mulven.Fragments.AccountFragment;
import com.hydertechno.mulven.Fragments.CartFragment;
import com.hydertechno.mulven.Fragments.CampaignFragment;
import com.hydertechno.mulven.Fragments.HomeFragment;
import com.hydertechno.mulven.Fragments.NotificationFragment;
import com.hydertechno.mulven.Fragments.ProfileFragment;
import com.hydertechno.mulven.Models.UserProfile;
import com.hydertechno.mulven.R;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    public static ChipNavigationBar chipNavigationBar;
    private Database_Helper helper;
    private boolean doubleBackToExitPressedOnce=false;
    private Fragment fragment=null;
    private SharedPreferences sharedPreferences;
    private int loggedIn,userId;
    private MaterialDialog mAnimatedDialog;
    private DatabaseReference reference;
    private Dialog logOutDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        try {
            FirebaseMessaging.getInstance().subscribeToTopic("all").addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()) {
                        Log.e("Firebase", task.toString());
                    }
                    Log.e("Firebase", "Success!!");
                }
            });

            FirebaseMessaging.getInstance().subscribeToTopic(userId + "").addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()) {
                        Log.e("Firebase", task.toString());
                    }
                    Log.e("Firebase", "Success!!");
                }
            });
        }catch (Exception ignored){

        }

        Intent getFragment = getIntent();
        String lodeFragment = getFragment.getStringExtra("fragment");
        if (lodeFragment.equals("home")) {
            chipNavigationBar.setItemSelected(R.id.home, true);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

            navigationView.getMenu().getItem(0).setChecked(true);
            navigationView.getMenu().getItem(1).setChecked(false);
            navigationView.getMenu().getItem(2).setChecked(false);
            navigationView.getMenu().getItem(3).setChecked(false);
            navigationView.getMenu().getItem(4).setChecked(false);
        } else if (lodeFragment.equals("cart")) {
            chipNavigationBar.setItemSelected(R.id.cart, true);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CartFragment()).commit();

            navigationView.getMenu().getItem(0).setChecked(false);
            navigationView.getMenu().getItem(1).setChecked(false);
            navigationView.getMenu().getItem(2).setChecked(true);
            navigationView.getMenu().getItem(3).setChecked(false);
            navigationView.getMenu().getItem(4).setChecked(false);
        } else if (lodeFragment.equals("profile")) {
            chipNavigationBar.setItemSelected(R.id.account, true);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
        } else if (lodeFragment.equals("login")) {
            chipNavigationBar.setItemSelected(R.id.account, true);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AccountFragment()).commit();
        }else if (lodeFragment.equals("notification")) {
            chipNavigationBar.setItemSelected(R.id.notification, true);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NotificationFragment()).commit();
        }else if (lodeFragment.equals("campaign")) {
            chipNavigationBar.setItemSelected(R.id.campaign, true);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CampaignFragment()).commit();
        }

        int count = helper.numberOfrows().getCount();
        if (count > 0) {
            chipNavigationBar.showBadge(R.id.cart, count);
        } else{
            chipNavigationBar.dismissBadge(R.id.cart);
        }
        if (loggedIn == 0){
            navigationView.getMenu().removeItem(R.id.logout);
        }else if(loggedIn == 1){
            navigationView.getMenu().removeItem(R.id.login);

        }

        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i){

                    case R.id.home:
                        fragment=new HomeFragment();
                        navigationView.getMenu().getItem(0).setChecked(true);
                        navigationView.getMenu().getItem(1).setChecked(false);
                        navigationView.getMenu().getItem(2).setChecked(false);
                        navigationView.getMenu().getItem(3).setChecked(false);
                        navigationView.getMenu().getItem(4).setChecked(false);
                        navigationView.getMenu().getItem(5).setChecked(false);
                        break;
                    case R.id.campaign:
                        fragment=new CampaignFragment();
                        navigationView.getMenu().getItem(0).setChecked(false);
                        navigationView.getMenu().getItem(1).setChecked(true);
                        navigationView.getMenu().getItem(2).setChecked(false);
                        navigationView.getMenu().getItem(3).setChecked(false);
                        navigationView.getMenu().getItem(4).setChecked(false);
                        navigationView.getMenu().getItem(5).setChecked(false);
                        break;
                    case R.id.cart:
                        fragment=new CartFragment();
                        navigationView.getMenu().getItem(0).setChecked(false);
                        navigationView.getMenu().getItem(1).setChecked(false);
                        navigationView.getMenu().getItem(2).setChecked(true);
                        navigationView.getMenu().getItem(3).setChecked(false);
                        navigationView.getMenu().getItem(4).setChecked(false);
                        navigationView.getMenu().getItem(5).setChecked(false);
                        break;
                    case R.id.notification:
                        fragment=new NotificationFragment();
                        navigationView.getMenu().getItem(0).setChecked(false);
                        navigationView.getMenu().getItem(1).setChecked(false);
                        navigationView.getMenu().getItem(2).setChecked(false);
                        navigationView.getMenu().getItem(3).setChecked(true);
                        navigationView.getMenu().getItem(4).setChecked(false);
                        navigationView.getMenu().getItem(5).setChecked(false);
                        break;
                    case R.id.account:
                        if (loggedIn == 0 ){
                            fragment=new AccountFragment();
                            navigationView.getMenu().getItem(0).setChecked(false);
                            navigationView.getMenu().getItem(1).setChecked(false);
                            navigationView.getMenu().getItem(2).setChecked(false);
                            navigationView.getMenu().getItem(3).setChecked(false);
                            navigationView.getMenu().getItem(4).setChecked(false);
                            navigationView.getMenu().getItem(5).setChecked(true);
                        }else{
                            fragment=new ProfileFragment();
                        }
                        break;
                }
                if(fragment!=null){

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
                }
            }
        });

        checkUpdate();
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
                        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
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
                }
            }

            @Override
            public void onFailure(Call<ResponseUpdate> call, Throwable t) {

            }
        });
    }

    private void init() {
        drawerLayout=findViewById(R.id.drawerLayout);
        navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        chipNavigationBar=findViewById(R.id.bottom_menu);
        helper = new Database_Helper(this);
        sharedPreferences = getSharedPreferences("MyRef", MODE_PRIVATE);
        loggedIn = sharedPreferences.getInt("loggedIn",0);
        userId = sharedPreferences.getInt("userId",0);
    }

    private void hideKeyboardFrom(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getWindow().getDecorView().getRootView().getWindowToken(), 0);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.homeMenu:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
                navigationView.getMenu().getItem(0).setChecked(true);
                navigationView.getMenu().getItem(1).setChecked(false);
                navigationView.getMenu().getItem(2).setChecked(false);
                navigationView.getMenu().getItem(3).setChecked(false);
                navigationView.getMenu().getItem(4).setChecked(false);
                chipNavigationBar.setItemSelected(R.id.home,true);
                break;
            case R.id.campaignMenu:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new CampaignFragment()).commit();
                navigationView.getMenu().getItem(0).setChecked(false);
                navigationView.getMenu().getItem(1).setChecked(true);
                navigationView.getMenu().getItem(2).setChecked(false);
                navigationView.getMenu().getItem(3).setChecked(false);
                navigationView.getMenu().getItem(4).setChecked(false);
                chipNavigationBar.setItemSelected(R.id.campaign,true);
                break;

            case R.id.cartMenu:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new CartFragment()).commit();
                navigationView.getMenu().getItem(0).setChecked(false);
                navigationView.getMenu().getItem(1).setChecked(false);
                navigationView.getMenu().getItem(2).setChecked(true);
                navigationView.getMenu().getItem(3).setChecked(false);
                navigationView.getMenu().getItem(4).setChecked(false);
                chipNavigationBar.setItemSelected(R.id.cart,true);
                break;
            case R.id.notificationMenu:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new NotificationFragment()).commit();
                navigationView.getMenu().getItem(0).setChecked(false);
                navigationView.getMenu().getItem(1).setChecked(false);
                navigationView.getMenu().getItem(2).setChecked(false);
                navigationView.getMenu().getItem(3).setChecked(true);
                navigationView.getMenu().getItem(4).setChecked(false);
                chipNavigationBar.setItemSelected(R.id.notification,true);
                break;

            case R.id.giftMenu:
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new CartFragment()).commit();
                startActivity(new Intent(getApplicationContext(),GiftVoucherActivity.class));
                break;

            case R.id.about:

                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                intent.putExtra("layout","About");
                startActivity(intent);
                break;
            case R.id.help:

                Intent intent2 = new Intent(MainActivity.this, AboutActivity.class);
                intent2.putExtra("layout","Contact");
                startActivity(intent2);
                break;

            case R.id.terms:
                Intent terms = new Intent(MainActivity.this, WebViewActivity.class);
                terms.putExtra("url","https://mulven.com/terms-conditions-for-app");
                startActivity(terms);
                break;

            case R.id.login:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new AccountFragment()).commit();
                navigationView.getMenu().getItem(3).setChecked(false);
                navigationView.getMenu().getItem(0).setChecked(false);
                navigationView.getMenu().getItem(1).setChecked(false);
                navigationView.getMenu().getItem(2).setChecked(false);
                navigationView.getMenu().getItem(5).setChecked(true);
                chipNavigationBar.setItemSelected(R.id.account,true);
                break;
            case R.id.logout:

                mAnimatedDialog = new MaterialDialog.Builder(this)
                        .setTitle("Logout?")
                        .setMessage("Are you sure want to logout?")
                        .setCancelable(false)
                        .setPositiveButton("Logout", R.drawable.ic_logout, new MaterialDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String token = sharedPreferences.getString("token",null);
                                Call<UserProfile> call = ApiUtils.getUserService().logoutUser(token);
                                call.enqueue(new Callback<UserProfile>() {
                                    @Override
                                    public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                                        if (response.isSuccessful()){
                                            FirebaseMessaging.getInstance().unsubscribeFromTopic(userId + "");
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("token", "");
                                            editor.putInt("loggedIn", 0);
                                            editor.putInt("isSubscribed", 0);
                                            editor.remove("userId");
                                            editor.remove("userName");
                                            editor.remove("userPhone");
                                            editor.apply();
                                            Toasty.success(MainActivity.this, "Logout successful!", Toasty.LENGTH_SHORT).show();
                                            finish();
                                            startActivity(getIntent());
                                            dialogInterface.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<UserProfile> call, Throwable t) {

                                    }
                                });
                            }
                        })
                        .setNegativeButton("Cancel", R.drawable.ic_close, new MaterialDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                dialogInterface.dismiss();
                            }
                        })
                        .build();
                mAnimatedDialog.show();

        }

        drawerLayout.closeDrawers();
        return false;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Press again for exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);

        }
    }
}