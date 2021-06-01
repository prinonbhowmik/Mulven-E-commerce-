package com.hydertechno.mulven.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
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
    private String nonUserId;
    private MaterialDialog mAnimatedDialog;
    private FirebaseAuth auth;
    private DatabaseReference reference;
    private APIService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                updateToken(task.getResult());
            }
        });
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
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

    }

    private void updateToken(String token){
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("UserToken").child(""+userId);
        userRef.setValue(token);
        /*DatabaseReference nonUserRef = FirebaseDatabase.getInstance().getReference().child("NonUserToken");
        nonUserId=nonUserRef.push().getKey();*/

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
                intent.putExtra("layout","about");
                startActivity(intent);
                break;
            case R.id.help:

                Intent intent2 = new Intent(MainActivity.this, AboutActivity.class);
                intent2.putExtra("layout","help");
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
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("token", "");
                                            editor.putInt("loggedIn", 0);
                                            editor.remove("userId");
                                            editor.remove("userName");
                                            editor.remove("userPhone");
                                            editor.commit();
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
                        .setAnimation("log_out.json")
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