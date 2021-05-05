package com.hydertechno.mulven.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;

import com.hydertechno.mulven.Adapters.SearchAdapter;
import com.hydertechno.mulven.Api.ApiUtils;
import com.hydertechno.mulven.Models.CategoriesModel;
import com.hydertechno.mulven.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    private RecyclerView allProductRecycler;
    private List<CategoriesModel> list;
    private SearchAdapter adapter;
    private EditText searchView;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        init();

        getAllProducts();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (searchView.getText().toString().equals("")) {
                    getAllProducts();
                } else {
                    adapter.getFilter().filter(searchView.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private void getAllProducts() {
        Call<List<CategoriesModel>> call = ApiUtils.getUserService().searchProduct();
        call.enqueue(new Callback<List<CategoriesModel>>() {
            @Override
            public void onResponse(Call<List<CategoriesModel>> call, Response<List<CategoriesModel>> response) {
                if (response.isSuccessful()) {
                    list = response.body();
                    adapter = new SearchAdapter(list, SearchActivity.this);
                    allProductRecycler.setAdapter(adapter);
                }
                adapter.notifyDataSetChanged();
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
        searchView = findViewById(R.id.searchAllET);
        backBtn = findViewById(R.id.backBtn);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}