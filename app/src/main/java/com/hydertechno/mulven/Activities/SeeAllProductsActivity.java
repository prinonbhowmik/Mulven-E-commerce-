package com.hydertechno.mulven.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hydertechno.mulven.Adapters.AllProductsAdapter;
import com.hydertechno.mulven.Models.CategoriesModel;
import com.hydertechno.mulven.R;

import java.util.ArrayList;
import java.util.List;

public class SeeAllProductsActivity extends AppCompatActivity {
    private String title;
    private TextView titleName;
    private RecyclerView productRecyclerView;
    private AllProductsAdapter all_product_Adapter;
    private List<CategoriesModel> allProductsList=new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all_products);
        init();
        Intent intent=getIntent();
        title=intent.getStringExtra("title");
        titleName.setText(title);
       // titleName.setPaintFlags(titleName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        for(int a=12; a>0;a--){
            if(a%2==0){
                allProductsList.add(new CategoriesModel("৳ 180","15 pcs/set Imitation Black Gem & Rhinestone Inlay Rings for Women",R.drawable.ring));
            }
            else{
                allProductsList.add(new CategoriesModel("৳ 250","white stone jewelry set for women",R.drawable.jewelry));
            }
        }

    }

    private void init() {
        titleName=findViewById(R.id.titleName);
        productRecyclerView=findViewById(R.id.allProductRecyclerView);
        all_product_Adapter=new AllProductsAdapter(allProductsList,this);
        productRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        productRecyclerView.setAdapter(all_product_Adapter);
    }

    public void seeAllProductBack(View view) {
        finish();
    }
}