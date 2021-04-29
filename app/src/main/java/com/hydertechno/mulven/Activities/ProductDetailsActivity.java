package com.hydertechno.mulven.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.hydertechno.mulven.Adapters.ProductAdapter;
import com.hydertechno.mulven.Adapters.ProductFeatureAdapter;
import com.hydertechno.mulven.Adapters.ProductImagesAdapter;
import com.hydertechno.mulven.Adapters.RelatedProductAdapter;
import com.hydertechno.mulven.Api.ApiInterface;
import com.hydertechno.mulven.Api.ApiUtils;
import com.hydertechno.mulven.Api.Config;
import com.hydertechno.mulven.DatabaseHelper.Database_Helper;
import com.hydertechno.mulven.Interface.ProductImageClickInterface;
import com.hydertechno.mulven.Internet.Connection;
import com.hydertechno.mulven.Internet.ConnectivityReceiver;
import com.hydertechno.mulven.Models.CategoriesModel;
import com.hydertechno.mulven.Models.ImageGalleryModel;
import com.hydertechno.mulven.Models.ProductColorModel;
import com.hydertechno.mulven.Models.ProductDetailsModel;
import com.hydertechno.mulven.Models.ProductFeatureModel;
import com.hydertechno.mulven.Models.ProductSizeModel;
import com.hydertechno.mulven.Models.ProductVariantModel;
import com.hydertechno.mulven.R;
import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailsActivity extends AppCompatActivity implements ProductImageClickInterface, ConnectivityReceiver.ConnectivityReceiverListener {
    private AutoCompleteTextView sizeTV,colorTV,variantTV;
    private TextInputLayout size_menu,color_menu,variant_menu;
    private ZoomageView product_Image;
    private TextView productOldPrice,addToCart,buyNow,product_Name,shop_Name,brand_Name,product_Price,shop_Address,cardQuantity;
    private RecyclerView productImagesRecycler,productFeatureRecyclerView,relatedProductRecyclerView;
    private ProductImagesAdapter productImagesAdapter;
    private ProductFeatureAdapter productFeatureAdapter;
    private Database_Helper databaseHelper;
    private List<ProductDetailsModel> list;
    private int product_id,quantity,category_id;
    private WebView webView;
    private String url = "https://mulven.com/pro-det-for-app/",imageString,size,color,variant,capmpagin_id;
    private ImageView shopLogoIV,card_Minus,card_Plus;
    private List<CategoriesModel> relatedProductList =new ArrayList<>();
    private ArrayList<String> productColor = new ArrayList<String>();
    private ArrayList<String> productSize = new ArrayList<String>();
    private ArrayList<String> productVariant = new ArrayList<String>();
    private ArrayList<Integer> productVariantPrice = new ArrayList<Integer>();
    private ApiInterface apiInterface;
    private RelatedProductAdapter relatedProductAdapter;
    private RelativeLayout feature_RelativeLayout,soldByRelativeLayout;
    private int productMrpPrice,productUnitPrice,store_id;
    private RelativeLayout rootLayout;
    private Snackbar snackbar;
    private boolean isConnected;
    private ConnectivityReceiver connectivityReceiver;
    private IntentFilter intentFilter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        Intent intent = getIntent();
        product_id = intent.getIntExtra("id",0);

        init();
        checkConnection();
        if (!isConnected) {
            snackBar(isConnected);
        }
        quantity=Integer.parseInt(cardQuantity.getText().toString());
        getProductDetails();

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnection();
                if (!isConnected) {
                    snackBar(isConnected);
                }else{
                if (sizeTV.getText().toString() != null) {
                    size = sizeTV.getText().toString();
                } else {
                    size = "";
                }
                if (colorTV.getText().toString() != null) {
                    color = colorTV.getText().toString();
                } else {
                    color = "";
                }
                if (variantTV.getText().toString() != null) {
                    variant = variantTV.getText().toString();
                } else {
                    variant = "";
                }
                if (capmpagin_id == null) {
                    capmpagin_id = "";
                }
                Log.d("CheckData", size + "," + color + "," + variant);
                if (databaseHelper.checkProductExist(product_id, size, color, variant)) {

                    int count = databaseHelper.checkQuantity(product_id);
                    databaseHelper.addQuantity(product_id, count + 1);
                    Toast.makeText(ProductDetailsActivity.this, "Product Added To Cart", Toast.LENGTH_SHORT).show();
                } else {
                    databaseHelper.addToCart(product_id, product_Name.getText().toString(),
                            productMrpPrice, productUnitPrice, size, color, variant,
                            shop_Name.getText().toString(), Integer.parseInt(cardQuantity.getText().toString()),
                            capmpagin_id, store_id, imageString);
                    Toast.makeText(ProductDetailsActivity.this, "Product Added To Cart", Toast.LENGTH_LONG).show();
                }
            }
        }
        });

        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    checkConnection();
                    if (!isConnected) {
                        snackBar(isConnected);
                    }else{
                        if (sizeTV.getText().toString() != null) {
                            size = sizeTV.getText().toString();
                        } else {
                            size = "";
                        }
                        if (colorTV.getText().toString() != null) {
                            color = colorTV.getText().toString();
                        } else {
                            color = "";
                        }
                        if (variantTV.getText().toString() != null) {
                            variant = variantTV.getText().toString();
                        } else {
                            variant = "";
                        }
                        if (capmpagin_id == null) {
                            capmpagin_id = "";
                        }
                        Log.d("CheckData", size + "," + color + "," + variant);
                        if (databaseHelper.checkProductExist(product_id, size, color, variant)) {

                            int count = databaseHelper.checkQuantity(product_id);
                            databaseHelper.addQuantity(product_id, count + 1);
                            Toast.makeText(ProductDetailsActivity.this, "Product Added To Cart", Toast.LENGTH_SHORT).show();
                        } else {
                            databaseHelper.addToCart(product_id, product_Name.getText().toString(),
                                    productMrpPrice, productUnitPrice, size, color, variant,
                                    shop_Name.getText().toString(), Integer.parseInt(cardQuantity.getText().toString()),
                                    capmpagin_id, store_id, imageString);
                            Toast.makeText(ProductDetailsActivity.this, "Product Added To Cart", Toast.LENGTH_LONG).show();
                        }
                    }
                Intent intent = new Intent(ProductDetailsActivity.this, MainActivity.class);
                intent.putExtra("fragment", "cart");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }

        });

        variantTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int index=productVariant.indexOf(variantTV.getText().toString());
                product_Price.setText("৳ "+productVariantPrice.get(index));
                productUnitPrice=productVariantPrice.get(index);
            }
        });



    }

    private void getProductDetails() {
        Call<ProductDetailsModel> call = ApiUtils.getUserService().getProd_details(product_id);
        call.enqueue(new Callback<ProductDetailsModel>() {
            @Override
            public void onResponse(Call<ProductDetailsModel> call, Response<ProductDetailsModel> response) {
                ProductDetailsModel detailsList = response.body();
                product_Name.setText(""+detailsList.getProduct_name());
                productMrpPrice=detailsList.getMrp_price();
                if(productMrpPrice==0){
                    productOldPrice.setVisibility(View.GONE);
                }else{
                    productOldPrice.setText("৳ "+productMrpPrice);
                }
                productUnitPrice=detailsList.getUnit_price();
                product_Price.setText("৳ "+productUnitPrice);
                shop_Name.setText(""+detailsList.getShop_name());
                shop_Address.setText(""+detailsList.getShop_address());
                brand_Name.setText(""+detailsList.getBrand());
                imageString = detailsList.getFeacher_image();
                category_id = detailsList.getCategory_id();
                store_id = detailsList.getStore_id();
                capmpagin_id = detailsList.getCampaign_id();
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
                //Get Product features
                List<ProductFeatureModel> productFeatureModelList = detailsList.getProduct_feature();
                if(productFeatureModelList.get(0) != null) {
                    feature_RelativeLayout.setVisibility(View.VISIBLE);
                    productFeatureAdapter = new ProductFeatureAdapter(productFeatureModelList, ProductDetailsActivity.this);
                    productFeatureRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                    productFeatureRecyclerView.setAdapter(productFeatureAdapter);
                }
                if(!shop_Name.equals("")){
                    soldByRelativeLayout.setVisibility(View.VISIBLE);
                }

                //Get Product Images
                List<ImageGalleryModel> productImagesModelList = detailsList.getImage_gallery();

                    productImagesAdapter=new ProductImagesAdapter(productImagesModelList,ProductDetailsActivity.this);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(ProductDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false);
                    productImagesRecycler.setLayoutManager(layoutManager);
                    productImagesRecycler.setAdapter(productImagesAdapter);

                //Get Product Colors
                List<ProductColorModel> productColorModelList = detailsList.getProduct_color();
                if(productColorModelList.get(0) != null){
                    color_menu.setVisibility(View.VISIBLE);
                productColor.clear();
                for (int i = 0; i < productColorModelList.size(); i++) {
                    productColor.add(productColorModelList.get(i).getColor_name());
                }
                ArrayAdapter<String> product_color = new ArrayAdapter<String>(ProductDetailsActivity.this, R.layout.spinner_item_design, R.id.simpleSpinner, productColor);
                product_color.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                colorTV.setText(product_color.getItem(0),false);
                colorTV.setAdapter(product_color);
                /*int index=productColorModelList.indexOf(product_color.getColor_name());
                colorTV.setSelection(productColor.indexOf(1));*/
                }

                //Get Product Sizes
                List<ProductSizeModel> productSizeModelList = detailsList.getProduct_size();
                if(productSizeModelList.get(0) != null){
                    size_menu.setVisibility(View.VISIBLE);
                    productSize.clear();
                    for (int i = 0; i < productSizeModelList.size(); i++) {
                        productSize.add(productSizeModelList.get(i).getSize_name());
                    }
                    ArrayAdapter<String> product_size = new ArrayAdapter<String>(ProductDetailsActivity.this, R.layout.spinner_item_design, R.id.simpleSpinner, productSize);
                    product_size.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    sizeTV.setText(product_size.getItem(0),false);
                    sizeTV.setAdapter(product_size);
                /*int index=productColorModelList.indexOf(product_color.getColor_name());
                colorTV.setSelection(productColor.indexOf(1));*/
                }

                //Get Product Variants
                List<ProductVariantModel> productVariantModelList = detailsList.getVariant();
                if(productVariantModelList.get(0) != null){
                    variant_menu.setVisibility(View.VISIBLE);
                    productVariant.clear();
                    for (int i = 0; i < productVariantModelList.size(); i++) {
                        productVariant.add(productVariantModelList.get(i).getFeature_name());
                        productVariantPrice.add(Integer.parseInt(productVariantModelList.get(i).getPrice()));
                    }
                    int lowestPrice=productVariantPrice.indexOf((Collections.min(productVariantPrice)));
                    ArrayAdapter<String> product_variant = new ArrayAdapter<String>(ProductDetailsActivity.this, R.layout.spinner_item_design, R.id.simpleSpinner, productVariant);
                    product_variant.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    variantTV.setText(product_variant.getItem(lowestPrice),false);
                    variantTV.setAdapter(product_variant);
                /*int index=productColorModelList.indexOf(product_color.getColor_name());
                colorTV.setSelection(productColor.indexOf(1));*/
                }
                getRelatedProduct(category_id);
            }

            @Override
            public void onFailure(Call<ProductDetailsModel> call, Throwable t) {

            }
        });

    }

    private void getRelatedProduct(int category_id) {
        relatedProductList.clear();
        Call<List<CategoriesModel>> call = apiInterface.getCategories(category_id);
        call.enqueue(new Callback<List<CategoriesModel>>() {
            @Override
            public void onResponse(Call<List<CategoriesModel>> call, Response<List<CategoriesModel>> response) {
                if (response.isSuccessful()){
                    relatedProductList = response.body();
                    relatedProductAdapter = new RelatedProductAdapter(relatedProductList, getApplicationContext());
                    relatedProductRecyclerView.setAdapter(relatedProductAdapter);
                }
                relatedProductAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<List<CategoriesModel>> call, Throwable t) {
                Log.d("ErrorKi",t.getMessage());
            }
        });
    }

    private void init() {
        rootLayout = findViewById(R.id.product_details_rootLayout);
        intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        connectivityReceiver = new ConnectivityReceiver();
        product_Name = findViewById(R.id.product_Name);
        shop_Name = findViewById(R.id.shop_Name);
        brand_Name = findViewById(R.id.brand_Name);
        product_Price = findViewById(R.id.product_Price);
        sizeTV=findViewById(R.id.sizeMenu);
        colorTV=findViewById(R.id.colorMenu);
        variantTV=findViewById(R.id.variantMenu);
        color_menu=findViewById(R.id.color_menu);
        size_menu=findViewById(R.id.size_menu);
        variant_menu=findViewById(R.id.variant_menu);
        addToCart=findViewById(R.id.addToCartTV);
        buyNow=findViewById(R.id.buyNowTV);
        product_Image = findViewById(R.id.product_Image);
        cardQuantity = findViewById(R.id.cardQuantity);
        card_Plus = findViewById(R.id.card_Plus);
        card_Minus = findViewById(R.id.card_Minus);
        productOldPrice=findViewById(R.id.product_Old_Price);
        shopLogoIV=findViewById(R.id.shopLogoIV);
        shop_Address=findViewById(R.id.shop_Address);
        databaseHelper = new Database_Helper(this);
        apiInterface = ApiUtils.getUserService();

        productOldPrice.setPaintFlags(productOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        productImagesRecycler=findViewById(R.id.productImagesRecyclerView);
        productFeatureRecyclerView=findViewById(R.id.productFeatureRecyclerView);
        relatedProductRecyclerView=findViewById(R.id.relatedProductRecyclerView);
        feature_RelativeLayout=findViewById(R.id.feature_RelativeLayout);
        soldByRelativeLayout=findViewById(R.id.soldByRelativeLayout);
        webView = findViewById(R.id.description);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url+""+product_id);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        layoutManager1.setSmoothScrollbarEnabled(true);
        relatedProductRecyclerView.setLayoutManager(layoutManager1);
        relatedProductRecyclerView.setAdapter(relatedProductAdapter);
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

    public void cardMinus(View view) {
        if(quantity>1) {
            quantity--;
            cardQuantity.setText("" + quantity);
        }
    }

    public void cardPlus(View view) {
        quantity++;
        cardQuantity.setText("" +quantity);
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        snackBar(isConnected);
    }

    private void checkConnection() {
        isConnected = ConnectivityReceiver.isConnected();
    }
    private void snackBar(boolean isConnected) {
        if(!isConnected) {
            snackbar = Snackbar.make(rootLayout, "No Internet Connection!", Snackbar.LENGTH_INDEFINITE);
            snackbar.setDuration(5000);
            snackbar.setActionTextColor(Color.WHITE);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(Color.RED);
            snackbar.show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(connectivityReceiver, intentFilter);
    }
    @Override
    protected void onResume() {

        // register connection status listener
        Connection.getInstance().setConnectivityListener(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try{
            if(connectivityReceiver!=null)
                unregisterReceiver(connectivityReceiver);

        }catch(Exception e){}

    }

    @Override
    protected void onStop() {
        try{
            if(connectivityReceiver!=null)
                unregisterReceiver(connectivityReceiver);

        }catch(Exception e){}

        super.onStop();
    }

}