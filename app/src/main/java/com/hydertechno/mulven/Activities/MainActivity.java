package com.hydertechno.mulven.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;
import com.hydertechno.mulven.Fragments.AccountFragment;
import com.hydertechno.mulven.Fragments.CartFragment;
import com.hydertechno.mulven.Fragments.HomeFragment;
import com.hydertechno.mulven.Fragments.NotifcationFragment;
import com.hydertechno.mulven.R;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ChipNavigationBar chipNavigationBar;
    private ImageView navIcon;
    private Fragment fragment=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        chipNavigationBar.setItemSelected(R.id.home,true);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i){
                    case R.id.home:
                        fragment=new HomeFragment();
                        break;
                    case R.id.favourite:
                        fragment=new AccountFragment();
                        break;case R.id.cart:
                        fragment=new CartFragment();
                        break;case R.id.notification:
                        fragment=new NotifcationFragment();
                        break;case R.id.account:
                        fragment=new AccountFragment();
                        break;
                }
                if(fragment!=null){

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
                }
            }
        });
        navIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
                hideKeyboardFrom(getApplicationContext());
            }
        });
    }

    private void init() {
        drawerLayout=findViewById(R.id.drawerLayout);
        navigationView=findViewById(R.id.nav_view);
        chipNavigationBar=findViewById(R.id.bottom_menu);
        navIcon=findViewById(R.id.navIcon);
    }

    private void hideKeyboardFrom(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getWindow().getDecorView().getRootView().getWindowToken(), 0);
    }
}