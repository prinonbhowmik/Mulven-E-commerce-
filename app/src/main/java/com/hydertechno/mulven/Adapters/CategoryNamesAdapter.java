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
import com.hydertechno.mulven.Api.Config;
import com.hydertechno.mulven.Models.CategoryNamesModel;
import com.hydertechno.mulven.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryNamesAdapter extends RecyclerView.Adapter<CategoryNamesAdapter.ViewHolder> {
    private List<CategoryNamesModel> categoryNamesModelList;
    private Context context;

    public CategoryNamesAdapter(List<CategoryNamesModel> categoryNamesModelList, Context context) {
        this.categoryNamesModelList = categoryNamesModelList;
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
        CategoryNamesModel model= categoryNamesModelList.get(position);
        holder.categoryName.setText(firstWord(model.getCategory_name()));

            try {
                Picasso.get()
                        .load(Config.IMAGE_LINE+model.getImage())
                        .into(holder.imageIV);
            } catch (Exception e) {
                e.printStackTrace();
            }



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, SeeAllProductsActivity.class);
                intent.putExtra("id",""+model.getId());
                intent.putExtra("title",""+model.getCategory_name());
                context.startActivity(intent);
            }
        });
    }

    public static String firstWord(String input) {

        for(int i = 0; i < input.length(); i++)
        {
            if(input.charAt(i) == ' ')
            {
                return input.substring(0, i);
            }
        }

        return input;
    }

    @Override
    public int getItemCount() {
        return categoryNamesModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView categoryName;
        private ImageView imageIV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName=itemView.findViewById(R.id.categoryName);
            imageIV=itemView.findViewById(R.id.imageIV);

        }
    }

}
