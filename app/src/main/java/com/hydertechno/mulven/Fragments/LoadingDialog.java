package com.hydertechno.mulven.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.hydertechno.mulven.R;

import java.util.Objects;

public class LoadingDialog extends Dialog {
    private String mHeaderText = null;
    private TextView mDialogTitle = null;

    public LoadingDialog(Context context) {
        super(context);
        this.setCancelable(false);
    }

    public LoadingDialog(@NonNull Context context, String mHeaderText) {
        super(context);
        this.mHeaderText = mHeaderText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(this.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.dialog_loading);
        mDialogTitle = ((TextView)findViewById( R.id.loading_dialog_title));
        if (mHeaderText != null)
            mDialogTitle.setText(this.mHeaderText);
    }
}
