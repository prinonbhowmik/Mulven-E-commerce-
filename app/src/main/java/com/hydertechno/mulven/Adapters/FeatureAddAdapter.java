package com.hydertechno.mulven.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.hydertechno.mulven.Api.Config;
import com.hydertechno.mulven.Models.Sliderimage;
import com.hydertechno.mulven.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FeatureAddAdapter extends RecyclerView.Adapter<FeatureAddAdapter.ViewHolder> {
    private List<Sliderimage> images;
    private Context context;

    public FeatureAddAdapter(List<Sliderimage> images, Context context) {
        this.images = images;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.feature_add_layout_design,parent,false);
        return new FeatureAddAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Sliderimage model=images.get(position);
        for(int i=0;i<=position;i++){

            try {
                Picasso.get()
                        .load(Config.IMAGE_LINE + model.getSlider_image())
                        .into(holder.featureImage);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView featureImage;
        private CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            featureImage=itemView.findViewById(R.id.featureAddIV);
            cardView=itemView.findViewById(R.id.parentCV);
        }
    }
}
