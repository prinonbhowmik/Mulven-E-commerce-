package com.hydertechno.mulven.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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
    private RecyclerView allproductRecycler;
    private List<CategoriesModel> list;
    private SearchAdapter adapter;
    private SearchView searchView;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        init();

        getAllProducts();

        getSearchResult();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getSearchResult() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                if (!newText.equals("")){
                    adapter.getFilter().filter(newText);
                }else{
                    getAllProducts();
                }
                return false;
            }
        });
    }

    private void getAllProducts() {
        Call<List<CategoriesModel>> call = ApiUtils.getUserService().searchProduct();
        call.enqueue(new Callback<List<CategoriesModel>>() {
            @Override
            public void onResponse(Call<List<CategoriesModel>> call, Response<List<CategoriesModel>> response) {
                if (response.isSuccessful()){
                    list = response.body();
                    adapter = new SearchAdapter(list,SearchActivity.this);
                    allproductRecycler.setAdapter(adapter);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<CategoriesModel>> call, Throwable t) {

            }
        });
    }

    private void init() {
        allproductRecycler = findViewById(R.id.allproductRecycler);
        allproductRecycler.setLayoutManager(new GridLayoutManager(this,2));
        allproductRecycler.setItemAnimator(new DefaultItemAnimator());
        searchView = findViewById(R.id.searchAllET);
        SearchManager manager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        backBtn = findViewById(R.id.backBtn);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}