package com.hydertechno.mulven.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hydertechno.mulven.Adapters.NotificationAdapter;
import com.hydertechno.mulven.Api.ApiUtils;
import com.hydertechno.mulven.Api.Config;
import com.hydertechno.mulven.Models.NotificationModel;
import com.hydertechno.mulven.Models.UserProfile;
import com.hydertechno.mulven.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class NotificationFragment extends Fragment {
    private DrawerLayout drawerLayout;
    private ImageView navIcon;
    private SharedPreferences sharedPreferences;
    private RelativeLayout noNotificationLayout,progressRL;
    private String  token;
    private RecyclerView notificationRecycler;
    private NotificationAdapter adapter;
    private List<NotificationModel> modelList=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_notification, container, false);
        init(view);
        drawerLayout=getActivity().findViewById(R.id.drawerLayout);

        navIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
                hideKeyboardFrom(view.getContext());
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getNotification();
    }

    private void getNotification() {
        progressRL.setVisibility(View.VISIBLE);
        Call<List<NotificationModel>> call = ApiUtils.getUserService().getNotification(token);
        call.enqueue(new Callback<List<NotificationModel>>() {
            @Override
            public void onResponse(Call<List<NotificationModel>> call, Response<List<NotificationModel>> response) {
                if (response.isSuccessful() && response.code()==200){
                    if(response.body()!=null && response.body().size()>0){
                        progressRL.setVisibility(View.GONE);
                        noNotificationLayout.setVisibility(View.GONE);
                        adapter=new NotificationAdapter(response.body(),getContext());
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
                        notificationRecycler.setLayoutManager(layoutManager);
                        notificationRecycler.setAdapter(adapter);
                    }else {
                        progressRL.setVisibility(View.GONE);
                        noNotificationLayout.setVisibility(View.VISIBLE);
                    }
                }else
                    Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<List<NotificationModel>> call, Throwable t) {
                progressRL.setVisibility(View.GONE);
                noNotificationLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void init(View view) {
        navIcon=view.findViewById(R.id.navIcon);
        noNotificationLayout=view.findViewById(R.id.noNotificationLayout);
        progressRL=view.findViewById(R.id.progressRL);
        notificationRecycler=view.findViewById(R.id.notificationRecycler);
        sharedPreferences = getContext().getSharedPreferences("MyRef", MODE_PRIVATE);
        token = sharedPreferences.getString("token",null);
    }

    private void hideKeyboardFrom(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getActivity().getWindow().getDecorView().getRootView().getWindowToken(), 0);
    }
}