package com.hydertechno.mulven.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.hydertechno.mulven.Api.ApiUtils;
import com.hydertechno.mulven.Models.UserProfile;
import com.hydertechno.mulven.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    private FrameLayout frameLayout;
    private CircleImageView profileImageTV;
    private EditText nameET,addressET,emailET,phoneET;
    private Uri imageUri;
    private SharedPreferences sharedPreferences;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();

        token = sharedPreferences.getString("token",null);
        Call<UserProfile> call = ApiUtils.getUserService().getUserData(token);
        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if (response.isSuccessful()){
                    nameET.setText(response.body().getFull_name());
                    phoneET.setText(response.body().getPhone());
                    addressET.setText(response.body().getAddress());
                    emailET.setText(response.body().getEmail());
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {

            }
        });


        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setFixAspectRatio(true)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .start(ProfileActivity.this);
            }
        });
    }

    private void init() {
        frameLayout=findViewById(R.id.frame_layout1);
        profileImageTV=findViewById(R.id.profileImageTV);
        sharedPreferences = getSharedPreferences("MyRef", MODE_PRIVATE);
        nameET = findViewById(R.id.nameET);
        addressET = findViewById(R.id.addressET);
        emailET = findViewById(R.id.emailET);
        phoneET = findViewById(R.id.phoneET);
    }

    public void profileBack(View view) {
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
}