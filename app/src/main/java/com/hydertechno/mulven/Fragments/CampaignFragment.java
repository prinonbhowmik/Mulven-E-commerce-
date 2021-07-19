package com.hydertechno.mulven.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.hydertechno.mulven.Adapters.CampaignAdapter;
import com.hydertechno.mulven.Api.ApiInterface;
import com.hydertechno.mulven.Api.ApiUtils;
import com.hydertechno.mulven.Internet.ConnectivityReceiver;
import com.hydertechno.mulven.Models.Campaign;
import com.hydertechno.mulven.Models.CampaignModel;
import com.hydertechno.mulven.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CampaignFragment extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener{
    private DrawerLayout drawerLayout;
    private ImageView navIcon;
    private int categoryID;
    private TextView titleName,noCampaign;
    private RecyclerView campaignRecyclerView;
    private CampaignAdapter campaignAdapter;
    private List<CampaignModel> campaignModelList =new ArrayList<>();
    private ApiInterface apiInterface;
    public static LinearLayout rootLayout;
    private RelativeLayout progressRL;
    private Snackbar snackbar;
    private boolean isConnected;
    private ConnectivityReceiver connectivityReceiver;
    private IntentFilter intentFilter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_campaign, container, false);
        init(view);
        drawerLayout=getActivity().findViewById(R.id.drawerLayout);
        navIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
                hideKeyboardFrom(view.getContext());
            }
        });


        getCategories();

        return view;
    }


    private void init(View view) {
        rootLayout=view.findViewById(R.id.campaign_fragment_rootLayout);
        progressRL=view.findViewById(R.id.progressRL);
        intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        connectivityReceiver = new ConnectivityReceiver();
        navIcon=view.findViewById(R.id.navIcon);
        noCampaign=view.findViewById(R.id.noCampaign);
        campaignRecyclerView =view.findViewById(R.id.campaignRecyclerView);
        campaignAdapter =new CampaignAdapter(campaignModelList,getContext());
        LinearLayoutManager camp = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        campaignRecyclerView.setLayoutManager(camp);
        campaignRecyclerView.setItemAnimator(new DefaultItemAnimator());
        campaignRecyclerView.setAdapter(campaignAdapter);
        apiInterface= ApiUtils.getUserService();
    }

    private void hideKeyboardFrom(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getActivity().getWindow().getDecorView().getRootView().getWindowToken(), 0);
    }
    private void getCategories() {
        progressRL.setVisibility(View.VISIBLE);
        campaignModelList.clear();
        Call<Campaign> call = apiInterface.getAllCampaigns();
        call.enqueue(new Callback<Campaign>() {
            @Override
            public void onResponse(Call<Campaign> call, Response<Campaign>response) {
                if (response.isSuccessful()){
                    progressRL.setVisibility(View.GONE);
                    Campaign list=response.body();
                    campaignModelList=list.getCampaign();
                    campaignAdapter = new CampaignAdapter(campaignModelList, getContext());
                    campaignRecyclerView.setAdapter(campaignAdapter);
                    if (campaignModelList.size() == 0) {
                        campaignRecyclerView.setVisibility(View.GONE);
                        noCampaign.setVisibility(View.VISIBLE);
                    }
                }
                campaignAdapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<Campaign> call, Throwable t) {
                progressRL.setVisibility(View.GONE);
            }
        });
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
}