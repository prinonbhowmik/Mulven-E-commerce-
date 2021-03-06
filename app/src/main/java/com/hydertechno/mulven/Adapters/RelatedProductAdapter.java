package com.hydertechno.mulven.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.hydertechno.mulven.Activities.CampaignProductActivity;
import com.hydertechno.mulven.Activities.ProductDetailsActivity;
import com.hydertechno.mulven.Api.Config;
import com.hydertechno.mulven.Internet.ConnectivityReceiver;
import com.hydertechno.mulven.Models.CategoriesModel;
import com.hydertechno.mulven.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RelatedProductAdapter extends RecyclerView.Adapter<RelatedProductAdapter.ViewHolder> implements ConnectivityReceiver.ConnectivityReceiverListener {

    private List<CategoriesModel> categoriesModelList;
    private Context context;
    private int limit = 8;
    private Snackbar snackbar;
    private boolean isConnected;
    private ConnectivityReceiver connectivityReceiver;
    private IntentFilter intentFilter;
    public ProductDetailsActivity activity;

    public RelatedProductAdapter(List<CategoriesModel> categoriesModelList, Context context) {
        this.categoriesModelList = categoriesModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_layout_design, parent, false);
        return new RelatedProductAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoriesModel model=categoriesModelList.get(position);
        int unitPrice=model.getUnit_price();
        holder.productUnitPrice.setText("??? "+unitPrice);
        int mrpPrice = model.getMrp_price() != null ? model.getMrp_price() : 0;
        holder.productName.setText(model.getProduct_name());
        try{
            Picasso.get()
                    .load(Config.IMAGE_LINE+model.getFeacher_image())
                    .into(holder.productImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(mrpPrice==0 || mrpPrice==unitPrice){
            holder.productMRPPrice.setVisibility(View.GONE);
        }else {
            holder.productMRPPrice.setVisibility(View.VISIBLE);
            holder.productMRPPrice.setText(String.valueOf(mrpPrice));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkConnection();
                if (!isConnected) {
                    toastShow(isConnected);
                } else {
                    try {
                        Intent intent = new Intent(context, ProductDetailsActivity.class);
                        intent.putExtra("id", model.getId());
                        intent.putExtra("from", "regular");
                        intent.putExtra("sku", model.getSku());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } catch (Exception e) {
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if (categoriesModelList.size()>limit){
            return limit;
        }else {
            return categoriesModelList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView productImage;
        private TextView productUnitPrice,productName,productMRPPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage=itemView.findViewById(R.id.productImage);
            productName=itemView.findViewById(R.id.productName);
            productUnitPrice=itemView.findViewById(R.id.productUnitPrice);
            productMRPPrice=itemView.findViewById(R.id.productMRPPrice);
            productMRPPrice.setPaintFlags(productMRPPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        toastShow(isConnected);
    }
    private void toastShow(boolean isConnected) {
        if(!isConnected) {
            snackbar = Snackbar.make(activity.rootLayout, "No Internet Connection! Please Try Again.", Snackbar.LENGTH_INDEFINITE);
            snackbar.setDuration(5000);
            snackbar.setActionTextColor(Color.WHITE);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(Color.RED);
            snackbar.show();
        }
    }


    private void checkConnection() {
        isConnected = ConnectivityReceiver.isConnected();
    }
}
