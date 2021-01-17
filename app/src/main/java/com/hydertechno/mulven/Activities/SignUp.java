package com.hydertechno.mulven.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hydertechno.mulven.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SignUp extends AppCompatActivity {
    private TextInputLayout nameTIL, dobTIL, passTIL;
    private TextInputEditText nameTIET, dobTIET, passTIET;
    private Button signUpBtn;
    private String currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();
        dobTIET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate();
            }
        });

    }

    private void init() {
        nameTIL = findViewById(R.id.name_LT);
        dobTIL = findViewById(R.id.date_LT);
        nameTIET = findViewById(R.id.name_Et);
        dobTIET = findViewById(R.id.date_ET);
        signUpBtn = findViewById(R.id.sign_upBtn);

    }

    private void getDate() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                currentDate = day + "-" + month + "-" + year;
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                Date date = null;

                try {
                    date = dateFormat.parse(currentDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                dobTIET.setText(dateFormat.format(date));
            }
        };

        Calendar calendar = Calendar.getInstance();
       // calendar.add(Calendar.YEAR, -18);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, dateSetListener, year, month, day);

        datePickerDialog.show();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialog2 = new AlertDialog.Builder(this);
        dialog2.setTitle("Exit!");
        dialog2.setIcon(R.drawable.applogo);
        dialog2.setMessage("Do you want to exit this page?");
        dialog2.setCancelable(false);
        dialog2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog2, int which) {
                finish();
            }
        });
        dialog2.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog2, int which) {
                dialog2.dismiss();
            }
        });
        AlertDialog alertDialog2 = dialog2.create();
        alertDialog2.show();

    }
}