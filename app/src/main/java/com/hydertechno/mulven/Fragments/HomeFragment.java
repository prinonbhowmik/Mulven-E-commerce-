package com.hydertechno.mulven.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.hydertechno.mulven.Activities.SeeAllProductsActivity;
import com.hydertechno.mulven.Adapters.CategoryNamesAdapter;
import com.hydertechno.mulven.Adapters.FeatureAddAdapter;
import com.hydertechno.mulven.Adapters.HomePageSliderAdapter;
import com.hydertechno.mulven.Adapters.ProductAdapter;
import com.hydertechno.mulven.Adapters.VerticalProductsAdapter;
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
    private RecyclerView categoryRecycler,feature_Add_Recycler_View,category_1,category_2,category_3,category_4;
    private HomePageSliderAdapter homePageSliderAdapter;
    private CategoryNamesAdapter categoryNamesAdapter;
    private ProductAdapter category1_Adapter;
    private ProductAdapter category2_Adapter;
    private ProductAdapter category3_Adapter;
    private VerticalProductsAdapter verticalProductsAdapter;
    private FeatureAddAdapter feature_Add_Adapter;
    private DrawerLayout drawerLayout;
    private ImageView navIcon;
    private List<CategoryNamesModel> categoryNamesModelList =new ArrayList<>();
    private List<Sliderimage> featureAddList =new ArrayList<>();
    private List<CategoriesModel> category1ModelList =new ArrayList<>();
    private List<CategoriesModel> category2ModelList =new ArrayList<>();
    private List<CategoriesModel> category3ModelList =new ArrayList<>();
    private List<CategoriesModel> category4ModelList =new ArrayList<>();
    private SliderView imageSlider;
    private TextView seeAll1,seeAll2,seeAll3,seeAll4;
    private ApiInterface apiInterface;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_home, container, false);
        init(view);
        drawerLayout=getActivity().findViewById(R.id.drawerLayout);

        getSliderImage();
        getCategoriesName();
        getFeatureAdds();
        getCategories1();
        getCategories2();
        getCategories3();
        getCategories4();

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
                startActivity(new Intent(getContext(), SeeAllProductsActivity.class).putExtra("title","Cellphones & Camera")
                        .putExtra("id","9"));
            }
        });
        seeAll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SeeAllProductsActivity.class).putExtra("title","Car & Bike")
                        .putExtra("id","4"));
            }
        });
        seeAll3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SeeAllProductsActivity.class).putExtra("title","Electronics")
                        .putExtra("id","10"));            }
        });
        seeAll4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SeeAllProductsActivity.class).putExtra("title","Grocery")
                        .putExtra("id","1"));            }
        });

        return view;
    }

    private void getSliderImage() {

        Call<List<Sliderimage>> call = apiInterface.getSliderImage("slider");
        call.enqueue(new Callback<List<Sliderimage>>() {
            @Override
            public void onResponse(Call<List<Sliderimage>> call, Response<List<Sliderimage>> response) {
                List<Sliderimage> imageList = response.body();
                String post_Type = imageList.get(0).getPost_type();
                if (post_Type.equals("slider")){
                    homePageSliderAdapter=new HomePageSliderAdapter(imageList);
                    imageSlider.setSliderAdapter(homePageSliderAdapter);
                    imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM);
                    imageSlider.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
                    imageSlider.setScrollTimeInSec(4);
                    imageSlider.startAutoCycle();
                }
            }

            @Override
            public void onFailure(Call<List<Sliderimage>> call, Throwable t) {
                Log.d("ErrorKi",t.getMessage());
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
                //Collections.reverse(categoryNamesModelList);
                categoryNamesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<CategoryNamesModel>> call, Throwable t) {
                Log.d("ErrorKi",t.getMessage());
            }
        });
    }
    private void getCategories1() {
        category1ModelList.clear();
        Call<List<CategoriesModel>> call = apiInterface.getCategories(9);
        call.enqueue(new Callback<List<CategoriesModel>>() {
            @Override
            public void onResponse(Call<List<CategoriesModel>> call, Response<List<CategoriesModel>> response) {
                if (response.isSuccessful()){
                    category1ModelList = response.body();
                    category1_Adapter = new ProductAdapter(category1ModelList, getContext());
                    category_1.setAdapter(category1_Adapter);
                }
                //Collections.reverse(categoryNamesModelList);
                category1_Adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<CategoriesModel>> call, Throwable t) {
                Log.d("ErrorKi",t.getMessage());
            }
        });
    }
    private void getCategories2() {
        category2ModelList.clear();
        Call<List<CategoriesModel>> call = apiInterface.getCategories(4);
        call.enqueue(new Callback<List<CategoriesModel>>() {
            @Override
            public void onResponse(Call<List<CategoriesModel>> call, Response<List<CategoriesModel>> response) {
                if (response.isSuccessful()){
                    category2ModelList = response.body();
                    category2_Adapter = new ProductAdapter(category2ModelList, getContext());
                    category_2.setAdapter(category2_Adapter);
                }
                //Collections.reverse(categoryNamesModelList);
                category2_Adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<CategoriesModel>> call, Throwable t) {
                Log.d("ErrorKi",t.getMessage());
            }
        });
    }
    private void getCategories3() {
        category3ModelList.clear();
        Call<List<CategoriesModel>> call = apiInterface.getCategories(10);
        call.enqueue(new Callback<List<CategoriesModel>>() {
            @Override
            public void onResponse(Call<List<CategoriesModel>> call, Response<List<CategoriesModel>> response) {
                if (response.isSuccessful()){
                    category3ModelList = response.body();
                    category3_Adapter = new ProductAdapter(category3ModelList, getContext());
                    category_3.setAdapter(category3_Adapter);
                }
                //Collections.reverse(categoryNamesModelList);
                category3_Adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<CategoriesModel>> call, Throwable t) {
                Log.d("ErrorKi",t.getMessage());
            }
        });
    }
    private void getCategories4() {
        category4ModelList.clear();
        Call<List<CategoriesModel>> call = apiInterface.getCategories(1);
        call.enqueue(new Callback<List<CategoriesModel>>() {
            @Override
            public void onResponse(Call<List<CategoriesModel>> call, Response<List<CategoriesModel>> response) {
                if (response.isSuccessful()){
                    category4ModelList = response.body();
                    verticalProductsAdapter = new VerticalProductsAdapter(category4ModelList, getContext());
                    category_4.setAdapter(verticalProductsAdapter);
                }
                //Collections.reverse(categoryNamesModelList);
                verticalProductsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<CategoriesModel>> call, Throwable t) {
                Log.d("ErrorKi",t.getMessage());
            }
        });
    }
    private void getFeatureAdds(){
        Call<List<Sliderimage>> call = apiInterface.getSliderImage("feature_add");
        call.enqueue(new Callback<List<Sliderimage>>() {
            @Override
            public void onResponse(Call<List<Sliderimage>> call, Response<List<Sliderimage>> response) {
                if(response.isSuccessful()){


                 featureAddList = response.body();

                feature_Add_Adapter=new FeatureAddAdapter(featureAddList,getContext());
                feature_Add_Recycler_View.setAdapter(feature_Add_Adapter);
            }
                feature_Add_Adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Sliderimage>> call, Throwable t) {
                Log.d("ErrorKi",t.getMessage());
            }
        });
    }
    private void init(View view) {
        navIcon=view.findViewById(R.id.navIcon);
        categoryRecycler=view.findViewById(R.id.categoryRecyclerView);
        homePageSliderAdapter = new HomePageSliderAdapter(getContext());
        imageSlider=view.findViewById(R.id.imageSlider);
        feature_Add_Recycler_View=view.findViewById(R.id.feature_Add_Recycler_View);
        category_1=view.findViewById(R.id.category_1Recycler_View);
        category_2=view.findViewById(R.id.category_2Recycler_View);
        category_3=view.findViewById(R.id.category_3Recycler_View);
        category_4=view.findViewById(R.id.category_4Recycler_View);

        seeAll1=view.findViewById(R.id.seeAll_1);
        seeAll2=view.findViewById(R.id.seeAll_2);
        seeAll3=view.findViewById(R.id.seeAll_3);
        seeAll4=view.findViewById(R.id.seeAll_4);

        categoryNamesAdapter =new CategoryNamesAdapter(categoryNamesModelList,getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        categoryRecycler.setLayoutManager(layoutManager);
        categoryRecycler.setAdapter(categoryNamesAdapter);

        category1_Adapter =new ProductAdapter(category1ModelList,getContext());
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        layoutManager1.setSmoothScrollbarEnabled(true);
        category_1.setLayoutManager(layoutManager1);
        category_1.setAdapter(category1_Adapter);

        category2_Adapter =new ProductAdapter(category2ModelList,getContext());
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        //category_2.setLayoutManager(new GridLayoutManager(getContext(),2));
        category_2.setLayoutManager(layoutManager2);
        category_2.setAdapter(category2_Adapter);

        category3_Adapter =new ProductAdapter(category3ModelList,getContext());
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        //category_2.setLayoutManager(new GridLayoutManager(getContext(),2));
        category_3.setLayoutManager(layoutManager3);
        category_3.setAdapter(category3_Adapter);
        apiInterface = ApiUtils.getUserService();

        verticalProductsAdapter =new VerticalProductsAdapter(category4ModelList,getContext());
        //LinearLayoutManager layoutManager4 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        category_4.setLayoutManager(new GridLayoutManager(getContext(),2));
        category_4.setAdapter(verticalProductsAdapter);

        apiInterface = ApiUtils.getUserService();

        feature_Add_Adapter =new FeatureAddAdapter(featureAddList,getContext());
        LinearLayoutManager layoutManager5 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        feature_Add_Recycler_View.setLayoutManager(layoutManager5);
        feature_Add_Recycler_View.setAdapter(feature_Add_Adapter);
    }

    private void hideKeyboardFrom(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getActivity().getWindow().getDecorView().getRootView().getWindowToken(), 0);
    }
}