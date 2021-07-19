package com.hydertechno.mulven.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.hydertechno.mulven.Api.ApiUtils;
import com.hydertechno.mulven.Fragments.LoadingDialog;
import com.hydertechno.mulven.Models.InvoiceDetailsModel;
import com.hydertechno.mulven.Models.PostRefundSettlementResponse;
import com.hydertechno.mulven.Models.RequestReportBody;
import com.hydertechno.mulven.R;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.Objects;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportIssueBottomSheet extends BottomSheetDialogFragment implements View.OnClickListener {
    private static final String TAG = ReportIssueBottomSheet.class.getSimpleName();

    View view;
    private String OrderId;
    private AutoCompleteTextView reportMenu;
    private EditText reportIssue;
    private TextView submitBtn;

    private SharedPreferences sharedPreferences;
    private String token;
    private int status;
    private String selectedType = null;


    private LoadingDialog loadingDialog;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.report_issue_bottom_sheet, container, false);
        init(view);
        Bundle mArgs = getArguments();
        OrderId = mArgs.getString("orderId");
        status = mArgs.getInt("status");
        reportIssue.setText("Invoice Id :"+OrderId);
        return view;
    }

    private void init(View view) {
        submitBtn = view.findViewById(R.id.submitRefund);
        reportMenu = view.findViewById(R.id.reportMenu);
        reportIssue = view.findViewById(R.id.report_issueET);

        sharedPreferences = requireContext().getSharedPreferences("MyRef", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", null);

        submitBtn.setOnClickListener(this);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadingDialog = LoadingDialog.instance();

        setAdapter();
    }

    private void setAdapter() {
        String[] issueTypes = new String[0];
        if(status==1) {
            issueTypes = getResources().getStringArray(R.array.issue_type);
        }else {
            issueTypes = getResources().getStringArray(R.array.issue_type2);
        }
        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, issueTypes);
        reportMenu.setAdapter(mAdapter);
        String[] finalIssueTypes = issueTypes;
        reportMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedType = finalIssueTypes[position];
                Log.e(TAG, finalIssueTypes[position]);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (isValid()) {
            postReportIssue();
        }
    }

    private boolean isValid() {
        if (selectedType == null) {
            reportMenu.setError("Please select a type!");
            return false;
        }

        if (reportIssue.getText().toString().isEmpty()) {
            reportIssue.setError("Required!");
            return false;
        }

        return true;
    }

    private void postReportIssue() {
        if (!loadingDialog.isAdded())
            loadingDialog.show(getChildFragmentManager(), null);

        RequestReportBody body = new RequestReportBody();
        body.setOrderId(OrderId);
        body.setIssueType(selectedType);
        body.setDescription(reportIssue.getText().toString());

        Call<PostRefundSettlementResponse> call = ApiUtils.getUserService().postReportIssue(token, body);
        call.enqueue(new Callback<PostRefundSettlementResponse>() {
            @Override
            public void onResponse(Call<PostRefundSettlementResponse> call, Response<PostRefundSettlementResponse> response) {
                loadingDialog.dismiss();
                if (response.isSuccessful() && response.code() == 200) {
                    if (response.body().getStatus().equals("1")) {
                        Toasty.success(requireContext(), response.body().getMessage(), Toasty.LENGTH_LONG).show();
                        ReportIssueBottomSheet.this.dismiss();
                    } else {
                        Toasty.error(requireContext(), response.body().getMessage(), Toasty.LENGTH_LONG).show();
                    }
                } else {
                    Toasty.error(requireContext(), "Error with: " + response.code(), Toasty.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<PostRefundSettlementResponse> call, Throwable t) {
                loadingDialog.dismiss();
                Toasty.error(requireContext(), Objects.requireNonNull(t.getMessage()), Toasty.LENGTH_LONG).show();
            }
        });
    }
}
