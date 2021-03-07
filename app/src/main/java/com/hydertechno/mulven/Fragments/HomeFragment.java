package com.hydertechno.mulven.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.hydertechno.mulven.Activities.SeeAllProductsActivity;
import com.hydertechno.mulven.Adapters.CategoryNamesAdapter;
import com.hydertechno.mulven.Adapters.HomePageSliderAdapter;
import com.hydertechno.mulven.Adapters.ProductAdapter;
import com.hydertechno.mulven.Api.ApiInterface;
import com.hydertechno.mulven.Api.ApiUtils;
import com.hydertechno.mulven.Models.CategoriesModel;
import com.hydertechno.mulven.Models.CategoryNamesModel;
import com.hydertechno.mulven.Models.Sliderimage;
import com.hydertechno.mulven.R;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private RecyclerView categoryRecycler,category_1,category_2,category_3;
    private HomePageSliderAdapter homePageSliderAdapter;
    private CategoryNamesAdapter categoryNamesAdapter;
    private ProductAdapter computer_and_accessory_Adapter;
    private ProductAdapter jewelry_and_watch_Adapter;
    private ProductAdapter grocery_Adapter;
    private DrawerLayout drawerLayout;
    private ImageView navIcon;
    private List<CategoryNamesModel> categoryNamesModelList =new ArrayList<>();
    private List<CategoriesModel> categoriesModelList=new ArrayList<>();
    private List<CategoriesModel> computer_and_accessory=new ArrayList<>();
    private List<CategoriesModel> jewelry_and_watch=new ArrayList<>();
    private List<CategoriesModel> grocery=new ArrayList<>();
    private SliderView imageSlider;
    private TextView seeAll1,seeAll2,seeAll3;
    private ApiInterface apiInterface;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_home, container, false);
        init(view);
        drawerLayout=getActivity().findViewById(R.id.drawerLayout);

        getSliderImage();
        getCategoriesName();

        navIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
                hideKeyboardFrom(view.getContext());
            }
        });

        seeAll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SeeAllProductsActivity.class).putExtra("title","Computer And Accessories"));
            }
        });
        seeAll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SeeAllProductsActivity.class).putExtra("title","Jewelry And Watches"));
            }
        });
        seeAll3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SeeAllProductsActivity.class).putExtra("title","Grocery"));
            }
        });

        /*categoriesModelList.add(new CategoriesModel("1","Food",R.drawable.ic_burger));
        categoriesModelList.add(new CategoriesModel("1","Kids and Toys",R.drawable.ic_baby));
        categoriesModelList.add(new CategoriesModel("1","Grocery Item",R.drawable.ic_shopping_basket));
        categoriesModelList.add(new CategoriesModel("1","Jewelry and Watches",R.drawable.ic_gem));
        categoriesModelList.add(new CategoriesModel("1","Sports",R.drawable.ic_volleybal));*/


        computer_and_accessory.add(new CategoriesModel("৳ 1000","LG 19M38A 18.5 Inch Monitor",R.drawable.monitor));
        computer_and_accessory.add(new CategoriesModel("৳ 2000","Transcend 8GB DDR4 2666MHz Bus SO-DIMM Laptop RAM",R.drawable.ram));
        computer_and_accessory.add(new CategoriesModel("৳ 1000","LG 19M38A 18.5 Inch Monitor",R.drawable.monitor));
        computer_and_accessory.add(new CategoriesModel("৳ 2000","Transcend 8GB DDR4 2666MHz Bus SO-DIMM Laptop RAM",R.drawable.ram));

        jewelry_and_watch.add(new CategoriesModel("৳ 180","15 pcs/set Imitation Black Gem & Rhinestone Inlay Rings for Women",R.drawable.ring));
        jewelry_and_watch.add(new CategoriesModel("৳ 250","white stone jewelry set for women",R.drawable.jewelry));
        jewelry_and_watch.add(new CategoriesModel("৳ 180","15 pcs/set Imitation Black Gem & Rhinestone Inlay Rings for Women",R.drawable.ring));
        jewelry_and_watch.add(new CategoriesModel("৳ 250","white stone jewelry set for women",R.drawable.jewelry));

        grocery.add(new CategoriesModel("৳ 350","Rupchanda Miniket Rice 5Kg",R.drawable.rice));
        grocery.add(new CategoriesModel("৳ 380","Danish Premium Ghee 900gm",R.drawable.ghee));
        grocery.add(new CategoriesModel("৳ 350","Rupchanda Miniket Rice 5Kg",R.drawable.rice));
        grocery.add(new CategoriesModel("৳ 380","Danish Premium Ghee 900gm",R.drawable.ghee));


        return view;
    }

    private void getSliderImage() {

        Call<List<Sliderimage>> call = apiInterface.getSliderImage();
        call.enqueue(new Callback<List<Sliderimage>>() {
            @Override
            public void onResponse(Call<List<Sliderimage>> call, Response<List<Sliderimage>> response) {
                List<Sliderimage> imageList = response.body();

                homePageSliderAdapter=new HomePageSliderAdapter(imageList);
                imageSlider.setSliderAdapter(homePageSliderAdapter);
                imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM);
                imageSlider.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
                imageSlider.setScrollTimeInSec(4);
                imageSlider.startAutoCycle();
            }

            @Override
            public void onFailure(Call<List<Sliderimage>> call, Throwable t) {

            }
        });
    }
    private void getCategoriesName() {
        categoryNamesModelList.clear();
        Call<List<CategoryNamesModel>> call = apiInterface.getProductsCategories();
        call.enqueue(new Callback<List<CategoryNamesModel>>() {
            @Override
            public void onResponse(Call<List<CategoryNamesModel>> call, Response<List<CategoryNamesModel>> response) {
                if (response.isSuccessful()){
                    categoryNamesModelList = response.body();
                    categoryNamesAdapter = new CategoryNamesAdapter(categoryNamesModelList, getContext());
                    categoryRecycler.setAdapter(categoryNamesAdapter);
                }
                Collections.reverse(categoryNamesModelList);
                categoryNamesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<CategoryNamesModel>> call, Throwable t) {
            }
        });
    }
    private void init(View view) {
        navIcon=view.findViewById(R.id.navIcon);
        categoryRecycler=view.findViewById(R.id.categoryRecyclerView);
        homePageSliderAdapter = new HomePageSliderAdapter(getContext());
        imageSlider=view.findViewById(R.id.imageSlider);
        category_1=view.findViewById(R.id.category_1Grid_View);
        category_2=view.findViewById(R.id.category_2Grid_View);
        category_3=view.findViewById(R.id.category_3Grid_View);
        seeAll1=view.findViewById(R.id.seeAll_1);
        seeAll2=view.findViewById(R.id.seeAll_2);
        seeAll3=view.findViewById(R.id.seeAll_3);

        categoryNamesAdapter =new CategoryNamesAdapter(categoryNamesModelList,getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        categoryRecycler.setLayoutManager(layoutManager);
        categoryRecycler.setAdapter(categoryNamesAdapter);
        Collections.reverse(categoriesModelList);

        computer_and_accessory_Adapter=new ProductAdapter(computer_and_accessory,getContext());
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        layoutManager1.setSmoothScrollbarEnabled(true);
        category_1.setLayoutManager(layoutManager1);
        category_1.setAdapter(computer_and_accessory_Adapter);

        jewelry_and_watch_Adapter=new ProductAdapter(jewelry_and_watch,getContext());
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        //category_2.setLayoutManager(new GridLayoutManager(getContext(),2));
        category_2.setLayoutManager(layoutManager2);
        category_2.setAdapter(jewelry_and_watch_Adapter);

        grocery_Adapter=new ProductAdapter(grocery,getContext());
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        //category_2.setLayoutManager(new GridLayoutManager(getContext(),2));
        category_3.setLayoutManager(layoutManager3);
        category_3.setAdapter(grocery_Adapter);
        apiInterface = ApiUtils.getUserService();
    }

    private void hideKeyboardFrom(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getActivity().getWindow().getDecorView().getRootView().getWindowToken(), 0);
    }
}