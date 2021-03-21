package com.hydertechno.mulven.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.hydertechno.mulven.Adapters.AllProductsAdapter;
import com.hydertechno.mulven.Api.ApiInterface;
import com.hydertechno.mulven.Api.ApiUtils;
import com.hydertechno.mulven.Models.CategoriesModel;
import com.hydertechno.mulven.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DreamDealFragment extends Fragment {
    private DrawerLayout drawerLayout;
    private ImageView navIcon;
    private String title,id;
    private int categoryID;
    private TextView titleName;
    private SearchView searchView;
    private RecyclerView dreamDealRecyclerView;
    private AllProductsAdapter all_product_Adapter;
    private List<CategoriesModel> allProductsList=new ArrayList<>();
    private ApiInterface apiInterface;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_dream_deal, container, false);
        init(view);
        drawerLayout=getActivity().findViewById(R.id.drawerLayout);
        navIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
                hideKeyboardFrom(view.getContext());
            }
        });
        getCategories();
        return view;
    }

    private void init(View view) {
        navIcon=view.findViewById(R.id.navIcon);
        dreamDealRecyclerView=view.findViewById(R.id.dream_dealRecyclerView);
        all_product_Adapter=new AllProductsAdapter(allProductsList,getContext());
        dreamDealRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        dreamDealRecyclerView.setItemAnimator(new DefaultItemAnimator());
        dreamDealRecyclerView.setAdapter(all_product_Adapter);
        apiInterface= ApiUtils.getUserService();
    }

    private void hideKeyboardFrom(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getActivity().getWindow().getDecorView().getRootView().getWindowToken(), 0);
    }
    private void getCategories() {
        allProductsList.clear();
        Call<List<CategoriesModel>> call = apiInterface.getCategories(95);
        call.enqueue(new Callback<List<CategoriesModel>>() {
            @Override
            public void onResponse(Call<List<CategoriesModel>> call, Response<List<CategoriesModel>> response) {
                if (response.isSuccessful()){
                    allProductsList = response.body();
                    all_product_Adapter = new AllProductsAdapter(allProductsList, getContext());
                    dreamDealRecyclerView.setAdapter(all_product_Adapter);
                    if (allProductsList.size() == 0) {
                        dreamDealRecyclerView.setVisibility(View.GONE);
                    }
                }
                all_product_Adapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<List<CategoriesModel>> call, Throwable t) {
            }
        });
    }
}