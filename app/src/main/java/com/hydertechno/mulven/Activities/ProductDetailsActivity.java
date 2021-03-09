package com.hydertechno.mulven.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hydertechno.mulven.Adapters.ProductAdapter;
import com.hydertechno.mulven.Adapters.ProductImagesAdapter;
import com.hydertechno.mulven.Interface.ProductImageClickInterface;
import com.hydertechno.mulven.Models.CategoriesModel;
import com.hydertechno.mulven.Models.ProductImagesModel;
import com.hydertechno.mulven.R;
import com.jsibbold.zoomage.ZoomageView;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailsActivity extends AppCompatActivity implements ProductImageClickInterface {
    private AutoCompleteTextView sizeTV,colorTV;
    private ZoomageView product_Image;
    private TextView productOldPrice,addToCart,buyNow;
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

      /*  for(int a=12; a>0;a--){
            if(a%2==0){
                jewelry_and_watch.add(new CategoriesModel("৳ 180","15 pcs/set Imitation Black Gem & Rhinestone Inlay Rings for Women",R.drawable.ring));
            }
            else{
                jewelry_and_watch.add(new CategoriesModel("৳ 250","white stone jewelry set for women",R.drawable.jewelry));

            }
        }*/

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProductDetailsActivity.this, "1 item add to cart ", Toast.LENGTH_SHORT).show();
            }
        });

        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailsActivity.this, MainActivity.class);
                intent.putExtra("fragment","cart");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
               // startActivity(new Intent(ProductDetailsActivity.this,MainActivity.class).putExtra("fragment","cart"));
            }
        });



    }

    private void init() {
        sizeTV=findViewById(R.id.sizeMenu);
        colorTV=findViewById(R.id.colorMenu);
        addToCart=findViewById(R.id.addToCartTV);
        buyNow=findViewById(R.id.buyNowTV);
        product_Image = findViewById(R.id.product_Image);
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



    @Override
    public void OnClick(int image) {
        product_Image.setImageResource(image);
    }
}