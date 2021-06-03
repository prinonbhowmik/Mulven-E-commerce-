package com.hydertechno.mulven.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.hydertechno.mulven.Adapters.AllProductsAdapter;
import com.hydertechno.mulven.Adapters.ProductAdapter;
import com.hydertechno.mulven.Adapters.SubCategoryAdapter;
import com.hydertechno.mulven.Adapters.SubSubCategoryAdapter;
import com.hydertechno.mulven.Api.ApiInterface;
import com.hydertechno.mulven.Api.ApiUtils;
import com.hydertechno.mulven.Interface.OnQueryTextChangeListener;
import com.hydertechno.mulven.Interface.SubCatIdInterface;
import com.hydertechno.mulven.Interface.SubSubCatIdInterface;
import com.hydertechno.mulven.Internet.Connection;
import com.hydertechno.mulven.Internet.ConnectivityReceiver;
import com.hydertechno.mulven.Models.CategoriesModel;
import com.hydertechno.mulven.Models.SubCatModel;
import com.hydertechno.mulven.R;
import com.hydertechno.mulven.Utilities.SearchAnimation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SeeAllProductsActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener, SubCatIdInterface, SubSubCatIdInterface, OnQueryTextChangeListener {
    private String title,id;
    private int categoryID;
    public static boolean subSeeAll=true;
    public static TextView titleName;

    private RecyclerView productRecyclerView,subCatRecycler;
    public static RecyclerView subSubCatRecycler;
    private AllProductsAdapter all_product_Adapter;
    private SubCategoryAdapter adapter;
    private SubSubCategoryAdapter subSubAdapter;
    private ImageView searchBtn,closeIV;
    private List<CategoriesModel> allProductsList=new ArrayList<>();
    private ApiInterface apiInterface;
    public static RelativeLayout rootLayout;
    private Snackbar snackbar;
    public static int countAll=0;
    private boolean isConnected;
    private ConnectivityReceiver connectivityReceiver;
    private IntentFilter intentFilter;
    Toolbar toolbar, searchtollbar;
    SearchAnimation searchAnimation;
    View rootView;
    ArrayList<SubCatModel> updateSubCatModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_see_all_products, null);
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
       // titleName.setPaintFlags(titleName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

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
                   adapter = new SubCategoryAdapter(subCatModels,SeeAllProductsActivity.this);
                   subCatRecycler.setLayoutManager(new LinearLayoutManager(SeeAllProductsActivity.this, LinearLayoutManager.HORIZONTAL, false));
                   subCatRecycler.setAdapter(adapter);
               }
           }

           @Override
           public void onFailure(Call<List<SubCatModel>> call, Throwable t) {

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

        //searchView = findViewById(R.id.searchET);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextAppearance(SeeAllProductsActivity.this,R.style.Comfortaa_bold);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        searchAnimation = new SearchAnimation(
                this,
                rootView,
                this
        );
        searchAnimation.init();


        //searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        titleName=findViewById(R.id.titleName);
        productRecyclerView=findViewById(R.id.allProductRecyclerView);
        all_product_Adapter=new AllProductsAdapter(allProductsList,this);
        productRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        productRecyclerView.setItemAnimator(new DefaultItemAnimator());
        productRecyclerView.setAdapter(all_product_Adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        subCatRecycler = findViewById(R.id.subCatRecycler);
        subCatRecycler.setLayoutManager(layoutManager);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        subSubCatRecycler=findViewById(R.id.subSubCatRecycler);
        subSubCatRecycler.setLayoutManager(layoutManager2);
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

    @Override
    public void OnClick(int id) {
        if (id == -1) {
            checkConnection();
            if (!isConnected) {
                snackBar(isConnected);
            }else {
                //sAll.setBackground(ContextCompat.getDrawable(SeeAllProductsActivity.this, R.drawable.status_tag_all));
                getCategories();
                subSubCatRecycler.setVisibility(View.GONE);
            }
        } else {
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
                        getSubSubCategory(id);
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

    private void getSubSubCategory(int id) {
        Call<List<SubCatModel>> call = apiInterface.getSubCat(Integer.parseInt(String.valueOf(id)));
        call.enqueue(new Callback<List<SubCatModel>>() {
            @Override
            public void onResponse(Call<List<SubCatModel>> call, Response<List<SubCatModel>> response) {
                if (response.isSuccessful()){
                    List<SubCatModel> model = response.body();
                    subSubAdapter = new SubSubCategoryAdapter(model,SeeAllProductsActivity.this);
                    subSubCatRecycler.setAdapter(subSubAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<SubCatModel>> call, Throwable t) {

            }
        });
    }

    @Override
    public void OnSubClick(int id) {
        allProductsList.clear();
        Log.d("productId", String.valueOf(categoryID));
        Call<List<CategoriesModel>> call = apiInterface.getCategories(categoryID);
        call.enqueue(new Callback<List<CategoriesModel>>() {
            @Override
            public void onResponse(Call<List<CategoriesModel>> call, Response<List<CategoriesModel>> response) {
                if (response.isSuccessful()){
                    allProductsList = response.body();
                    all_product_Adapter = new AllProductsAdapter(allProductsList, getApplicationContext());
                    all_product_Adapter.getFilter2().filter(String.valueOf(id));
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
        if (!query.equals("")){

            all_product_Adapter.getFilter().filter(query);
            if(adapter!=null && updateSubCatModels!=null){
                adapter = new SubCategoryAdapter(updateSubCatModels,SeeAllProductsActivity.this);
                subCatRecycler.setLayoutManager(new LinearLayoutManager(SeeAllProductsActivity.this, LinearLayoutManager.HORIZONTAL, false));
                subCatRecycler.setAdapter(adapter);
            }
        }else{
            getCategories();
        }
    }
}