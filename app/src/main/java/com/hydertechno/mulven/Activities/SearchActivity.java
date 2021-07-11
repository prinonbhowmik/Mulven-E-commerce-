package com.hydertechno.mulven.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.material.snackbar.Snackbar;
import com.hydertechno.mulven.Adapters.SearchAdapter;
import com.hydertechno.mulven.Api.ApiUtils;
import com.hydertechno.mulven.Interface.OnQueryTextChangeListener;
import com.hydertechno.mulven.Internet.Connection;
import com.hydertechno.mulven.Internet.ConnectivityReceiver;
import com.hydertechno.mulven.Models.CategoriesModel;
import com.hydertechno.mulven.R;
import com.hydertechno.mulven.Utilities.SearchAnimation;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements OnQueryTextChangeListener, ConnectivityReceiver.ConnectivityReceiverListener {
    private RecyclerView allProductRecycler;
    private List<CategoriesModel> list;
    private SearchAdapter allSearchAdapter;
    private ImageView backBtn;
    Toolbar toolbar;
    SearchAnimation searchAnimation;
    View rootView;
    private RelativeLayout rootLayout;
    private Snackbar snackbar;
    private boolean isConnected;
    private ConnectivityReceiver connectivityReceiver;
    private IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_search, null);
        setContentView(rootView);

        init();
        checkConnection();
        if (!isConnected) {
            snackBar(isConnected);
        }
        getAllProducts();


    }


    private void getAllProducts() {
        Call<List<CategoriesModel>> call = ApiUtils.getUserService().searchProduct();
        call.enqueue(new Callback<List<CategoriesModel>>() {
            @Override
            public void onResponse(Call<List<CategoriesModel>> call, Response<List<CategoriesModel>> response) {
                if (response.isSuccessful()) {
                    list = response.body();
                    allSearchAdapter = new SearchAdapter(list, SearchActivity.this);
                    allProductRecycler.setAdapter(allSearchAdapter);
                }
                allSearchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<CategoriesModel>> call, Throwable t) {

            }
        });
    }

    private void init() {
        allProductRecycler = findViewById(R.id.allproductRecycler);
        allProductRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        allProductRecycler.setItemAnimator(new DefaultItemAnimator());
        rootLayout = findViewById(R.id.search_rootLayout);
        intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        connectivityReceiver = new ConnectivityReceiver();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        searchAnimation = new SearchAnimation(
                this,
                rootView,
                this
        );
        searchAnimation.init();

        new Handler(Looper.myLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                searchAnimation.circleReveal(R.id.searchtoolbar,1,true,true);
                searchAnimation.item_search.expandActionView();
            }
        }, 500);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    // ----------------- this will be copied
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.product_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_search:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    searchAnimation.circleReveal(R.id.searchtoolbar,1,true,true);
                else
                    searchAnimation.searchToolbar.setVisibility(View.VISIBLE);

                searchAnimation.item_search.expandActionView();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onChange(String query) {
        //Do searching
        checkConnection();
        if (!isConnected) {
            snackBar(isConnected);
        }else{
            if (!query.equals("")){
                allSearchAdapter.getFilter().filter(query);
            }else{
                getAllProducts();
            }
        }

    }



    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        snackBar(isConnected);
    }

    private void checkConnection() {
        isConnected = ConnectivityReceiver.isConnected();
    }
    private void snackBar(boolean isConnected) {
        if(!isConnected) {
            snackbar = Snackbar.make(rootLayout, "No Internet Connection!", Snackbar.LENGTH_INDEFINITE).setAction("ReTry", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recreate();
                }
            });
            snackbar.setDuration(5000);
            snackbar.setActionTextColor(Color.WHITE);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(Color.RED);
            snackbar.show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(connectivityReceiver, intentFilter);
    }
    @Override
    protected void onResume() {

        // register connection status listener
        Connection.getInstance().setConnectivityListener(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try{
            if(connectivityReceiver!=null)
                unregisterReceiver(connectivityReceiver);

        }catch(Exception e){}

    }

    @Override
    protected void onStop() {
        try{
            if(connectivityReceiver!=null)
                unregisterReceiver(connectivityReceiver);

        }catch(Exception e){}

        super.onStop();
    }
}