package com.hydertechno.mulven.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hydertechno.mulven.Activities.ProductDetailsActivity;
import com.hydertechno.mulven.Models.CategoriesModel;
import com.hydertechno.mulven.R;

import java.util.List;

public class AllProductsAdapter extends RecyclerView.Adapter<AllProductsAdapter.ViewHolder> {

    private List<CategoriesModel> categoriesModelList;
    private Context context;

    public AllProductsAdapter(List<CategoriesModel> categoriesModelList, Context context) {
        this.categoriesModelList = categoriesModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_product_layout_design, parent, false);
        return new AllProductsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoriesModel model=categoriesModelList.get(position);
        holder.productImage.setImageResource(model.getIcon());
        holder.productPrice.setText(model.getId());
        holder.productName.setText(model.getCategoriesName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(context, ProductDetailsActivity.class);
                    context.startActivity(intent);
                    /*    ((Activity)context).finish();*/
                } catch (Exception e) {
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return categoriesModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView productImage;
        private TextView productPrice,productName,productOldPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage=itemView.findViewById(R.id.productImage);
            productName=itemView.findViewById(R.id.productName);
            productPrice=itemView.findViewById(R.id.productPrice);
            productOldPrice=itemView.findViewById(R.id.productOldPrice);
            productOldPrice.setPaintFlags(productOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }
}

