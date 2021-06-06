package com.hydertechno.mulven.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.hydertechno.mulven.Activities.PlaceOrderDetailsActivity;
import com.hydertechno.mulven.Activities.PlaceOrderListActivity;
import com.hydertechno.mulven.Activities.ProductDetailsActivity;
import com.hydertechno.mulven.Interface.OnClickOrderListener;
import com.hydertechno.mulven.Internet.ConnectivityReceiver;
import com.hydertechno.mulven.Models.CategoriesModel;
import com.hydertechno.mulven.Models.OrderListModel;
import com.hydertechno.mulven.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> implements ConnectivityReceiver.ConnectivityReceiverListener  {
    private List<OrderListModel> orderListModelList;
    private List<OrderListModel> orderListModelListFiltered;
    private Context context;
    private Snackbar snackbar;
    private boolean isConnected;
    private ConnectivityReceiver connectivityReceiver;
    private IntentFilter intentFilter;
    public PlaceOrderListActivity activity;
    private OnClickOrderListener listener;

    public OrderListAdapter(List<OrderListModel> orderListModelList, Context context, OnClickOrderListener listener) {
        this.orderListModelList = orderListModelList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_layout_design, parent, false);
        return new OrderListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderListModel orderListModel=orderListModelList.get(position);
        holder.orderIDTV.setText(orderListModel.getOrder_id());
        holder.orderDateTV.setText(orderListModel.getDate());
        holder.orderTimeTV.setText(orderListModel.getTime());
        String orderStatus=orderListModel.getOrders_status();
        //holder.orderStatusTV.setBackground(ContextCompat.getDrawable(context, R.drawable.status_cancel));
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
            case "Picked":
                holder.orderStatusTV.setBackground(ContextCompat.getDrawable(context, R.drawable.status_picked));
                break;
        }
        holder.orderStatusTV.setText(orderStatus);
        String payStatus=orderListModel.getPay_status();

        if(payStatus.equals("Unpaid")){
            holder.paymentStatusTV.setText("Unpaid");
            holder.paymentStatusTV.setBackground(ContextCompat.getDrawable(context, R.drawable.payment_unpaid));
        }else if(payStatus.equals("Paid")) {
            holder.paymentStatusTV.setText("Paid");
            holder.paymentStatusTV.setBackground(ContextCompat.getDrawable(context, R.drawable.payment_paid));
        } else if(payStatus.equals("Partial Paid")){
            holder.paymentStatusTV.setText("Partial Paid");
            holder.paymentStatusTV.setBackground(ContextCompat.getDrawable(context, R.drawable.payment_partial_paid));
        }

        intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        connectivityReceiver = new ConnectivityReceiver();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnection();
                if (!isConnected) {
                    toastShow(isConnected);
                }else{
                try {
                    listener.onClick(orderListModel);
                } catch (Exception e) {
                    Log.d("Error", e.getMessage());
                }
            }
            }
        });
    }

    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<OrderListModel> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(orderListModelListFiltered);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (OrderListModel item : orderListModelList) {
                    if (item.getOrders_status().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            orderListModelList.clear();
            orderListModelList.addAll((List) results.values);
            Collections.reverse(orderListModelList);
            notifyDataSetChanged();
        }
    };

    @Override
    public int getItemCount() {
        return orderListModelList.size();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        toastShow(isConnected);
    }

    private void toastShow(boolean isConnected) {
        if(!isConnected) {
            snackbar = Snackbar.make(activity.rootLayout, "No Internet Connection! Please Try Again.", Snackbar.LENGTH_INDEFINITE);
            snackbar.setDuration(5000);
            snackbar.setActionTextColor(Color.WHITE);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(Color.RED);
            snackbar.show();
            }
    }


    private void checkConnection() {
        isConnected = ConnectivityReceiver.isConnected();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView orderIDTV,orderDateTV,orderTimeTV,orderStatusTV,paymentStatusTV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIDTV=itemView.findViewById(R.id.orderIDTV);
            orderDateTV=itemView.findViewById(R.id.orderDateTV);
            orderTimeTV=itemView.findViewById(R.id.orderTimeTV);
            orderStatusTV=itemView.findViewById(R.id.orderStatusTV);
            paymentStatusTV=itemView.findViewById(R.id.paymentStatusTV);
        }
    }
}
