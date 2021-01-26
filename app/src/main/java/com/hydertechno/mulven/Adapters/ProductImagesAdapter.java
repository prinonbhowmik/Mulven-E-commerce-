package com.hydertechno.mulven.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.hydertechno.mulven.Models.CategoriesModel;
import com.hydertechno.mulven.Models.ProductImagesModel;
import com.hydertechno.mulven.R;

import java.util.List;

public class ProductImagesAdapter extends RecyclerView.Adapter<ProductImagesAdapter.ViewHolder> {
    private List<ProductImagesModel> productImagesModelList;
    private Context context;

    public ProductImagesAdapter(List<ProductImagesModel> productImagesModelList, Context context) {
        this.productImagesModelList = productImagesModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_images_layout_design, parent, false);
        return new ProductImagesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductImagesModel model=productImagesModelList.get(position);
        holder.imageView.setImageResource(model.getImage());
    }

    @Override
    public int getItemCount() {
        return productImagesModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageIv);
        }
    }
}
