package com.hydertechno.mulven.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hydertechno.mulven.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

public class HomePageSliderAdapter extends SliderViewAdapter<HomePageSliderAdapter.Holder> {
    private int[] image;

    public HomePageSliderAdapter(int[] image) {
        this.image = image;
    }

    @Override
    public HomePageSliderAdapter.Holder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_page_image_slider_design, parent, false);
        return new HomePageSliderAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(HomePageSliderAdapter.Holder viewHolder, int position) {
        viewHolder.sliderImage.setImageResource(image[position]);
    }

    @Override
    public int getCount() {
        return image.length;
    }

    public class Holder extends SliderViewAdapter.ViewHolder {
        private ImageView sliderImage;
        public Holder(View itemView) {
            super(itemView);
            sliderImage=itemView.findViewById(R.id.sliderImage);
        }
    }
}
