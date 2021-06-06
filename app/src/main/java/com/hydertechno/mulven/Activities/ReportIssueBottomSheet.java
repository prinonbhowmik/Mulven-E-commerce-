package com.hydertechno.mulven.Activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.hydertechno.mulven.R;

import org.w3c.dom.Text;

public class ReportIssueBottomSheet extends BottomSheetDialogFragment {
    View view;
    private String OrderId;
    private EditText reportIssue;
    private TextView submitBtn;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.report_issue_bottom_sheet, container, false);
        init(view);
        Bundle mArgs = getArguments();
        OrderId = mArgs.getString("orderId");
        reportIssue.setText("Invoice Id :"+OrderId);


        return view;
    }

    private void init(View view) {
        submitBtn=view.findViewById(R.id.submitRefund);
        reportIssue=view.findViewById(R.id.report_issueET);
    }
}
