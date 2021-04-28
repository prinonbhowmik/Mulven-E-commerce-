package com.hydertechno.mulven.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hydertechno.mulven.Activities.PlaceOrderDetailsActivity;
import com.hydertechno.mulven.Adapters.CartAdapter;
import com.hydertechno.mulven.Api.ApiUtils;
import com.hydertechno.mulven.DatabaseHelper.Database_Helper;
import com.hydertechno.mulven.Models.CartProductModel;
import com.hydertechno.mulven.Models.PlaceItemModel;
import com.hydertechno.mulven.Models.PlaceOrderModel;
import com.hydertechno.mulven.Models.ProductDetailsModel;
import com.hydertechno.mulven.R;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class CartFragment extends Fragment {
    private DrawerLayout drawerLayout;
    private RelativeLayout noCartLayout, cartLayout;
    private ImageView navIcon;
    private TextView placeOrder;
    public static TextView cardSubtotalAmount;
    private RecyclerView cartRecycler;
    private Database_Helper databaseHelper;
    private List<CartProductModel> list;
    private List<PlaceItemModel> item;
    private CartAdapter adapter;
    private SharedPreferences sharedPreferences;
    private String token;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        init(view);
        drawerLayout = getActivity().findViewById(R.id.drawerLayout);

        cardSubtotalAmount.setText("৳ " + databaseHelper.columnSum());

        int count = databaseHelper.numberOfrows().getCount();

        getCartProducts();

        navIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
                hideKeyboardFrom(view.getContext());
            }
        });

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = databaseHelper.getCart();
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        int id = cursor.getInt(cursor.getColumnIndex(databaseHelper.ID));
                        String name = cursor.getString(cursor.getColumnIndex(databaseHelper.PRODUCT_NAME));
                        int unit_price = cursor.getInt(cursor.getColumnIndex(databaseHelper.UNIT_PRICE));
                        int quantity = cursor.getInt(cursor.getColumnIndex(databaseHelper.QUANTITY));
                        String size = cursor.getString(cursor.getColumnIndex(databaseHelper.SIZE));
                        String color = cursor.getString(cursor.getColumnIndex(databaseHelper.COLOR));
                        String variant = cursor.getString(cursor.getColumnIndex(databaseHelper.VARIANT));
                        String campaign_id = cursor.getString(cursor.getColumnIndex(databaseHelper.CAMPAIGN_ID));
                        int store_id = cursor.getInt(cursor.getColumnIndex(databaseHelper.STORE_ID));

                        List<Map<String, String>> list1 = new ArrayList<>();
                        Map<String, String> parms = new HashMap<String,String>();

                        for (int i = 0; i < list.size(); i++) {
                            parms.put("item_id", String.valueOf(id));
                            parms.put("pro_name", name);
                            parms.put("variant", variant);
                            parms.put("size", size);
                            parms.put("color", color);
                            parms.put("price", String.valueOf(unit_price));
                            parms.put("order_from", campaign_id);
                            parms.put("store_id", String.valueOf(store_id));
                            parms.put("quantity", String.valueOf(quantity));
                            list1.add(parms);
                        }

                       /* Map<String, String> parms = new HashMap<String, String>();
                        for (int i = 0; i < list.size(); i++) {
                            parms.put("item_id",String.valueOf(id));
                            parms.put("pro_name",name);
                            parms.put("variant",variant);
                            parms.put("size",size);
                            parms.put("color",color);
                            parms.put("price",String.valueOf(unit_price));
                            parms.put("order_from",campaign_id);
                            parms.put("store_id", String.valueOf(store_id));
                            parms.put("quantity", String.valueOf(quantity));
                        }*/

                        JSONArray array = new JSONArray(list1);
                        Log.d("checkList", String.valueOf(array));

                        Call<PlaceOrderModel> call = ApiUtils.getUserService().placeOrder(token, array);
                        call.enqueue(new Callback<PlaceOrderModel>() {
                            @Override
                            public void onResponse(Call<PlaceOrderModel> call, Response<PlaceOrderModel> response) {
                                if (response.isSuccessful()) {
                                    int status = response.body().getStatus();
                                    if (status == 1) {
                                        Toast.makeText(getContext(), "Order Placed Successfully!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<PlaceOrderModel> call, Throwable t) {
                                Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                }
            }
        });
        return view;
    }

    private void getCartProducts() {

        Cursor cursor = databaseHelper.getCart();
        if (cursor != null) {
            while (cursor.moveToNext()) {

                int id = cursor.getInt(cursor.getColumnIndex(databaseHelper.ID));
                String name = cursor.getString(cursor.getColumnIndex(databaseHelper.PRODUCT_NAME));
                int mrp_price = cursor.getInt(cursor.getColumnIndex(databaseHelper.MRP_PRICE));
                int unit_price = cursor.getInt(cursor.getColumnIndex(databaseHelper.UNIT_PRICE));
                String shop_name = cursor.getString(cursor.getColumnIndex(databaseHelper.SHOP_NAME));
                int quantity = cursor.getInt(cursor.getColumnIndex(databaseHelper.QUANTITY));
                String image = cursor.getString(cursor.getColumnIndex(databaseHelper.IMAGE));
                String size = cursor.getString(cursor.getColumnIndex(databaseHelper.SIZE));
                String color = cursor.getString(cursor.getColumnIndex(databaseHelper.COLOR));
                String variant = cursor.getString(cursor.getColumnIndex(databaseHelper.VARIANT));

                CartProductModel cartProductsModel = new CartProductModel(id, name, mrp_price, unit_price, size, color, variant, shop_name, quantity, image);

                list.add(cartProductsModel);
                adapter = new CartAdapter(list, getContext());
                cartRecycler.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            if (list.size() == 0) {
                noCartLayout.setVisibility(View.VISIBLE);
                cartLayout.setVisibility(View.GONE);
            }
        }
    }

    private void init(View view) {
        navIcon = view.findViewById(R.id.navIcon);
        placeOrder = view.findViewById(R.id.placeOrderTV);
        noCartLayout = view.findViewById(R.id.noCartLayout);
        cartLayout = view.findViewById(R.id.cartLayout);
        cardSubtotalAmount = view.findViewById(R.id.cardSubtotalAmount);
        databaseHelper = new Database_Helper(getContext());
        list = new ArrayList<>();
        item = new ArrayList<>();
        cartRecycler = view.findViewById(R.id.cartRecycler);
        cartRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        sharedPreferences = getContext().getSharedPreferences("MyRef", MODE_PRIVATE);
        token = sharedPreferences.getString("token", null);
        Log.d("ShowToken", token);
    }

    private void hideKeyboardFrom(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getActivity().getWindow().getDecorView().getRootView().getWindowToken(), 0);
    }
}