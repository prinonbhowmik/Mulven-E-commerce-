package com.hydertechno.mulven.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.hydertechno.mulven.Activities.PlaceOrderDetailsActivity;
import com.hydertechno.mulven.Activities.ProductDetailsActivity;
import com.hydertechno.mulven.Models.OrderListModel;
import com.hydertechno.mulven.R;
import java.util.List;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {
    private List<OrderListModel> orderListModelList;
    private Context context;

    public OrderListAdapter(List<OrderListModel> orderListModelList, Context context) {
        this.orderListModelList = orderListModelList;
        this.context = context;
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
        if(orderStatus.equals("Pending")){
            holder.orderStatusTV.setBackground(ContextCompat.getDrawable(context, R.drawable.status_pending));
        }else if(orderStatus.equals("Processing")){
            holder.orderStatusTV.setBackground(ContextCompat.getDrawable(context, R.drawable.status_processing));
        }else if(orderStatus.equals("Cancel")){
            //holder.orderStatusTV.setBackgroundColor(ContextCompat.getColor(context,R.color.GRed));
            holder.orderStatusTV.setBackground(ContextCompat.getDrawable(context, R.drawable.status_cancel));
        }else if(orderStatus.equals("Delivered")){
            holder.orderStatusTV.setBackground(ContextCompat.getDrawable(context, R.drawable.status_delivered));
        }
        else if(orderStatus.equals("Partial Paid")){
            holder.orderStatusTV.setBackground(ContextCompat.getDrawable(context, R.drawable.status_partial_paid));
        }
        holder.orderStatusTV.setText(orderStatus);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(context, PlaceOrderDetailsActivity.class);
                    intent.putExtra("OrderId",orderListModel.getOrder_id());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (Exception e) {
                    Log.d("Error",e.getMessage());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderListModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView orderIDTV,orderDateTV,orderTimeTV,orderStatusTV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIDTV=itemView.findViewById(R.id.orderIDTV);
            orderDateTV=itemView.findViewById(R.id.orderDateTV);
            orderTimeTV=itemView.findViewById(R.id.orderTimeTV);
            orderStatusTV=itemView.findViewById(R.id.orderStatusTV);
        }
    }
}
