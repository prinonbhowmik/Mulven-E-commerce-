package com.hydertechno.mulven.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
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
    private String phone,password;
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
        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = phoneTIET.getText().toString();
                password = passwordTIET.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    hideKeyboardFrom(getContext());
                    phoneTIL.setError("Please Enter Phone Number");
                    phoneTIET.requestFocus();
                } else if (phone.length() != 11) {
                    hideKeyboardFrom(getContext());
                    phoneTIL.setError("Please Provide Correct Phone Number");
                    phoneTIET.requestFocus();
                }else if (TextUtils.isEmpty(password)) {
                    hideKeyboardFrom(getContext());
                    passwordTIL.setError("Please Enter Password");
                    passwordTIET.requestFocus();
                } else if (password.length() < 5) {
                    hideKeyboardFrom(getContext());
                    passwordTIL.setError("Minimum 6 digits password");
                    passwordTIET.requestFocus();
                } else {
                    phoneTIL.setErrorEnabled(false);
                    passwordTIL.setErrorEnabled(false);
                    hideKeyboardFrom(getContext());
                    Fragment newFragment = new ProfileFragment();
                    // consider using Java coding conventions (upper first char class names!!!)
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();

                    // Replace whatever is in the fragment_container view with this fragment,
                    // and add the transaction to the back stack
                    transaction.replace(R.id.fragment_container, newFragment);
                    transaction.addToBackStack(null);

                    // Commit the transaction
                    transaction.commit();
                }
                 if (phone.length() == 11) {
                    hideKeyboardFrom(getContext());
                    phoneTIL.setErrorEnabled(false);
                }
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