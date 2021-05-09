package com.hydertechno.mulven.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.hydertechno.mulven.Adapters.AllProductsAdapter;
import com.hydertechno.mulven.Adapters.ProductAdapter;
import com.hydertechno.mulven.Adapters.SubCategoryAdapter;
import com.hydertechno.mulven.Api.ApiInterface;
import com.hydertechno.mulven.Api.ApiUtils;
import com.hydertechno.mulven.Interface.SubCatIdInterface;
import com.hydertechno.mulven.Internet.Connection;
import com.hydertechno.mulven.Internet.ConnectivityReceiver;
import com.hydertechno.mulven.Models.CategoriesModel;
import com.hydertechno.mulven.Models.SubCatModel;
import com.hydertechno.mulven.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SeeAllProductsActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener, SubCatIdInterface {
    private String title,id;
    private int categoryID;
    private TextView titleName,sAll;
    private EditText searchView;
    private RecyclerView productRecyclerView,subCatRecycler;
    private AllProductsAdapter all_product_Adapter;
    private ImageView searchBtn,closeIV;
    private List<CategoriesModel> allProductsList=new ArrayList<>();
    private ApiInterface apiInterface;
    private RelativeLayout rootLayout;
    private SubCategoryAdapter adapter;
    private Snackbar snackbar;
    private boolean isConnected;
    private ConnectivityReceiver connectivityReceiver;
    private IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all_products);
        init();
        checkConnection();
        if (!isConnected) {
            snackBar(isConnected);
        }
        Intent intent=getIntent();
        title=intent.getStringExtra("title");
        id=intent.getStringExtra("id");
        categoryID=Integer.parseInt(id);
        titleName.setText(title);
        getCategories();

        getSubCat(id);
       // titleName.setPaintFlags(titleName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        getSearchResult();

        sAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCategories();
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setVisibility(View.VISIBLE);
                closeIV.setVisibility(View.VISIBLE);
                searchBtn.setVisibility(View.GONE);
            }
        });

        closeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setVisibility(View.GONE);
                closeIV.setVisibility(View.GONE);
                searchBtn.setVisibility(View.VISIBLE);
                hideKeyboardFrom(SeeAllProductsActivity.this);
            }
        });

       /* for(int a=12; a>0;a--){
            if(a%2==0){
                allProductsList.add(new CategoriesModel("৳ 180","15 pcs/set Imitation Black Gem & Rhinestone Inlay Rings for Women",R.drawable.ring));
            }
            else{
                allProductsList.add(new CategoriesModel("৳ 250","white stone jewelry set for women",R.drawable.jewelry));
            }
        }*/

    }

    private void getSubCat(String id) {
        Call<List<SubCatModel>> call = apiInterface.getSubCat(Integer.parseInt(id));
       call.enqueue(new Callback<List<SubCatModel>>() {
           @Override
           public void onResponse(Call<List<SubCatModel>> call, Response<List<SubCatModel>> response) {
               if (response.isSuccessful()){
                   List<SubCatModel> model = response.body();
                   adapter = new SubCategoryAdapter(model,SeeAllProductsActivity.this);
                   subCatRecycler.setAdapter(adapter);
               }
           }

           @Override
           public void onFailure(Call<List<SubCatModel>> call, Throwable t) {

           }
       });
    }

    private void getSearchResult() {
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!searchView.getText().toString().equals("")){
                    all_product_Adapter.getFilter().filter(searchView.getText().toString());
                }else{
                    getCategories();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void layoutAnimator(RecyclerView recyclerView){
        Context context=recyclerView.getContext();
        LayoutAnimationController layoutAnimationController= AnimationUtils.loadLayoutAnimation(context,R.anim.item_animation_fall_down);
        recyclerView.setLayoutAnimation(layoutAnimationController);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }
    private void init() {
        rootLayout = findViewById(R.id.see_all_product_rootLayout);
        intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        connectivityReceiver = new ConnectivityReceiver();

        searchView = findViewById(R.id.searchET);
        searchBtn = findViewById(R.id.SearchIV);
        closeIV = findViewById(R.id.closeIV);
        sAll = findViewById(R.id.sAll);

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        titleName=findViewById(R.id.titleName);
        productRecyclerView=findViewById(R.id.allProductRecyclerView);
        all_product_Adapter=new AllProductsAdapter(allProductsList,this);
        productRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        productRecyclerView.setItemAnimator(new DefaultItemAnimator());
        productRecyclerView.setAdapter(all_product_Adapter);

        subCatRecycler = findViewById(R.id.subCatRecycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        subCatRecycler.setLayoutManager(layoutManager);
        apiInterface= ApiUtils.getUserService();
    }


    private void getCategories() {
        allProductsList.clear();
        Call<List<CategoriesModel>> call = apiInterface.getCategories(categoryID);
        call.enqueue(new Callback<List<CategoriesModel>>() {
            @Override
            public void onResponse(Call<List<CategoriesModel>> call, Response<List<CategoriesModel>> response) {
                if (response.isSuccessful()){
                    allProductsList = response.body();
                    all_product_Adapter = new AllProductsAdapter(allProductsList, getApplicationContext());
                    productRecyclerView.setAdapter(all_product_Adapter);
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


    private void hideKeyboardFrom(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getWindow().getDecorView().getRootView().getWindowToken(), 0);
    }

    public void seeAllProductBack(View view) {
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

    @Override
    public void OnClick(int id) {
        allProductsList.clear();
        Log.d("productId", String.valueOf(categoryID));
        Call<List<CategoriesModel>> call = apiInterface.getCategories(categoryID);
        call.enqueue(new Callback<List<CategoriesModel>>() {
            @Override
            public void onResponse(Call<List<CategoriesModel>> call, Response<List<CategoriesModel>> response) {
                if (response.isSuccessful()){
                    allProductsList = response.body();
                    all_product_Adapter = new AllProductsAdapter(allProductsList, getApplicationContext());
                    productRecyclerView.setAdapter(all_product_Adapter);
                    all_product_Adapter.getFilter().filter(String.valueOf(id));
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
}