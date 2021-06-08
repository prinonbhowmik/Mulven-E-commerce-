package com.hydertechno.mulven.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hydertechno.mulven.Interface.OnPMethodItemClickListener;
import com.hydertechno.mulven.Models.PaymentMethodModel;
import com.hydertechno.mulven.R;

import java.util.List;

public class PaymentMethodsAdapter extends RecyclerView.Adapter<PaymentMethodsAdapter.ViewHolder> {

    private List<PaymentMethodModel> dataList;
    private OnPMethodItemClickListener listener;

    public PaymentMethodsAdapter(List<PaymentMethodModel> dataList, OnPMethodItemClickListener listener) {
        this.dataList = dataList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment_method,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PaymentMethodModel item = dataList.get(position);

        holder.itemImage.setImageResource(item.getImage());
        holder.itemTitle.setText(item.getTitle());
        holder.itemDescription.setText(item.getDescription());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView itemImage;
        TextView itemTitle;
        TextView itemDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.item_image);
            itemTitle = itemView.findViewById(R.id.item_title);
            itemDescription = itemView.findViewById(R.id.item_description);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            PaymentMethodModel item = dataList.get(getAdapterPosition());
            listener.onClick(item);
        }
    }
}
