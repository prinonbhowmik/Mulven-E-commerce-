package com.hydertechno.mulven.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hydertechno.mulven.Activities.SeeAllProductsActivity;
import com.hydertechno.mulven.Models.CategoriesModel;
import com.hydertechno.mulven.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {
    private List<CategoriesModel> categoriesModelList;
    private Context context;

    public CategoriesAdapter(List<CategoriesModel> categoriesModelList, Context context) {
        this.categoriesModelList = categoriesModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_layout_design, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoriesModel model=categoriesModelList.get(position);
        holder.categoryName.setText(model.getCategoriesName());
        holder.categoryIcon.setImageResource(model.getIcon());
        /*try {
            Picasso.get()
                    .load(model.getImage())
                    .into(holder.categoryIcon);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, SeeAllProductsActivity.class);
                intent.putExtra("title",""+model.getCategoriesName());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return categoriesModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView categoryIcon;
        private TextView categoryName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryIcon=itemView.findViewById(R.id.categoryIcon);
            categoryName=itemView.findViewById(R.id.categoryName);

        }
    }
}
