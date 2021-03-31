package com.hydertechno.mulven.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hydertechno.mulven.Activities.AddressActivity;
import com.hydertechno.mulven.Activities.ChangePasswordActivity;
import com.hydertechno.mulven.Activities.PlaceOrderListActivity;
import com.hydertechno.mulven.Activities.ProfileActivity;
import com.hydertechno.mulven.R;

public class ProfileFragment extends Fragment {
    private LinearLayout checkBalanceLayout,addressLayout,profileLayout;
    private RelativeLayout bottomRL;
    private DrawerLayout drawerLayout;
    private ImageView navIcon;
    private Dialog dialog;
    private Animation upAnimation,downAnimation;
    private RelativeLayout paymentHistoryRL,changePasswordRL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        init(view);
        drawerLayout=getActivity().findViewById(R.id.drawerLayout);
        navIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
                hideKeyboardFrom(view.getContext());
            }
        });

        checkBalanceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(view.getContext());
                dialog.setContentView(R.layout.check_balance_layout_design);

                dialog.setCancelable(true);

                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        });

        addressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddressActivity.class));
            }
        });

        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ProfileActivity.class));
            }
        });

        paymentHistoryRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PlaceOrderListActivity.class));
            }
        });

        changePasswordRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ChangePasswordActivity.class));
            }
        });


        return view;
    }

    private void init(View view) {
        navIcon=view.findViewById(R.id.navIcon);
        upAnimation=AnimationUtils.loadAnimation(getContext(),R.anim.slide_up);
        downAnimation=AnimationUtils.loadAnimation(getContext(),R.anim.slide_down);

        bottomRL=view.findViewById(R.id.bottomRL);
        bottomRL.setAnimation(upAnimation);
        checkBalanceLayout=view.findViewById(R.id.balanceLayout);
        addressLayout=view.findViewById(R.id.addressLayout);
        profileLayout=view.findViewById(R.id.profileLayout);
        paymentHistoryRL=view.findViewById(R.id.paymentHistoryRL);
        changePasswordRL=view.findViewById(R.id.changePasswordRL);
    }
    private void hideKeyboardFrom(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getActivity().getWindow().getDecorView().getRootView().getWindowToken(), 0);
    }
}