package com.hydertechno.mulven.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.hydertechno.mulven.Adapters.CampaignCategoryAdapter;
import com.hydertechno.mulven.Adapters.CampaignProductsAdapter;
import com.hydertechno.mulven.Api.ApiInterface;
import com.hydertechno.mulven.Api.ApiUtils;
import com.hydertechno.mulven.Interface.CampaignCatInterface;
import com.hydertechno.mulven.Interface.OnQueryTextChangeListener;
import com.hydertechno.mulven.Internet.Connection;
import com.hydertechno.mulven.Internet.ConnectivityReceiver;
import com.hydertechno.mulven.Models.CampaignCategoriesModel;
import com.hydertechno.mulven.Models.CampaignProductsModel;
import com.hydertechno.mulven.Models.CategoriesModel;
import com.hydertechno.mulven.R;
import com.hydertechno.mulven.Utilities.SearchAnimation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CampaignProductActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener, OnQueryTextChangeListener, CampaignCatInterface {
    private final String TAG = CampaignProductActivity.class.getSimpleName();

    private ImageView navIcon,searchIv,closeIv;
    private String title,id;
    private int campaignID;
    private TextView titleName;
    private RecyclerView campaignProductRecyclerView,campaignCatRecycler;
    private CampaignProductsAdapter campaignProductsAdapter;
    private CampaignCategoryAdapter campaignCategoryAdapter;
    private List<CategoriesModel> allItems=new ArrayList<>();
    private List<CampaignCategoriesModel> campaignCategory=new ArrayList<>();
    private List<CampaignProductsModel> allProductsList=new ArrayList<>();
    private ApiInterface apiInterface;
    public static RelativeLayout rootLayout;
    private Snackbar snackbar;
    private boolean isConnected;
    private ConnectivityReceiver connectivityReceiver;
    private IntentFilter intentFilter;
    Toolbar toolbar;
    SearchAnimation searchAnimation;
    View rootView;
    RelativeLayout progressRL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_campaign_product, null);
        setContentView(rootView);
        init();
        checkConnection();
        if (!isConnected) {
            snackBar(isConnected);
        }
        Intent intent=getIntent();
        campaignID=intent.getIntExtra("id",0);
        title=intent.getStringExtra("title");
        getSupportActionBar().setTitle(title);
        Log.e(TAG, "id=>>>" + campaignID);

        getCategories();

    }

    private void init() {
        progressRL = findViewById(R.id.progressRL);
        rootLayout = findViewById(R.id.campaign_product_rootLayout);
        intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        connectivityReceiver = new ConnectivityReceiver();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextAppearance(CampaignProductActivity.this,R.style.Comfortaa_bold);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        searchAnimation = new SearchAnimation(
                this,
                rootView,
                this
        );
        searchAnimation.init();
        campaignProductRecyclerView =findViewById(R.id.campaignProductRecyclerView);
        titleName = findViewById(R.id.titleName);
        campaignProductsAdapter=new CampaignProductsAdapter(allItems,CampaignProductActivity.this);
        campaignProductRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        campaignProductRecyclerView.setItemAnimator(new DefaultItemAnimator());
        campaignProductRecyclerView.setAdapter(campaignProductsAdapter);

        apiInterface= ApiUtils.getUserService();


        campaignCatRecycler = findViewById(R.id.camCatRecycler);
        campaignCategoryAdapter=new CampaignCategoryAdapter(campaignCategory, this, CampaignProductActivity.this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        campaignCatRecycler.setLayoutManager(new LinearLayoutManager(CampaignProductActivity.this, LinearLayoutManager.HORIZONTAL, false));
        campaignCatRecycler.setLayoutManager(layoutManager);
        campaignCatRecycler.setAdapter(campaignCategoryAdapter);
    }


    private void hideKeyboardFrom(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getWindow().getDecorView().getRootView().getWindowToken(), 0);
    }
    private void getCategories() {
//        campaignCategory.clear();
//        allItems.clear();
        progressRL.setVisibility(View.VISIBLE);
        Call<CampaignProductsModel> call = apiInterface.getCampaignItem(campaignID);
        call.enqueue(new Callback<CampaignProductsModel>() {
            @Override
            public void onResponse(Call<CampaignProductsModel> call, Response<CampaignProductsModel> response) {
                if (response.isSuccessful()){
                    Log.e(CampaignProductActivity.class.getSimpleName(), response.body().toString());
                    CampaignProductsModel list  = response.body();

                    CampaignCategoriesModel model = new CampaignCategoriesModel(-1, "All");
                    ArrayList<CampaignCategoriesModel> subCatModels = new ArrayList<CampaignCategoriesModel>();
                    subCatModels.add(model);
                    if (list != null && list.getCategory() != null) subCatModels.addAll(list.getCategory());
                    campaignCategory=subCatModels;
                    campaignCategoryAdapter.updateData(campaignCategory);

                    allItems=list.getAllitems();
                    campaignProductsAdapter.updateData(allItems);
                    if (allItems.size() == 0) {
                        campaignProductRecyclerView.setVisibility(View.GONE);
                    }
                    if (campaignCategory.size() == 0) {
                        campaignCatRecycler.setVisibility(View.GONE);
                    }
                }

                progressRL.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<CampaignProductsModel> call, Throwable t) {
                progressRL.setVisibility(View.GONE);
            }
        });
    }

    public void backCampaign(View view) {
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
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
            snackbar = Snackbar.make(rootLayout, "No Internet Connection!", Snackbar.LENGTH_INDEFINITE);
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
                campaignProductsAdapter.getFilter().filter(query);
            }else{
                getCategories();
            }
        }

    }

    @Override
    public void onClick(int id) {
        if (id == -1) {
            checkConnection();
            if (!isConnected) {
                snackBar(isConnected);
            }else {
                getCategories();
            }
        } else {
            campaignProductsAdapter.getFilter().filter(String.valueOf(id));
        }
//        allItems.clear();
//        Call<CampaignProductsModel> call = apiInterface.getCampaignItem(campaignID);
//        call.enqueue(new Callback<CampaignProductsModel>() {
//            @Override
//            public void onResponse(Call<CampaignProductsModel> call, Response<CampaignProductsModel> response) {
//                if (response.isSuccessful()){
//
//                    CampaignProductsModel list  = response.body();
//
//                    campaignCategory=list.getCategory();
//                    campaignCategoryAdapter.updateData(campaignCategory);
//                    allItems=list.getAllitems();
//                    campaignProductsAdapter.updateData(allItems);
//                    campaignProductsAdapter.getFilter().filter(String.valueOf(id));
////                    campaignProductRecyclerView.setAdapter(campaignProductsAdapter);
//                }
//                //Collections.reverse(categoryNamesModelList);
//                campaignProductsAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onFailure(Call<CampaignProductsModel> call, Throwable t) {
//                Log.d("ErrorKi",t.getMessage());
//            }
//        });
    }
}