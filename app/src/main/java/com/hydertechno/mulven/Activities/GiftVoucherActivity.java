package com.hydertechno.mulven.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.hydertechno.mulven.Adapters.AllProductsAdapter;
import com.hydertechno.mulven.Adapters.SubCategoryAdapter;
import com.hydertechno.mulven.Api.ApiInterface;
import com.hydertechno.mulven.Api.ApiUtils;
import com.hydertechno.mulven.Interface.OnQueryTextChangeListener;
import com.hydertechno.mulven.Interface.SubCatIdInterface;
import com.hydertechno.mulven.Internet.Connection;
import com.hydertechno.mulven.Internet.ConnectivityReceiver;
import com.hydertechno.mulven.Models.CategoriesModel;
import com.hydertechno.mulven.Models.SubCatModel;
import com.hydertechno.mulven.R;
import com.hydertechno.mulven.Utilities.SearchAnimation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GiftVoucherActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener, SubCatIdInterface, OnQueryTextChangeListener {

    private String title,id;
    private int categoryID;
    public static boolean subSeeAll=true;
    public static TextView titleName;

    private RecyclerView voucherRecyclerView,voucherSubCategoryRecyclerView;
    private AllProductsAdapter all_product_Adapter;
    private SubCategoryAdapter adapter;
    private List<CategoriesModel> allProductsList=new ArrayList<>();
    private ApiInterface apiInterface;
    ArrayList<SubCatModel> updateSubCatModels;

    private RelativeLayout rootLayout;
    private Snackbar snackbar;
    private boolean isConnected;
    private ConnectivityReceiver connectivityReceiver;
    private IntentFilter intentFilter;
    Toolbar toolbar;
    SearchAnimation searchAnimation;
    View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_gift_voucher, null);
        setContentView(rootView);
        init();
        checkConnection();
        if (!isConnected) {
            snackBar(isConnected);
        }

        Intent intent=getIntent();
        title=intent.getStringExtra("title");
        id=intent.getStringExtra("id");
        categoryID=Integer.parseInt(id);
        getSupportActionBar().setTitle(title);
        getCategories();

        getSubCat(id);

    }

    private void getCategories() {
//        allProductsList.clear();
        Call<List<CategoriesModel>> call = apiInterface.getCategories(categoryID);
        call.enqueue(new Callback<List<CategoriesModel>>() {
            @Override
            public void onResponse(Call<List<CategoriesModel>> call, Response<List<CategoriesModel>> response) {
                if (response.isSuccessful()){
                    allProductsList = response.body();
                    all_product_Adapter = new AllProductsAdapter(allProductsList, getApplicationContext());
                    voucherRecyclerView.setAdapter(all_product_Adapter);
                }
                //Collections.reverse(categoryNamesModelList);
                all_product_Adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<CategoriesModel>> call, Throwable t) {
                Log.d("ErrorKi",t.getMessage());
            }
        });
    }


    private void getSubCat(String id) {
        Call<List<SubCatModel>> call = apiInterface.getSubCat(Integer.parseInt(id));
        call.enqueue(new Callback<List<SubCatModel>>() {
            @Override
            public void onResponse(Call<List<SubCatModel>> call, Response<List<SubCatModel>> response) {
                if (response.isSuccessful()){
                    List<SubCatModel> model = response.body();
                    SubCatModel seeAll = new SubCatModel(-1, "All", null);
                    ArrayList<SubCatModel> subCatModels = new ArrayList<SubCatModel>();
                    subCatModels.add(seeAll);
                    if (model != null) subCatModels.addAll(model);
                    updateSubCatModels=subCatModels;
                    adapter = new SubCategoryAdapter(subCatModels,GiftVoucherActivity.this);
                    voucherSubCategoryRecyclerView.setLayoutManager(new LinearLayoutManager(GiftVoucherActivity.this, LinearLayoutManager.HORIZONTAL, false));
                    voucherSubCategoryRecyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<SubCatModel>> call, Throwable t) {

            }
        });
    }

    private void init() {
        rootLayout = findViewById(R.id.gift_voucher_rootLayout);
        intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        connectivityReceiver = new ConnectivityReceiver();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextAppearance(GiftVoucherActivity.this,R.style.Comfortaa_bold);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        searchAnimation = new SearchAnimation(
                this,
                rootView,
                this
        );
        searchAnimation.init();


        titleName=findViewById(R.id.titleName);
        voucherRecyclerView=findViewById(R.id.voucherRecyclerView);
        all_product_Adapter=new AllProductsAdapter(allProductsList,this);
        voucherRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        voucherRecyclerView.setItemAnimator(new DefaultItemAnimator());
        voucherRecyclerView.setAdapter(all_product_Adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        voucherSubCategoryRecyclerView = findViewById(R.id.voucherSubCategoryRecyclerView);
        voucherSubCategoryRecyclerView.setLayoutManager(layoutManager);

        apiInterface= ApiUtils.getUserService();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.product_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_search:
                searchAnimation.circleReveal(R.id.searchtoolbar,1,true,true);
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
        }else {
            if (!query.equals("")) {

                all_product_Adapter.getFilter().filter(query);
                if (adapter != null && updateSubCatModels != null) {
                    adapter = new SubCategoryAdapter(updateSubCatModels, GiftVoucherActivity.this);
                    voucherSubCategoryRecyclerView.setLayoutManager(new LinearLayoutManager(GiftVoucherActivity.this, LinearLayoutManager.HORIZONTAL, false));
                    voucherSubCategoryRecyclerView.setAdapter(adapter);
                }
            } else {
                getCategories();
            }
        }
    }




    @Override
    public void OnClick(int id) {

        if (id == -1) {
            //sAll.setBackground(ContextCompat.getDrawable(SeeAllProductsActivity.this, R.drawable.status_tag_all));

            checkConnection();
            if (!isConnected) {
                snackBar(isConnected);
            }else {
                getCategories();
            }

        } else {
            Log.d("productId", String.valueOf(categoryID));
            all_product_Adapter.getFilter().filter(String.valueOf(id));
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