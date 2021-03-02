package com.hydertechno.mulven.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hydertechno.mulven.Api.Config;
import com.hydertechno.mulven.Models.Sliderimage;
import com.hydertechno.mulven.R;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HomePageSliderAdapter extends SliderViewAdapter<HomePageSliderAdapter.Holder> {
    private List<Sliderimage> image;
    private String [] imageList;
    private Context context;

    public HomePageSliderAdapter(List<Sliderimage> image) {
        this.image = image;
    }

    public HomePageSliderAdapter(String[] imageList) {
        this.imageList = imageList;
    }

    public HomePageSliderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public HomePageSliderAdapter.Holder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_page_image_slider_design, parent, false);
        return new HomePageSliderAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(HomePageSliderAdapter.Holder viewHolder, int position) {
        Sliderimage imageList = image.get(position);
        Picasso.get().load(Config.IMAGE_LINE+imageList.getSlider_image())
                .into(viewHolder.sliderImage, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {}
                    @Override
                    public void onError(Exception e) {
                        Log.d("kiKahini", e.getMessage());
                    }
                });
    }

    @Override
    public int getCount() {
        return image.size();
    }

    public class Holder extends SliderViewAdapter.ViewHolder {
        private ImageView sliderImage;
        public Holder(View itemView) {
            super(itemView);
            sliderImage=itemView.findViewById(R.id.sliderImage);
        }
    }
}
