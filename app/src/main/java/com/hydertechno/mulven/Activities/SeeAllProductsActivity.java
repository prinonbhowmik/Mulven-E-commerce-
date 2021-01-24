package com.hydertechno.mulven.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.hydertechno.mulven.Adapters.AllProductsAdapter;
import com.hydertechno.mulven.Adapters.CategoriesAdapter;
import com.hydertechno.mulven.Adapters.ProductAdapter;
import com.hydertechno.mulven.Models.CategoriesModel;
import com.hydertechno.mulven.R;

import java.util.ArrayList;
import java.util.List;

public class SeeAllProductsActivity extends AppCompatActivity {
    private String title;
    private ImageView searchIcon;
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
        allProductsList.add(new CategoriesModel("৳ 180","15 pcs/set Imitation Black Gem & Rhinestone Inlay Rings for Women",R.drawable.ring));
        allProductsList.add(new CategoriesModel("৳ 250","white stone jewelry set for women",R.drawable.jewelry));
        allProductsList.add(new CategoriesModel("৳ 180","15 pcs/set Imitation Black Gem & Rhinestone Inlay Rings for Women",R.drawable.ring));
        allProductsList.add(new CategoriesModel("৳ 250","white stone jewelry set for women",R.drawable.jewelry));
        allProductsList.add(new CategoriesModel("৳ 180","15 pcs/set Imitation Black Gem & Rhinestone Inlay Rings for Women",R.drawable.ring));
        allProductsList.add(new CategoriesModel("৳ 250","white stone jewelry set for women",R.drawable.jewelry));
        allProductsList.add(new CategoriesModel("৳ 180","15 pcs/set Imitation Black Gem & Rhinestone Inlay Rings for Women",R.drawable.ring));
        allProductsList.add(new CategoriesModel("৳ 250","white stone jewelry set for women",R.drawable.jewelry));
        /*searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMenuInflater().inflate(R.menu.search_bar_menu,);
                MenuItem menuItem=menu.findItem(R.id.searchBar);
                SearchView searchView=(SearchView) menuItem.getActionView();
                searchView.setQueryHint("Search Item...");
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                });
            }
        });*/

    }

    private void init() {
        titleName=findViewById(R.id.titleName);
        searchIcon=findViewById(R.id.searchIV);
        productRecyclerView=findViewById(R.id.allProductRecyclerView);
        all_product_Adapter=new AllProductsAdapter(allProductsList,this);
        productRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        productRecyclerView.setAdapter(all_product_Adapter);
    }

    public void seeAllProductBack(View view) {
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_bar_menu,menu);
        MenuItem menuItem=menu.findItem(R.id.searchBar);
        SearchView searchView=(SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search Item...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}