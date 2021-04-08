package com.hydertechno.mulven.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.hydertechno.mulven.Api.Config;
import com.hydertechno.mulven.Models.OrderItemsModel;
import com.hydertechno.mulven.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderItemsAdapter extends RecyclerView.Adapter<OrderItemsAdapter.ViewHolder> {
    private List<OrderItemsModel> orderItemsModelList;
    private Context context;

    public OrderItemsAdapter(List<OrderItemsModel> orderItemsModelList, Context context) {
        this.orderItemsModelList = orderItemsModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.ordered_product_description_design,parent,false);
        return new OrderItemsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderItemsModel model=orderItemsModelList.get(position);
        holder.OrderProductName.setText(model.getPro_name());
        String variant=model.getVariant();
        String color=model.getColor();
        String size=model.getSize();
        if (variant != null || color!=null || size!=null){
            holder.ProductVariantName.setVisibility(View.VISIBLE);
            holder.ProductVariantName.setText(color+size+variant);
        }
        int price=model.getPrice();
        int quantity=model.getQuantity();
        int totalPrice=price*quantity;
        holder.OderProductPrice.setText("৳ "+ price);
        holder.OrderProductQuantity.setText(" X "+quantity);
        holder.OrderProductTotalPrice.setText("৳ "+ totalPrice);

        try{
            Picasso.get()
                    .load(Config.IMAGE_LINE+model.getFeacher_image())
                    .into(holder.OrderProductImageIV);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String orderStatus=model.getStatus();
        switch (orderStatus) {
            case "Pending":
                holder.orderStatusTV.setBackground(ContextCompat.getDrawable(context, R.drawable.status_pending));
                break;
            case "Processing":
                holder.orderStatusTV.setBackground(ContextCompat.getDrawable(context, R.drawable.status_processing));
                break;
            case "Cancel":
                //holder.orderStatusTV.setBackgroundColor(ContextCompat.getColor(context,R.color.GRed));
                holder.orderStatusTV.setBackground(ContextCompat.getDrawable(context, R.drawable.status_cancel));
                break;
            case "Delivered":
                holder.orderStatusTV.setBackground(ContextCompat.getDrawable(context, R.drawable.status_delivered));
                break;
            case "Partial Paid":
                holder.orderStatusTV.setBackground(ContextCompat.getDrawable(context, R.drawable.status_partial_paid));
                break;
            case "Shipped":
                holder.orderStatusTV.setBackground(ContextCompat.getDrawable(context, R.drawable.status_shipped));
                break;
        }
        holder.orderStatusTV.setText(model.getStatus());

    }

    @Override
    public int getItemCount() {
        return orderItemsModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView OrderProductImageIV;
        private TextView OrderProductName,ProductVariantName,OderProductPrice,OrderProductQuantity,OrderProductTotalPrice,orderStatusTV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            OrderProductImageIV=itemView.findViewById(R.id.OrderProductImageIV);
            OrderProductName=itemView.findViewById(R.id.OrderProductName);
            ProductVariantName=itemView.findViewById(R.id.ProductVariantName);
            OderProductPrice=itemView.findViewById(R.id.OderProductPrice);
            OrderProductQuantity=itemView.findViewById(R.id.OrderProductQuantity);
            OrderProductTotalPrice=itemView.findViewById(R.id.OrderProductTotalPrice);
            orderStatusTV=itemView.findViewById(R.id.OrderStatusTV);
        }
    }
}
