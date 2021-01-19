package com.hydertechno.mulven.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.hydertechno.mulven.R;

public class ProductDetailsActivity extends AppCompatActivity {
    private AutoCompleteTextView sizeTV,colorTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        init();
        final String[] size = new String[]{"12", "18", "24","32","40"};
        final String[] color = new String[]{"Red", "Green", "Blue","Black","White"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, R.layout.list_item, size);
        sizeTV.setText(adapter1.getItem(0).toString(),false);
        sizeTV.setAdapter(adapter1);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, R.layout.list_item, color);
        colorTV.setText(adapter2.getItem(0).toString(),false);
        colorTV.setAdapter(adapter2);
    }

    private void init() {
        sizeTV=findViewById(R.id.sizeMenu);
        colorTV=findViewById(R.id.colorMenu);
    }

    public void productDetailsBack(View view) {
        finish();
    }
}