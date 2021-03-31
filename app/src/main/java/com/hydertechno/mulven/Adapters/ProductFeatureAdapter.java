package com.hydertechno.mulven.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hydertechno.mulven.Models.ProductFeatureModel;
import com.hydertechno.mulven.R;

import java.util.List;

public class ProductFeatureAdapter extends RecyclerView.Adapter<ProductFeatureAdapter.ViewHolder> {

    private List<ProductFeatureModel> productFeatureModelList;
    private Context context;

    public ProductFeatureAdapter(List<ProductFeatureModel> productFeatureModelList, Context context) {
        this.productFeatureModelList = productFeatureModelList;
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
        ProductFeatureModel feature= productFeatureModelList.get(position);
        holder.featureTV.setText(feature.getFeature_name());
    }

    @Override
    public int getItemCount() {
        return productFeatureModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView featureTV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            featureTV=itemView.findViewById(R.id.featureTV);
        }
    }
}
