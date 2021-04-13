package com.hydertechno.mulven.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.google.android.material.navigation.NavigationView;
import com.hydertechno.mulven.Api.ApiUtils;
import com.hydertechno.mulven.DatabaseHelper.Database_Helper;
import com.hydertechno.mulven.Fragments.AccountFragment;
import com.hydertechno.mulven.Fragments.CartFragment;
import com.hydertechno.mulven.Fragments.DreamDealFragment;
import com.hydertechno.mulven.Fragments.HomeFragment;
import com.hydertechno.mulven.Fragments.NotificationFragment;
import com.hydertechno.mulven.Fragments.ProfileFragment;
import com.hydertechno.mulven.Models.UserProfile;
import com.hydertechno.mulven.R;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import dev.shreyaspatil.MaterialDialog.AbstractDialog;
import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;
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
    private int loggedIn;
    private MaterialDialog mAnimatedDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        Intent getFragment=getIntent();
        String lodeFragment=getFragment.getStringExtra("fragment");
        if(lodeFragment.equals("home")){
            chipNavigationBar.setItemSelected(R.id.home,true);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();

        }else if(lodeFragment.equals("cart")){
            chipNavigationBar.setItemSelected(R.id.cart,true);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new CartFragment()).commit();
        }else if(lodeFragment.equals("profile")){
            chipNavigationBar.setItemSelected(R.id.account,true);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProfileFragment()).commit();
        }

        int count = helper.numberOfrows().getCount();
        if (count>0) {
            chipNavigationBar.showBadge(R.id.cart, count);
        }
        chipNavigationBar.showBadge(R.id.notification,1);
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
                        break;
                    case R.id.dream_deal:
                        fragment=new DreamDealFragment();
                        break;
                    case R.id.cart:
                        fragment=new CartFragment();
                        break;
                    case R.id.notification:
                        fragment=new NotificationFragment();
                        break;
                    case R.id.account:
                        if (loggedIn == 0 ){
                            fragment=new AccountFragment();
                            break;
                        }else{
                            fragment=new ProfileFragment();
                            break;
                        }
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

    }

    private void hideKeyboardFrom(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getWindow().getDecorView().getRootView().getWindowToken(), 0);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
                navigationView.getMenu().getItem(0).setChecked(true);
                navigationView.getMenu().getItem(1).setChecked(false);
                navigationView.getMenu().getItem(2).setChecked(false);
                navigationView.getMenu().getItem(3).setChecked(false);
                chipNavigationBar.setItemSelected(R.id.home,true);
                break;
            case R.id.gift:
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new CartFragment()).commit();

                startActivity(new Intent(getApplicationContext(),GiftVoucherActivity.class));
                break;
            case R.id.cart:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new CartFragment()).commit();
                navigationView.getMenu().getItem(0).setChecked(false);
                navigationView.getMenu().getItem(1).setChecked(false);
                navigationView.getMenu().getItem(3).setChecked(false);
                navigationView.getMenu().getItem(2).setChecked(true);
                chipNavigationBar.setItemSelected(R.id.cart,true);
                break;
            case R.id.login:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new AccountFragment()).commit();
                navigationView.getMenu().getItem(3).setChecked(true);
                navigationView.getMenu().getItem(0).setChecked(false);
                navigationView.getMenu().getItem(1).setChecked(false);
                navigationView.getMenu().getItem(2).setChecked(false);
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
                                            editor.commit();
                                            Toast.makeText(MainActivity.this, "Logout successful!", Toast.LENGTH_SHORT).show();

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
                        .setAnimation("logout.json")
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