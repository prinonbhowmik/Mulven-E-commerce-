package com.hydertechno.mulven.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hydertechno.mulven.Adapters.ProductAdapter;
import com.hydertechno.mulven.Adapters.ProductFeatureAdapter;
import com.hydertechno.mulven.Adapters.ProductImagesAdapter;
import com.hydertechno.mulven.Api.ApiUtils;
import com.hydertechno.mulven.Api.Config;
import com.hydertechno.mulven.DatabaseHelper.Database_Helper;
import com.hydertechno.mulven.Interface.ProductImageClickInterface;
import com.hydertechno.mulven.Models.CategoriesModel;
import com.hydertechno.mulven.Models.ImageGallery;
import com.hydertechno.mulven.Models.ProductDetails;
import com.hydertechno.mulven.Models.ProductFeature;
import com.hydertechno.mulven.Models.ProductImagesModel;
import com.hydertechno.mulven.R;
import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailsActivity extends AppCompatActivity implements ProductImageClickInterface {
    private AutoCompleteTextView sizeTV,colorTV;
    private ZoomageView product_Image;
    private TextView productOldPrice,addToCart,buyNow,product_Name,shop_Name,brand_Name,product_Price,shop_Address,cardQuantity;
    private RecyclerView productImagesRecycler,productFeatureRecyclerView,relatedProductRecyclerView;
    private ProductImagesAdapter productImagesAdapter;
    private ProductFeatureAdapter productFeatureAdapter;
    private Database_Helper databaseHelper;
    private List<ProductDetails> list;
    private int product_id;
    private WebView webView;
    private String url = "https://mulven.com/pro-det-for-app/",imageString;
    private ImageView shopLogoIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        Intent intent = getIntent();
        product_id = intent.getIntExtra("id",0);

        init();

        getProductDeatils();

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (databaseHelper.checkCart(product_id)){
                    Toast.makeText(ProductDetailsActivity.this, "Product Already in Cart", Toast.LENGTH_LONG).show();
                    return;
                }else{
                    databaseHelper.addToCart(product_id,product_Name.getText().toString(),
                            Integer.parseInt(productOldPrice.getText().toString()),
                            Integer.parseInt(product_Price.getText().toString()),
                            shop_Name.getText().toString(),Integer.parseInt(cardQuantity.getText().toString()),imageString);
                    Toast.makeText(ProductDetailsActivity.this, "Product Added To Cart", Toast.LENGTH_LONG).show();
                }
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

    private void getProductDeatils() {
        Call<ProductDetails> call = ApiUtils.getUserService().getProd_details(product_id);
        call.enqueue(new Callback<ProductDetails>() {
            @Override
            public void onResponse(Call<ProductDetails> call, Response<ProductDetails> response) {
                ProductDetails detailsList = response.body();
                product_Name.setText(""+detailsList.getProduct_name());
                productOldPrice.setText(""+detailsList.getMrp_price());
                product_Price.setText(""+detailsList.getUnit_price());
                shop_Name.setText(""+detailsList.getShop_name());
                shop_Address.setText(""+detailsList.getShop_address());
                brand_Name.setText(""+detailsList.getBrand());
                imageString = detailsList.getFeacher_image();
                try{
                    Picasso.get()
                            .load(Config.IMAGE_LINE+detailsList.getFeacher_image())
                            .into(product_Image);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try{
                    Picasso.get()
                            .load(Config.IMAGE_LINE+detailsList.getShop_logo())
                            .into(shopLogoIV);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                List<ProductFeature> productFeatureList = detailsList.getProduct_feature();
                productFeatureAdapter=new ProductFeatureAdapter(productFeatureList,ProductDetailsActivity.this);
                productFeatureRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
                productFeatureRecyclerView.setAdapter(productFeatureAdapter);

                List<ImageGallery> productImagesModelList = detailsList.getImage_gallery();

                    productImagesAdapter=new ProductImagesAdapter(productImagesModelList,ProductDetailsActivity.this);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(ProductDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false);
                    productImagesRecycler.setLayoutManager(layoutManager);
                    productImagesRecycler.setAdapter(productImagesAdapter);

            }

            @Override
            public void onFailure(Call<ProductDetails> call, Throwable t) {

            }
        });
    }

    private void init() {
        product_Name = findViewById(R.id.product_Name);
        shop_Name = findViewById(R.id.shop_Name);
        brand_Name = findViewById(R.id.brand_Name);
        product_Price = findViewById(R.id.product_Price);
        sizeTV=findViewById(R.id.sizeMenu);
        colorTV=findViewById(R.id.colorMenu);
        addToCart=findViewById(R.id.addToCartTV);
        buyNow=findViewById(R.id.buyNowTV);
        product_Image = findViewById(R.id.product_Image);
        cardQuantity = findViewById(R.id.cardQuantity);
        productOldPrice=findViewById(R.id.product_Old_Price);
        shopLogoIV=findViewById(R.id.shopLogoIV);
        shop_Address=findViewById(R.id.shop_Address);
        databaseHelper = new Database_Helper(this);

        productOldPrice.setPaintFlags(productOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        productImagesRecycler=findViewById(R.id.productImagesRecyclerView);
        productFeatureRecyclerView=findViewById(R.id.productFeatureRecyclerView);
        relatedProductRecyclerView=findViewById(R.id.relatedProductRecyclerView);
        webView = findViewById(R.id.description);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url+""+product_id);

    }

    public void productDetailsBack(View view) {
        finish();
    }
/*
    @Override
    public void OnClick(int image) {
        product_Image.setImageResource(image);
    }*/

    @Override
    public void OnClick(String image) {
        try{
            Picasso.get()
                    .load(Config.IMAGE_LINE+image)
                    .into(product_Image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}