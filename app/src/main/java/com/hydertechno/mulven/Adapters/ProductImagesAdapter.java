package com.hydertechno.mulven.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hydertechno.mulven.Api.Config;
import com.hydertechno.mulven.Interface.ProductImageClickInterface;
import com.hydertechno.mulven.Models.ImageGallery;
import com.hydertechno.mulven.Models.ProductImagesModel;
import com.hydertechno.mulven.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductImagesAdapter extends RecyclerView.Adapter<ProductImagesAdapter.ViewHolder> {
    private List<ImageGallery> productImagesModelList;
    private ProductImageClickInterface productImageClickInterface;

    public ProductImagesAdapter(List<ImageGallery> productImagesModelList, ProductImageClickInterface productImageClickInterface) {
        this.productImagesModelList = productImagesModelList;
        this.productImageClickInterface = productImageClickInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_images_layout_design, parent, false);
        return new ProductImagesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ImageGallery model=productImagesModelList.get(position);
        try{
            Picasso.get()
                    .load(Config.IMAGE_LINE+model.getImage_name())
                    .into(holder.imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.getImage_name()!=null) {
                    productImageClickInterface.OnClick(model.getImage_name());
                }
            }
        });
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
