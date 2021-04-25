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
import android.widget.ImageView;
import android.widget.Toast;

import com.hydertechno.mulven.Api.ApiUtils;
import com.hydertechno.mulven.Api.Config;
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

public class ProfileActivity extends AppCompatActivity {
    private FrameLayout frameLayout;
    private CircleImageView profileImageTV;
    private EditText nameET,addressET,emailET,phoneET;
    private Uri imageUri;
    private ImageView saveIcon;
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
                CropImage.activity()
                        .setFixAspectRatio(true)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .start(ProfileActivity.this);
            }
        });

        saveIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (imageUri!=null){
                    File file = new File(imageUri.getPath());

                    RequestBody userImage = RequestBody.create(MediaType.parse("image/*"), file);

                    MultipartBody.Part photo = MultipartBody.Part.createFormData("image", file.getName(), userImage);

                    RequestBody  fullName = RequestBody .create(MediaType.parse("text/plain"), nameET.getText().toString());
                    RequestBody  email = RequestBody .create(MediaType.parse("text/plain"), emailET.getText().toString());
                    RequestBody  address = RequestBody .create(MediaType.parse("text/plain"), addressET.getText().toString());
                    Call<UserProfile> call1 = ApiUtils.getUserService().updateProfileDataWithImage(token, photo,fullName,email,address);
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

                        }
                    });
                }else {
                    Call<UserProfile> call1 = ApiUtils.getUserService().updateProfileData(token, nameET.getText().toString(),
                            emailET.getText().toString(), addressET.getText().toString());
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

                        }
                    });
                }
            }
        });
    }

    private void init() {
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
}