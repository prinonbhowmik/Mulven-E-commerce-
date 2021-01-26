package com.hydertechno.mulven.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.hydertechno.mulven.Adapters.CategoriesAdapter;
import com.hydertechno.mulven.Adapters.ProductAdapter;
import com.hydertechno.mulven.Adapters.ProductImagesAdapter;
import com.hydertechno.mulven.Models.CategoriesModel;
import com.hydertechno.mulven.Models.ProductImagesModel;
import com.hydertechno.mulven.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductDetailsActivity extends AppCompatActivity {
    private AutoCompleteTextView sizeTV,colorTV;
    private TextView productOldPrice;
    private RecyclerView productImagesRecycler,relatedProductRecyclerView;
    private ProductImagesAdapter productImagesAdapter;
    private ProductAdapter jewelry_and_watch_Adapter;
    private List<ProductImagesModel> productImagesModelList=new ArrayList<>();
    private List<CategoriesModel> jewelry_and_watch=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        init();
        final String[] size = new String[]{"12", "18", "24","32","40"};
        final String[] color = new String[]{"Red", "Green", "Blue","Black","White"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, R.layout.list_item, size);
        sizeTV.setText(adapter1.getItem(0).toString(),false);
        sizeTV.setAdapter(adapter1);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, R.layout.list_item, color);
        colorTV.setText(adapter2.getItem(0).toString(),false);
        colorTV.setAdapter(adapter2);

        productImagesModelList.add(new ProductImagesModel("1",R.drawable.monitor));
        productImagesModelList.add(new ProductImagesModel("2",R.drawable.ram));
        productImagesModelList.add(new ProductImagesModel("3",R.drawable.ghee));
        productImagesModelList.add(new ProductImagesModel("4",R.drawable.rice));

        jewelry_and_watch.add(new CategoriesModel("৳ 180","15 pcs/set Imitation Black Gem & Rhinestone Inlay Rings for Women",R.drawable.ring));
        jewelry_and_watch.add(new CategoriesModel("৳ 250","white stone jewelry set for women",R.drawable.jewelry));
        jewelry_and_watch.add(new CategoriesModel("৳ 180","15 pcs/set Imitation Black Gem & Rhinestone Inlay Rings for Women",R.drawable.ring));
        jewelry_and_watch.add(new CategoriesModel("৳ 250","white stone jewelry set for women",R.drawable.jewelry));

    }

    private void init() {
        sizeTV=findViewById(R.id.sizeMenu);
        colorTV=findViewById(R.id.colorMenu);
        productOldPrice=findViewById(R.id.product_Old_Price);
        productOldPrice.setPaintFlags(productOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        productImagesRecycler=findViewById(R.id.productImagesRecyclerView);
        relatedProductRecyclerView=findViewById(R.id.relatedProductRecyclerView);

        productImagesAdapter=new ProductImagesAdapter(productImagesModelList,this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        productImagesRecycler.setLayoutManager(layoutManager);
        productImagesRecycler.setAdapter(productImagesAdapter);

        jewelry_and_watch_Adapter=new ProductAdapter(jewelry_and_watch,this);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        //category_2.setLayoutManager(new GridLayoutManager(getContext(),2));
        relatedProductRecyclerView.setLayoutManager(layoutManager2);
        relatedProductRecyclerView.setAdapter(jewelry_and_watch_Adapter);

    }

    public void productDetailsBack(View view) {
        finish();
    }
}