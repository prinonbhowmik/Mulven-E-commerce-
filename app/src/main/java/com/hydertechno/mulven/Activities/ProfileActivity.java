package com.hydertechno.mulven.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.hydertechno.mulven.Api.ApiUtils;
import com.hydertechno.mulven.Api.Config;
import com.hydertechno.mulven.Internet.Connection;
import com.hydertechno.mulven.Internet.ConnectivityReceiver;
import com.hydertechno.mulven.Models.UserProfile;
import com.hydertechno.mulven.R;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private static final String TAG = ProfileActivity.class.getSimpleName();
    private FrameLayout frameLayout;
    private CircleImageView profileImageTV;
    private EditText nameET,addressET,emailET,phoneET;
    private Uri imageUri;
    private ImageView saveIcon;
    private SharedPreferences sharedPreferences;
    private String token;
    private RelativeLayout rootLayout;
    private Snackbar snackbar;
    private boolean isConnected;
    private ConnectivityReceiver connectivityReceiver;
    private IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();
        checkConnection();
        if (!isConnected) {
            snackBar(isConnected);
        }

        token = sharedPreferences.getString("token",null);
        Call<UserProfile> call = ApiUtils.getUserService().getUserData(token);
        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if (response.isSuccessful()) {
                    nameET.setText(response.body().getFull_name());
                    phoneET.setText(response.body().getPhone());
                    addressET.setText(response.body().getAddress());
                    emailET.setText(response.body().getEmail());
                    if (response.body().getUser_photo() != null) {
                        try {
                            Picasso.get()
                                    .load(Config.IMAGE_LINE + response.body().getUser_photo())
                                    .into(profileImageTV);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {

            }
        });


        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkConnection();
                if (!isConnected) {
                    snackBar(isConnected);
                }else {
                    CropImage.activity()
                            .setFixAspectRatio(true)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setCropShape(CropImageView.CropShape.OVAL)
                            .start(ProfileActivity.this);
                }
            }
        });

        saveIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnection();
                if (!isConnected) {
                    snackBar(isConnected);
                } else {
                if (imageUri != null) {
                    File file = new File(imageUri.getPath());

                    Log.d("checkToken", token + " , " + imageUri.toString());

                    RequestBody userImage = RequestBody.create(MediaType.parse("image/*"), file);

                    MultipartBody.Part user_photo = MultipartBody.Part.createFormData("user_photo", file.getName(), userImage);

                    RequestBody fullName = RequestBody.create(MediaType.parse("text/plain"), nameET.getText().toString());
                    RequestBody tokenPart = RequestBody.create(MediaType.parse("text/plain"), token);
                    RequestBody email = RequestBody.create(MediaType.parse("text/plain"), emailET.getText().toString());
                    RequestBody address = RequestBody.create(MediaType.parse("text/plain"), addressET.getText().toString());
                    Call<UserProfile> call1 = ApiUtils.getUserService().updateProfileDataWithImage(tokenPart, fullName, email, address, user_photo);
                    call1.enqueue(new Callback<UserProfile>() {
                        @Override
                        public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                            if (response.isSuccessful()) {
                                String status = response.body().getStatus();
                                if (status.equals("1")) {
                                    Toasty.success(ProfileActivity.this, "Update Success!", Toast.LENGTH_SHORT, true).show();
                                    Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                                    intent.putExtra("fragment", "profile");
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                    finish();
                                } else {
                                    Toasty.error(ProfileActivity.this, "Update Failed", Toast.LENGTH_LONG, true).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<UserProfile> call, Throwable t) {

                            Toast.makeText(ProfileActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Call<UserProfile> call1 = ApiUtils.getUserService()
                            .updateProfileData(
                                    token,
                                    nameET.getText().toString(),
                                    emailET.getText().toString(),
                                    addressET.getText().toString());
                    call1.enqueue(new Callback<UserProfile>() {
                        @Override
                        public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                            Log.e(TAG, response.toString());
                            if (response.isSuccessful()) {
                                String status = response.body().getStatus();
                                Log.e(TAG, status);
                                if (status.equals("1")) {
                                    Toasty.success(ProfileActivity.this, "Update Success!", Toast.LENGTH_SHORT, true).show();
                                    Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                                    intent.putExtra("fragment", "profile");
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                    finish();
                                } else {
                                    Toasty.error(ProfileActivity.this, "Update Failed", Toast.LENGTH_LONG, true).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<UserProfile> call, Throwable t) {
                            Log.e(TAG, t.getMessage());
                        }
                    });
                }
            }
        }
        });
    }

    private void init() {
        rootLayout=findViewById(R.id.activity_profile_rootLayout);
        intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        connectivityReceiver = new ConnectivityReceiver();
        frameLayout=findViewById(R.id.frame_layout1);
        profileImageTV=findViewById(R.id.profileImageTV);
        sharedPreferences = getSharedPreferences("MyRef", MODE_PRIVATE);
        nameET = findViewById(R.id.nameET);
        saveIcon = findViewById(R.id.saveIcon);
        addressET = findViewById(R.id.addressET);
        emailET = findViewById(R.id.emailET);
        phoneET = findViewById(R.id.phoneET);
    }

    public void profileBack(View view) {
        Intent intent=new Intent( ProfileActivity.this,MainActivity.class);
        intent.putExtra("fragment","profile");
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                imageUri = resultUri;
                profileImageTV.setImageURI(imageUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(ProfileActivity.this, "Failed"+error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent( ProfileActivity.this,MainActivity.class);
        intent.putExtra("fragment","profile");
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
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
                    recreate();
                }
            });
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