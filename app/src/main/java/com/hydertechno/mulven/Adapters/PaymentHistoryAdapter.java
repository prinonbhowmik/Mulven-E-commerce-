package com.hydertechno.mulven.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hydertechno.mulven.Models.Transaction;
import com.hydertechno.mulven.R;

import java.util.List;

public class PaymentHistoryAdapter extends RecyclerView.Adapter<PaymentHistoryAdapter.ViewHolder> {
    private List<Transaction> list;
    private Context context;

    public PaymentHistoryAdapter(List<Transaction> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_history_layout_design,parent,false);
        return new PaymentHistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction transaction = list.get(position);
        holder.bindView(transaction);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView paymentTime;
        TextView paymentDescription;
        TextView paymentAmountTV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            paymentTime = itemView.findViewById(R.id.paymentTime);
            paymentDescription = itemView.findViewById(R.id.paymentDescription);
            paymentAmountTV = itemView.findViewById(R.id.paymentAmountTV);
        }

        void bindView(Transaction item) {
            paymentTime.setText(item.getCreatedAt());
            paymentDescription.setText(item.getNotice());
            paymentAmountTV.setText(item.getAmount());
        }
    }

    public void updateData(List<Transaction> data) {
        this.list = data;
        notifyDataSetChanged();
    }
}
