package com.hydertechno.mulven.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hydertechno.mulven.Activities.PhoneNumber;
import com.hydertechno.mulven.R;
public class AccountFragment extends Fragment {
    private RelativeLayout loginRelative;
    private TextInputLayout phoneTIL,passwordTIL;
    private TextInputEditText phoneTIET,passwordTIET;
    private TextView forgetPasswordTV,newRegisterTV;
    private Button logInBtn;
    private DrawerLayout drawerLayout;
    private ImageView navIcon;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_account, container, false);
        init(view);
        drawerLayout=getActivity().findViewById(R.id.drawerLayout);
        newRegisterTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), PhoneNumber.class));
            }
        });
        navIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
                hideKeyboardFrom(view.getContext());
            }
        });

        return view;
    }

    private void init(View view) {
        loginRelative=view.findViewById(R.id.logInRelative);
        phoneTIL=view.findViewById(R.id.phone_LT);
        passwordTIL=view.findViewById(R.id.password_LT);
        phoneTIET=view.findViewById(R.id.phone_ET);
        passwordTIET=view.findViewById(R.id.password_ET);
        forgetPasswordTV=view.findViewById(R.id.forget_pass_TV);
        newRegisterTV=view.findViewById(R.id.registerTV);
        logInBtn=view.findViewById(R.id.loginBtn);
        navIcon=view.findViewById(R.id.navIcon);

    }

    private void hideKeyboardFrom(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getActivity().getWindow().getDecorView().getRootView().getWindowToken(), 0);
    }
}