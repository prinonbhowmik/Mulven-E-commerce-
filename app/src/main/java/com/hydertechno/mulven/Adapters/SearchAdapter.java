package com.hydertechno.mulven.Adapters;

import android.content.Context;
import android.content.Intent;
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

import com.hydertechno.mulven.Activities.ProductDetailsActivity;
import com.hydertechno.mulven.Api.Config;
import com.hydertechno.mulven.Models.CategoriesModel;
import com.hydertechno.mulven.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private List<CategoriesModel> categoriesModelList;
    private List<CategoriesModel> categoriesModelFiltered;
    private Context context;


    public SearchAdapter(List<CategoriesModel> categoriesModelList, Context context) {
        this.categoriesModelList = categoriesModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_product_layout_design, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoriesModel model=categoriesModelList.get(position);
        int unitPrice=model.getUnit_price();
        holder.productUnitPrice.setText("৳ "+unitPrice);
        holder.productName.setText(model.getProduct_name());
        try{
            Picasso.get()
                    .load(Config.IMAGE_LINE+model.getFeacher_image())
                    .into(holder.productImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int mrpPrice = model.getMrp_price() != null ? model.getMrp_price() : 0;
        if(mrpPrice==0 || mrpPrice==unitPrice){
            holder.productMRPPrice.setVisibility(View.GONE);
        } else {
            holder.productMRPPrice.setText(String.valueOf(mrpPrice));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(context, ProductDetailsActivity.class);
                    intent.putExtra("id",model.getId());
                    intent.putExtra("from","regular");
                    intent.putExtra("sku",model.getSku());
                    Log.d("productId", String.valueOf(model.getId()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    /*    ((Activity)context).finish();*/
                } catch (Exception e) {
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
            List<CategoriesModel> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(categoriesModelFiltered);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (CategoriesModel item : categoriesModelList) {
                    if (item.getProduct_name().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            categoriesModelList.clear();
            categoriesModelList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    @Override
    public int getItemCount() {
        return categoriesModelList.size();
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
}
