package com.hydertechno.mulven.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.hydertechno.mulven.Activities.MainActivity;
import com.hydertechno.mulven.Activities.PaymentMethodsActivity;
import com.hydertechno.mulven.Activities.PlaceOrderListActivity;
import com.hydertechno.mulven.Adapters.CartAdapter;
import com.hydertechno.mulven.Api.ApiUtils;
import com.hydertechno.mulven.DatabaseHelper.Database_Helper;
import com.hydertechno.mulven.Internet.ConnectivityReceiver;
import com.hydertechno.mulven.Models.CartProductModel;
import com.hydertechno.mulven.Models.PlaceItemModel;
import com.hydertechno.mulven.Models.PlaceOrderModel;
import com.hydertechno.mulven.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class CartFragment extends Fragment  implements ConnectivityReceiver.ConnectivityReceiverListener{
    private DrawerLayout drawerLayout;
    public static RelativeLayout totalLayout,noCartLayout,cartLayout;
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
    private int loggedIn;
    private Dialog dialog;
    private RelativeLayout rootLayout,progressRL;
    private Snackbar snackbar;
    private boolean isConnected;
    private ConnectivityReceiver connectivityReceiver;
    private IntentFilter intentFilter;

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
                checkConnection();
                if (!isConnected) {
                    snackBar(isConnected);
                } else {
                    if (loggedIn == 0) {
                        Toasty.normal(getContext(), "Please login first!", Toasty.LENGTH_SHORT).show();
                    } else if (loggedIn == 1) {

                        Cursor cursor = databaseHelper.getCart();
                        if (cursor != null) {

                            double unitPrice = databaseHelper.columnSum();
                            if (unitPrice >= 500) {
                                ArrayList<CartProductModel> allCartProducts = databaseHelper.getAllCartProducts();

                                ArrayList<JSONObject> jsonArrayList = new ArrayList<>();

                                for (CartProductModel item : allCartProducts) {
//                                    Map<String, String> parms = new HashMap<String, String>();
                                    JSONObject parms = new JSONObject();

                                    try {
                                        parms.put("item_id", String.valueOf(item.getId()));
                                        parms.put("pro_name", item.getProduct_name());
                                        parms.put("sku", item.getSku());
                                        parms.put("variant", item.getVariant());
                                        parms.put("size", item.getSize());
                                        parms.put("color", item.getColor());
                                        parms.put("price", String.valueOf(item.getUnit_price()));
                                        parms.put("order_from", item.getCampaignId());
                                        parms.put("store_id", String.valueOf(item.getStoreId()));
                                        parms.put("category_id", String.valueOf(item.getCategoryId()));
                                        parms.put("quantity", String.valueOf(item.getQuantity()));

                                        jsonArrayList.add(parms);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
//                                    array = new JSONArray(list1);
//                                    jsonArrayList.add(array);
                                }
                                Log.d("checkList", String.valueOf(jsonArrayList));

                                if (jsonArrayList.size() > 0) {
                                    progressRL.setVisibility(View.VISIBLE);
                                    Call<PlaceOrderModel> call = ApiUtils.getUserService().placeOrder(token, jsonArrayList.toString());
                                    call.enqueue(new Callback<PlaceOrderModel>() {
                                        @Override
                                        public void onResponse(Call<PlaceOrderModel> call, Response<PlaceOrderModel> response) {
                                            Log.e("Response=====>", response.toString());
                                            if (response.isSuccessful() && response.code() == 200) {
                                                int status = response.body().getStatus();
                                                Log.e("Response=====>", response.body().getMessage() + "");
                                                Log.e("Response=====>", response.body().getStatus() + "");
                                                if (status == 200) {
                                                    showSuccessDialog();
                                                } else {
                                                    showErrorDialog(response.body().getMessage());
                                                }
                                            } else {
                                                showErrorDialog("Something went wrong, Please try again!");
                                            }
                                            progressRL.setVisibility(View.GONE);
                                        }
                                        @Override
                                        public void onFailure(Call<PlaceOrderModel> call, Throwable t) {
                                            showErrorDialog("Something went wrong, Please contact support!");
                                            progressRL.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }
                            } else {
                                Toast.makeText(getContext(), "Minimum order value is 500 Tk", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
        });
        return view;
    }


    private void showSuccessDialog() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        DialogFragment newFragment = new OrderSuccessFragment();
        newFragment.show(ft, "dialog");

        ArrayList<CartProductModel> allCartProducts = databaseHelper.getAllCartProducts();
        for (CartProductModel item : allCartProducts) {
            databaseHelper.deleteData(item.getId(), item.getSize(), item.getColor(), item.getVariant());
        }
        int count=databaseHelper.numberOfrows().getCount();
        if (count>0) {
            MainActivity.chipNavigationBar.showBadge(R.id.cart, count);
        } else{
            MainActivity.chipNavigationBar.dismissBadge(R.id.cart);
            totalLayout.setVisibility(View.GONE);
            cartLayout.setVisibility(View.GONE);
            noCartLayout.setVisibility(View.VISIBLE);
        }
    }

    private void showErrorDialog(String text) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        DialogFragment newFragment = new OrderErrorFragment(text);
        newFragment.show(ft, "dialog");
    }

    private void getCartProducts() {
        ArrayList<CartProductModel> allCartProducts = databaseHelper.getAllCartProducts();
        if (allCartProducts.size() == 0) {
            noCartLayout.setVisibility(View.VISIBLE);
            cartLayout.setVisibility(View.GONE);
        } else {
            adapter = new CartAdapter(allCartProducts, getContext());
            cartRecycler.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        progressRL.setVisibility(View.GONE);
    }

    private void init(View view) {
        rootLayout=view.findViewById(R.id.fragment_cart_rootLayout);
        intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        connectivityReceiver = new ConnectivityReceiver();
        navIcon = view.findViewById(R.id.navIcon);
        placeOrder = view.findViewById(R.id.placeOrderTV);
        noCartLayout = view.findViewById(R.id.noCartLayout);
        cartLayout = view.findViewById(R.id.cartLayout);
        totalLayout = view.findViewById(R.id.totalLayout);
        cardSubtotalAmount = view.findViewById(R.id.cardSubtotalAmount);
        databaseHelper = new Database_Helper(getContext());
        list = new ArrayList<>();
        item = new ArrayList<>();
        cartRecycler = view.findViewById(R.id.cartRecycler);
        cartRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        sharedPreferences = getContext().getSharedPreferences("MyRef", MODE_PRIVATE);
        token = sharedPreferences.getString("token", null);
        loggedIn = sharedPreferences.getInt("loggedIn",0);
        progressRL=view.findViewById(R.id.progressRL);
        //Log.d("ShowToken", token);
    }

    private void hideKeyboardFrom(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getActivity().getWindow().getDecorView().getRootView().getWindowToken(), 0);
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
            snackbar = Snackbar.make(rootLayout, "No Internet Connection!", Snackbar.LENGTH_INDEFINITE).setAction("ReTry", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //recreate();
                }
            });
            snackbar.setDuration(5000);
            snackbar.setActionTextColor(Color.WHITE);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(Color.RED);
            snackbar.show();
        }
    }
}