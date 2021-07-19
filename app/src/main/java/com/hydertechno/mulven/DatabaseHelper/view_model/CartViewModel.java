package com.hydertechno.mulven.DatabaseHelper.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hydertechno.mulven.DatabaseHelper.Database_Helper;
import com.hydertechno.mulven.Models.CartProductModel;

import java.util.ArrayList;

public class CartViewModel extends AndroidViewModel {
    MutableLiveData<ArrayList<CartProductModel>> liveData;
    Database_Helper databaseHelper;

    public CartViewModel(@NonNull Application application) {
        super(application);
        databaseHelper = new Database_Helper(application);
    }

    public MutableLiveData<ArrayList<CartProductModel>> getAllProduct() {
//        if (liveData != null) {
//            return liveData;
//        }

        liveData = new MutableLiveData<>();
        ArrayList<CartProductModel> allCartProducts = databaseHelper.getAllCartProducts();
        liveData.setValue(allCartProducts);

        return liveData;
    }
}
