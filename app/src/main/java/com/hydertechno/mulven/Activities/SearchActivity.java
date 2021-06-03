package com.hydertechno.mulven.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hydertechno.mulven.Adapters.SearchAdapter;
import com.hydertechno.mulven.Api.ApiUtils;
import com.hydertechno.mulven.Interface.OnQueryTextChangeListener;
import com.hydertechno.mulven.Models.CategoriesModel;
import com.hydertechno.mulven.R;
import com.hydertechno.mulven.Utilities.SearchAnimation;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements OnQueryTextChangeListener {
    private RecyclerView allProductRecycler;
    private List<CategoriesModel> list;
    private SearchAdapter allSearchAdapter;
    private ImageView backBtn;
    Toolbar toolbar;
    SearchAnimation searchAnimation;
    View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_search, null);
        setContentView(rootView);

        init();
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
        if (!query.equals("")){
            allSearchAdapter.getFilter().filter(query);
        }else{
            getAllProducts();
        }
    }
}