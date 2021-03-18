package com.hydertechno.mulven.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hydertechno.mulven.Models.ProductFeature;
import com.hydertechno.mulven.R;

import java.util.List;

public class ProductFeatureAdapter extends RecyclerView.Adapter<ProductFeatureAdapter.ViewHolder> {

    private List<ProductFeature> productFeatureList;
    private Context context;

    public ProductFeatureAdapter(List<ProductFeature> productFeatureList, Context context) {
        this.productFeatureList = productFeatureList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feature_layout_design, parent, false);
        return new ProductFeatureAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductFeature feature=productFeatureList.get(position);
        holder.featureTV.setText(feature.getFeature_name());
    }

    @Override
    public int getItemCount() {
        return productFeatureList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView featureTV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            featureTV=itemView.findViewById(R.id.featureTV);
        }
    }
}
