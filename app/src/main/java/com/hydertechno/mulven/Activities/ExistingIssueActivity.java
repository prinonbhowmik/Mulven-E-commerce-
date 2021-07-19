package com.hydertechno.mulven.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hydertechno.mulven.Adapters.ExistingIssueListAdapter;
import com.hydertechno.mulven.Adapters.NotificationAdapter;
import com.hydertechno.mulven.Api.ApiUtils;
import com.hydertechno.mulven.Models.ExistingIssueModel;
import com.hydertechno.mulven.Models.NotificationModel;
import com.hydertechno.mulven.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExistingIssueActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private SharedPreferences sharedPreferences;
    private RelativeLayout progressRL;
    private String  token, orderId;
    private RecyclerView existingIssueRecycler;
    private ExistingIssueListAdapter adapter;
    private List<ExistingIssueModel> modelList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existing_issue);
        init();
        getReports();

    }

    private void getReports() {
        progressRL.setVisibility(View.VISIBLE);
        Call<List<ExistingIssueModel>> call = ApiUtils.getUserService().getReportIssue(token, orderId);
        call.enqueue(new Callback<List<ExistingIssueModel>>() {
            @Override
            public void onResponse(Call<List<ExistingIssueModel>> call, Response<List<ExistingIssueModel>> response) {
                progressRL.setVisibility(View.GONE);
                if (response.isSuccessful() && response.code()==200){
                    if(response.body()!=null && response.body().size()>0){
                        adapter=new ExistingIssueListAdapter(response.body(),ExistingIssueActivity.this);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(ExistingIssueActivity.this,LinearLayoutManager.VERTICAL,false);
                        existingIssueRecycler.setLayoutManager(layoutManager);
                        existingIssueRecycler.setAdapter(adapter);
                    }
                }else
                    Toasty.error(ExistingIssueActivity.this, "Something went wrong!", Toasty.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<List<ExistingIssueModel>> call, Throwable t) {
                progressRL.setVisibility(View.GONE);
                Toasty.error(ExistingIssueActivity.this, Objects.requireNonNull(t.getMessage()), Toasty.LENGTH_LONG).show();
            }
        });
    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        progressRL=findViewById(R.id.progressRL);
        existingIssueRecycler=findViewById(R.id.existingIssueRecycler);
        sharedPreferences = getSharedPreferences("MyRef", MODE_PRIVATE);
        token = sharedPreferences.getString("token",null);
        orderId = getIntent().getStringExtra("orderId");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}