package com.hydertechno.mulven.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.hydertechno.mulven.Adapters.AllProductsAdapter;
import com.hydertechno.mulven.Adapters.ProductAdapter;
import com.hydertechno.mulven.Api.ApiInterface;
import com.hydertechno.mulven.Api.ApiUtils;
import com.hydertechno.mulven.Models.CategoriesModel;
import com.hydertechno.mulven.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SeeAllProductsActivity extends AppCompatActivity {
    private String title,id;
    private int categoryID;
    private TextView titleName;
    private SearchView searchView;
    private RecyclerView productRecyclerView;
    private AllProductsAdapter all_product_Adapter;
    private List<CategoriesModel> allProductsList=new ArrayList<>();
    private ApiInterface apiInterface;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all_products);
        init();
        Intent intent=getIntent();
        title=intent.getStringExtra("title");
        id=intent.getStringExtra("id");
        categoryID=Integer.parseInt(id);
        titleName.setText(title);
        getCategories();
       // titleName.setPaintFlags(titleName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        getSearchResult();

       /* for(int a=12; a>0;a--){
            if(a%2==0){
                allProductsList.add(new CategoriesModel("৳ 180","15 pcs/set Imitation Black Gem & Rhinestone Inlay Rings for Women",R.drawable.ring));
            }
            else{
                allProductsList.add(new CategoriesModel("৳ 250","white stone jewelry set for women",R.drawable.jewelry));
            }
        }*/

    }

    private void getSearchResult() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                if (!newText.equals("")){
                    all_product_Adapter.getFilter().filter(newText);
                }else{
                    getCategories();
                }
                return false;
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
        searchView = findViewById(R.id.searchET);
        SearchManager manager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        titleName=findViewById(R.id.titleName);
        productRecyclerView=findViewById(R.id.allProductRecyclerView);
        all_product_Adapter=new AllProductsAdapter(allProductsList,this);
        productRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        productRecyclerView.setItemAnimator(new DefaultItemAnimator());
        productRecyclerView.setAdapter(all_product_Adapter);
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
            }
        });
    }
    public void seeAllProductBack(View view) {
        finish();
    }
}