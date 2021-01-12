package com.hydertechno.mulven.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hydertechno.mulven.Adapters.CategoriesAdapter;
import com.hydertechno.mulven.Adapters.HomePageSliderAdapter;
import com.hydertechno.mulven.Models.CategoriesModel;
import com.hydertechno.mulven.R;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView categoryRecycler,category_1;
    private CategoriesAdapter categoriesAdapter;
    private List<CategoriesModel> categoriesModelList=new ArrayList<>();
    private SliderView imageSlider;
    int[] image={R.drawable.beefslider,R.drawable.seller,R.drawable.slider1,R.drawable.slider2,R.drawable.slider3};
    HomePageSliderAdapter homePageSliderAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_home, container, false);
        init(view);


        categoriesModelList.add(new CategoriesModel("1","Food",R.drawable.ic_burger));
        categoriesModelList.add(new CategoriesModel("1","Kids and Toy",R.drawable.ic_baby));
        categoriesModelList.add(new CategoriesModel("1","Grocery Item",R.drawable.ic_shopping_basket));
        categoriesModelList.add(new CategoriesModel("1","Jewelry and Watches",R.drawable.ic_gem));
        categoriesModelList.add(new CategoriesModel("1","Sports",R.drawable.ic_volleybal));

        imageSlider.setSliderAdapter(homePageSliderAdapter);
        imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM);
        imageSlider.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        imageSlider.startAutoCycle();
        return view;
    }

    private void init(View view) {
        categoryRecycler=view.findViewById(R.id.categoryRecyclerView);
        categoriesAdapter=new CategoriesAdapter(categoriesModelList,getContext());
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        categoryRecycler.setLayoutManager(layoutManager);
        categoryRecycler.setAdapter(categoriesAdapter);
        imageSlider=view.findViewById(R.id.imageSlider);
        homePageSliderAdapter=new HomePageSliderAdapter(image);
        Collections.reverse(categoriesModelList);
        category_1=view.findViewById(R.id.category_1Grid_View);
        category_1.setLayoutManager(new GridLayoutManager(getContext(),2));
        category_1.setAdapter(categoriesAdapter);
    }
}