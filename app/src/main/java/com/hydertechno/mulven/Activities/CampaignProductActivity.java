package com.hydertechno.mulven.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hydertechno.mulven.Adapters.AllProductsAdapter;
import com.hydertechno.mulven.Adapters.CampaignProductsAdapter;
import com.hydertechno.mulven.Api.ApiInterface;
import com.hydertechno.mulven.Api.ApiUtils;
import com.hydertechno.mulven.Models.CategoriesModel;
import com.hydertechno.mulven.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CampaignProductActivity extends AppCompatActivity {

    private ImageView navIcon,searchIv,closeIv;
    private String title,id;
    private int campaignID;
    private TextView titleName;
    private EditText searchView;
    private RecyclerView campaignProductRecyclerView;
    private CampaignProductsAdapter campaignProductsAdapter;
    private List<CategoriesModel> allProductsList=new ArrayList<>();
    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign_product);
        init();
        Intent intent=getIntent();
        campaignID=intent.getIntExtra("id",0);
        title=intent.getStringExtra("title");
        titleName.setText(title);

        searchIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setVisibility(View.VISIBLE);
                closeIv.setVisibility(View.VISIBLE);
                searchIv.setVisibility(View.GONE);
            }
        });

        closeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchIv.setVisibility(View.VISIBLE);
                closeIv.setVisibility(View.GONE);
                searchView.setVisibility(View.GONE);
                hideKeyboardFrom(CampaignProductActivity.this);
            }
        });

        getCategories();
        getSearchResult();
    }


    private void getSearchResult() {
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!searchView.getText().toString().equals("")){
                    campaignProductsAdapter.getFilter().filter(searchView.getText().toString());
                }else{
                    getCategories();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void init() {

        campaignProductRecyclerView =findViewById(R.id.campaignProductRecyclerView);
        searchView = findViewById(R.id.searchET);
        searchIv = findViewById(R.id.SearchIvs);
        closeIv = findViewById(R.id.closeIvs);
        titleName = findViewById(R.id.titleName);
        campaignProductsAdapter=new CampaignProductsAdapter(allProductsList,CampaignProductActivity.this);
        campaignProductRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        campaignProductRecyclerView.setItemAnimator(new DefaultItemAnimator());
        campaignProductRecyclerView.setAdapter(campaignProductsAdapter);
        apiInterface= ApiUtils.getUserService();
    }


    private void hideKeyboardFrom(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getWindow().getDecorView().getRootView().getWindowToken(), 0);
    }
    private void getCategories() {
        allProductsList.clear();
        Call<List<CategoriesModel>> call = apiInterface.getCampaignItem(campaignID);
        call.enqueue(new Callback<List<CategoriesModel>>() {
            @Override
            public void onResponse(Call<List<CategoriesModel>> call, Response<List<CategoriesModel>> response) {
                if (response.isSuccessful()){
                    allProductsList = response.body();
                    campaignProductsAdapter = new CampaignProductsAdapter(allProductsList, CampaignProductActivity.this);
                    campaignProductRecyclerView.setAdapter(campaignProductsAdapter);
                    if (allProductsList.size() == 0) {
                        campaignProductRecyclerView.setVisibility(View.GONE);
                    }
                }
                campaignProductsAdapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<List<CategoriesModel>> call, Throwable t) {
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
}