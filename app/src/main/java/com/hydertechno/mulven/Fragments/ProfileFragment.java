package com.hydertechno.mulven.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.hydertechno.mulven.Activities.ChangePasswordActivity;
import com.hydertechno.mulven.Activities.PaymentHistoryActivity;
import com.hydertechno.mulven.Activities.PlaceOrderListActivity;
import com.hydertechno.mulven.Activities.ProfileActivity;
import com.hydertechno.mulven.Activities.RefundMethodFormActivity;
import com.hydertechno.mulven.Activities.RefundOTPActivity;
import com.hydertechno.mulven.Activities.RefundSettlementActivity;
import com.hydertechno.mulven.Activities.RewardsActivity;
import com.hydertechno.mulven.Api.ApiUtils;
import com.hydertechno.mulven.Api.Config;
import com.hydertechno.mulven.Internet.ConnectivityReceiver;
import com.hydertechno.mulven.Models.UserProfile;
import com.hydertechno.mulven.R;
import com.hydertechno.mulven.Utilities.AppBarStateChangeListener;
import com.hydertechno.mulven.Utilities.Utils;
import com.squareup.picasso.Picasso;

import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener {
    private LinearLayout checkBalanceLayout,addressLayout,profileLayout;
    private RelativeLayout bottomRL;
    private TextView username,phoneNo;
    private DrawerLayout drawerLayout;
    private ImageView navIcon;
    private CircleImageView mAvatarImageView;
    private Dialog dialog;
    private Animation upAnimation,downAnimation;
    private CardView userProfileCV;
    private CardView orderHistoryCV;
    private CardView paymentHistoryCV;
    private CardView refundSettlementCV;
    private CardView sendInvitationCV;
    private CardView changePasswordCV;
    private SharedPreferences sharedPreferences;
    private String token,name,phone;
    private int id,userId;
    private CoordinatorLayout rootLayout;
    private Snackbar snackbar;
    private boolean isConnected;
    private ConnectivityReceiver connectivityReceiver;
    private IntentFilter intentFilter;

    private AppBarLayout mAppBarLayout;
    private Toolbar anim_toolbar;
    private TextView mToolbarTextView;
    private Space mSpace;
    AppBarStateChangeListener mAppBarStateChangeListener;
    private final static float EXPAND_AVATAR_SIZE_DP = 130f;
    private final static float COLLAPSED_AVATAR_SIZE_DP = 32f;
    private final float[] mAvatarPoint = new float[2], mSpacePoint = new float[2],
            mToolbarTextPoint = new float[2], mTitleTextViewPoint = new float[2];
    private float mTitleTextSize;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        init(view);

        token = sharedPreferences.getString("token",null);
        name = sharedPreferences.getString("userName",null);
        phone = sharedPreferences.getString("userPhone",null);

        Log.d("ShowToken",token);
        username.setText(name);
        phoneNo.setText(phone);
        Call<UserProfile> call = ApiUtils.getUserService().getUserData(token);
        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if (response.isSuccessful()){
                    id = response.body().getId();
                    if(response.body().getUser_photo()!=null) {
                        try {
                            Picasso.get()
                                    .load(Config.IMAGE_LINE + response.body().getUser_photo())
                                    .into(mAvatarImageView);
                            username.post(new Runnable() {
                                @Override
                                public void run() {
                                    resetPoints(true);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                else {

                    try {
                        Picasso.get()
                                .load(R.drawable.demo_profile)
                                .into(mAvatarImageView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {

            }
        });

        drawerLayout=getActivity().findViewById(R.id.drawerLayout);
        navIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
                hideKeyboardFrom(view.getContext());
            }
        });

//        checkBalanceLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                checkConnection();
//                if (!isConnected) {
//                    snackBar(isConnected);
//                } else {
//                    dialog = new Dialog(view.getContext());
//                    dialog.setContentView(R.layout.check_balance_layout_design);
//
//                    dialog.setCancelable(true);
//
//                    dialog.show();
//                    Window window = dialog.getWindow();
//                    window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                }
//            }
//        });
//
//        addressLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                checkConnection();
//                if (!isConnected) {
//                    snackBar(isConnected);
//                } else {
//                    // startActivity(new Intent(getActivity(), AddressActivity.class));
//                    Intent intent = new Intent(getActivity(), AddressActivity.class);
//                    startActivity(intent);
//                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                    getActivity().finish();
//                }
//            }
//        });
//
//        profileLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                checkConnection();
//                if (!isConnected) {
//                    snackBar(isConnected);
//                } else {
//                    Intent intent = new Intent(getActivity(), ProfileActivity.class);
//                    startActivity(intent);
//                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                    getActivity().finish();
//                }
//            }
//        });


        userProfileCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnection();
                if (!isConnected) {
                    snackBar(isConnected);
                } else {
                    Intent intent=new Intent(getActivity(), ProfileActivity.class);
                    intent.putExtra("from","profile");
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    //getActivity().finish();
                    //startActivity(new Intent(getActivity(), PlaceOrderListActivity.class).putExtra("id",id).putExtra("token",token));
                }
            }
        });

        orderHistoryCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnection();
                if (!isConnected) {
                    snackBar(isConnected);
                } else {
                Intent intent=new Intent(getActivity(), PlaceOrderListActivity.class);
                intent.putExtra("from","profile");
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                //getActivity().finish();
                //startActivity(new Intent(getActivity(), PlaceOrderListActivity.class).putExtra("id",id).putExtra("token",token));
            }
            }
        });

        paymentHistoryCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnection();
                if (!isConnected) {
                    snackBar(isConnected);
                } else {
                    Intent intent = new Intent(getActivity(), PaymentHistoryActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
        });

        refundSettlementCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnection();
                if (!isConnected) {
                    snackBar(isConnected);
                } else {
                    Intent intent = new Intent(getActivity(), RefundSettlementActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    getActivity().finish();
                }
            }
        });

        sendInvitationCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RewardsActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                getActivity().finish();
            }
        });

        changePasswordCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnection();
                if (!isConnected) {
                    snackBar(isConnected);
                } else {
                    Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
                    intent.putExtra("token", token);
                    intent.putExtra("activity", "profile");
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                /*startActivity(new Intent(getActivity(), ChangePasswordActivity.class).putExtra("id",id)
                        .putExtra("token",token).putExtra("activity","profile"));*/
                }
            }
        });


        return view;
    }

    private void init(View view) {
        rootLayout=view.findViewById(R.id.fragment_profile_rootLayout);
        intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        connectivityReceiver = new ConnectivityReceiver();
        navIcon=view.findViewById(R.id.navIcon);
        upAnimation=AnimationUtils.loadAnimation(getContext(),R.anim.slide_up);
        downAnimation=AnimationUtils.loadAnimation(getContext(),R.anim.slide_down);
        username = view.findViewById(R.id.userName);
        mAvatarImageView = view.findViewById(R.id.userImageIV);
        phoneNo = view.findViewById(R.id.userPhoneNo);
        bottomRL=view.findViewById(R.id.bottomRL);
        bottomRL.setAnimation(upAnimation);
        checkBalanceLayout=view.findViewById(R.id.balanceLayout);
        addressLayout=view.findViewById(R.id.addressLayout);
        profileLayout=view.findViewById(R.id.profileLayout);
        orderHistoryCV=view.findViewById(R.id.orderHistoryCV);
        userProfileCV=view.findViewById(R.id.userProfileCV);
        paymentHistoryCV=view.findViewById(R.id.paymentHistoryCV);
        refundSettlementCV=view.findViewById(R.id.refundSettlementCV);
        sendInvitationCV=view.findViewById(R.id.sendInvitationCV);
        changePasswordCV=view.findViewById(R.id.changePasswordCV);
        sharedPreferences = getContext().getSharedPreferences("MyRef", MODE_PRIVATE);

        mAppBarLayout = view.findViewById(R.id.appBarLayout);
        anim_toolbar = view.findViewById(R.id.anim_toolbar);
        mToolbarTextView = view.findViewById(R.id.toolbar_title);
        mSpace = view.findViewById(R.id.space);
        mTitleTextSize = username.getTextSize();
        setUpToolbar();
        setUpAmazingAvatar();
    }

    private void setUpToolbar() {
        mAppBarLayout.getLayoutParams().height = Utils.getDisplayMetrics(requireContext()).widthPixels * 9 / 16;
        mAppBarLayout.requestLayout();
    }

    private void setUpAmazingAvatar() {
        mAppBarStateChangeListener = new AppBarStateChangeListener() {

            @Override
            public void onStateChanged(AppBarLayout appBarLayout,
                                       AppBarStateChangeListener.State state) {
            }

            @Override
            public void onOffsetChanged(AppBarStateChangeListener.State state, float offset) {
                translationView(offset);
            }
        };
        mAppBarLayout.addOnOffsetChangedListener(mAppBarStateChangeListener);
    }

    private void translationView(float offset) {
//        float newAvatarSize = Utils.convertDpToPixel(EXPAND_AVATAR_SIZE_DP - (EXPAND_AVATAR_SIZE_DP - COLLAPSED_AVATAR_SIZE_DP) * offset, requireActivity());
//        float expandAvatarSize = Utils.convertDpToPixel(EXPAND_AVATAR_SIZE_DP, requireActivity());
//        float xAvatarOffset = (mSpacePoint[0] - mAvatarPoint[0] - (expandAvatarSize - newAvatarSize) / 2f) * offset;
//        // If avatar center in vertical, just half `(expandAvatarSize - newAvatarSize)`
//        float yAvatarOffset = (mSpacePoint[1] - mAvatarPoint[1] - (expandAvatarSize - newAvatarSize)) * offset;
//        mAvatarImageView.getLayoutParams().width = Math.round(newAvatarSize);
//        mAvatarImageView.getLayoutParams().height = Math.round(newAvatarSize);
//        mAvatarImageView.setTranslationX(xAvatarOffset);
//        mAvatarImageView.setTranslationY(yAvatarOffset);
//        Log.e("AvatarSize===>", newAvatarSize + " : " + expandAvatarSize);

        float newTextSize = mTitleTextSize - (mTitleTextSize - mToolbarTextView.getTextSize()) * offset;
        Paint paint = new Paint(username.getPaint());
        paint.setTextSize(newTextSize);
        float newTextWidth = Utils.getTextWidth(paint, username.getText().toString());
        paint.setTextSize(mTitleTextSize);
        float originTextWidth = Utils.getTextWidth(paint, username.getText().toString());
        // If rtl should move title view to end of view.
        boolean isRTL = TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) ==
                View.LAYOUT_DIRECTION_RTL ||
                rootLayout.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
        float xTitleOffset = ((mToolbarTextPoint[0] + (isRTL ? mToolbarTextView.getWidth() : 0)) -
                (mTitleTextViewPoint[0] + (isRTL ? username.getWidth() : 0)) -
                (mToolbarTextView.getWidth() > newTextWidth ?
                        (originTextWidth - newTextWidth) / 2f : 0)) * offset;
        float yTitleOffset = (mToolbarTextPoint[1] - mTitleTextViewPoint[1]) * offset;
        username.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
        username.setTranslationX(xTitleOffset);
        username.setTranslationY(yTitleOffset);
    }

    private void resetPoints(boolean isTextChanged) {
        final float offset = mAppBarStateChangeListener.getCurrentOffset();

        float newAvatarSize = Utils.convertDpToPixel(
                EXPAND_AVATAR_SIZE_DP - (EXPAND_AVATAR_SIZE_DP - COLLAPSED_AVATAR_SIZE_DP) * offset,
                requireActivity());
        float expandAvatarSize = Utils.convertDpToPixel(EXPAND_AVATAR_SIZE_DP, requireActivity());

        int[] avatarPoint = new int[2];
        mAvatarImageView.getLocationOnScreen(avatarPoint);
        mAvatarPoint[0] = avatarPoint[0] - mAvatarImageView.getTranslationX() -
                (expandAvatarSize - newAvatarSize) / 2f;
        // If avatar center in vertical, just half `(expandAvatarSize - newAvatarSize)`
        mAvatarPoint[1] = avatarPoint[1] - mAvatarImageView.getTranslationY() -
                (expandAvatarSize - newAvatarSize);

        int[] spacePoint = new int[2];
        mSpace.getLocationOnScreen(spacePoint);
        mSpacePoint[0] = spacePoint[0];
        mSpacePoint[1] = spacePoint[1];

        int[] toolbarTextPoint = new int[2];
        mToolbarTextView.getLocationOnScreen(toolbarTextPoint);
        mToolbarTextPoint[0] = toolbarTextPoint[0];
        mToolbarTextPoint[1] = toolbarTextPoint[1];

        Paint paint = new Paint(username.getPaint());
        float newTextWidth = Utils.getTextWidth(paint, username.getText().toString());
        paint.setTextSize(mTitleTextSize);
        float originTextWidth = Utils.getTextWidth(paint, username.getText().toString());
        int[] titleTextViewPoint = new int[2];
        username.getLocationOnScreen(titleTextViewPoint);
        mTitleTextViewPoint[0] = titleTextViewPoint[0] - username.getTranslationX() -
                (mToolbarTextView.getWidth() > newTextWidth ?
                        (originTextWidth - newTextWidth) / 2f : 0);
        mTitleTextViewPoint[1] = titleTextViewPoint[1] - username.getTranslationY();

        if (isTextChanged) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    translationView(offset);
                }
            });
        }
    }

    @Override
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
        super.onMultiWindowModeChanged(isInMultiWindowMode);
        if (!isInMultiWindowMode) {
            return;
        }
        resetPoints(false);
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

/*
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
    }*/
}