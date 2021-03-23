package com.hydertechno.mulven.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.Toast;

import com.hydertechno.mulven.Activities.PlaceOrderActivity;
import com.hydertechno.mulven.Activities.PlaceOrderDetailsActivity;
import com.hydertechno.mulven.Adapters.CartAdapter;
import com.hydertechno.mulven.DatabaseHelper.Database_Helper;
import com.hydertechno.mulven.Models.CartProduct;
import com.hydertechno.mulven.R;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {
    private DrawerLayout drawerLayout;
    private ImageView navIcon;
    private TextView placeOrder;
    public static TextView cardSubtotalAmount;
    private RecyclerView cartRecycler;
    private Database_Helper databaseHelper;
    private List<CartProduct> list;
    private CartAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_cart, container, false);
        init(view);
        drawerLayout=getActivity().findViewById(R.id.drawerLayout);

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
                startActivity(new Intent(getActivity(), PlaceOrderDetailsActivity.class));
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

                CartProduct cartProducts = new CartProduct(id,name,mrp_price,unit_price,shop_name ,quantity, image);

                list.add(cartProducts);
                adapter = new CartAdapter(list, getContext());
                cartRecycler.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }else{
            Toast.makeText(getContext(), "No products in Cart", Toast.LENGTH_SHORT).show();
        }
    }

    private void init(View view) {
        navIcon=view.findViewById(R.id.navIcon);
        placeOrder=view.findViewById(R.id.placeOrderTV);
        cartRecycler = view.findViewById(R.id.cartRecycler);
        cardSubtotalAmount = view.findViewById(R.id.cardSubtotalAmount);
        databaseHelper = new Database_Helper(getContext());
        list = new ArrayList<>();
        cartRecycler = view.findViewById(R.id.cartRecycler);
        cartRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
    }
    private void hideKeyboardFrom(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getActivity().getWindow().getDecorView().getRootView().getWindowToken(), 0);
    }
}