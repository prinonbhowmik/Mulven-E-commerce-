package com.hydertechno.mulven.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.hydertechno.mulven.R;

public class RefundRequestActivity extends AppCompatActivity {
    private RecyclerView requestRecycler;
    private EditText commentBox;
    private TextView applyRequestTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund_request);
        init();
    }
    private void init() {
        requestRecycler=findViewById(R.id.refundMethodRecycler);
        commentBox=findViewById(R.id.refundCommentET);
        applyRequestTV=findViewById(R.id.applyRequestTV);

    }
}