package com.hydertechno.mulven.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.hydertechno.mulven.Fragments.AccountFragment;
import com.hydertechno.mulven.Fragments.CartFragment;
import com.hydertechno.mulven.Fragments.DreamDealFragment;
import com.hydertechno.mulven.Fragments.HomeFragment;
import com.hydertechno.mulven.Fragments.NotificationFragment;
import com.hydertechno.mulven.R;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ChipNavigationBar chipNavigationBar;
    private boolean doubleBackToExitPressedOnce=false;
    private Fragment fragment=null;

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
        }



        chipNavigationBar.showBadge(R.id.cart,2);
        chipNavigationBar.showBadge(R.id.notification,1);
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
                        fragment=new AccountFragment();
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
                navigationView.getMenu().getItem(2).setChecked(false);
                chipNavigationBar.setItemSelected(R.id.home,true);
                break;
            case R.id.gift:
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new CartFragment()).commit();

                startActivity(new Intent(getApplicationContext(),GiftVoucherActivity.class));
                break;
            case R.id.cart:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new CartFragment()).commit();
                navigationView.getMenu().getItem(0).setChecked(false);
                navigationView.getMenu().getItem(2).setChecked(true);
                chipNavigationBar.setItemSelected(R.id.cart,true);
                break;
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