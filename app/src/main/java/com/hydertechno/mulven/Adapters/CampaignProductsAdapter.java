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
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.hydertechno.mulven.Activities.CampaignProductActivity;
import com.hydertechno.mulven.Activities.ProductDetailsActivity;
import com.hydertechno.mulven.Api.Config;
import com.hydertechno.mulven.Fragments.HomeFragment;
import com.hydertechno.mulven.Internet.ConnectivityReceiver;
import com.hydertechno.mulven.Models.CategoriesModel;
import com.hydertechno.mulven.Models.ImageGalleryModel;
import com.hydertechno.mulven.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CampaignProductsAdapter extends RecyclerView.Adapter<CampaignProductsAdapter.ViewHolder> implements ConnectivityReceiver.ConnectivityReceiverListener {

    private List<CategoriesModel> categoriesModelList;
    private List<CategoriesModel> filterModelList;
    private List<ImageGalleryModel> productImagesModelList = new ArrayList<>();
    private List<CategoriesModel> categoriesModelFiltered;
    private Context context;
    private Snackbar snackbar;
    private boolean isConnected;
    private ConnectivityReceiver connectivityReceiver;
    private IntentFilter intentFilter;
    public CampaignProductActivity activity;

    public CampaignProductsAdapter(List<CategoriesModel> categoriesModelList, Context context) {
        this.categoriesModelList = categoriesModelList;
        this.filterModelList = categoriesModelList;
        this.context = context;
//        categoriesModelFiltered = new ArrayList<>(categoriesModelFiltered);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_campaign_all_product, parent, false);
        return new CampaignProductsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoriesModel model=categoriesModelList.get(position);

        int unitPrice=model.getUnit_price();
        int mrpPrice=model.getMrp_price();
        int percentage=(((mrpPrice-unitPrice)*100)/mrpPrice);
        int per=Math.round(percentage);
        holder.textViewPercentage.setText("-"+per+"%");
        holder.productUnitPrice.setText("৳ "+unitPrice);
        holder.productName.setText(model.getProduct_name());
        try{
            Picasso.get()
                    .load(Config.IMAGE_LINE+model.getFeacher_image())
                    .into(holder.productImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(model.getMrp_price()==0){
            holder.productMRPPrice.setVisibility(View.GONE);
        }
        else {
            holder.productMRPPrice.setText(""+mrpPrice);
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
                        intent.putExtra("sku", model.getSku());
                        intent.putExtra("from", "campaign");
                        Log.d("productId", String.valueOf(model.getId()));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        /*    ((Activity)context).finish();*/
                    } catch (Exception e) {
                    }
                }
            }
        });

    }

    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint == null || constraint.length() == 0) {
                categoriesModelList = filterModelList;
            } else {
                List<CategoriesModel> filteredList = new ArrayList<>();
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (CategoriesModel item : filterModelList) {
                    if (item.getProduct_name().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    } else if(item.getCategory().equals(filterPattern)) {
                        Log.e("Items==>", item.getCategory() + "==" + filterPattern);
                        filteredList.add(item);
                    }
                }
                categoriesModelList = filteredList;
            }
            FilterResults results = new FilterResults();
            results.values = categoriesModelList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            Log.e("values", results.toString());
            categoriesModelList = (ArrayList<CategoriesModel>) results.values;
            notifyDataSetChanged();
        }
    };

        @Override
    public int getItemCount() {
        return categoriesModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView productImage;
        private TextView productUnitPrice,productName,productMRPPrice,textViewPercentage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewPercentage=itemView.findViewById(R.id.textViewPercentage);
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

    public void updateData(List<CategoriesModel> list) {
        this.categoriesModelList = list;
        this.filterModelList = list;
        notifyDataSetChanged();
    }
}

