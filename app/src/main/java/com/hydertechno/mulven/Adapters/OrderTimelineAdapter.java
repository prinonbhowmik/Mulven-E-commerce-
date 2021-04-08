package com.hydertechno.mulven.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hydertechno.mulven.Models.OrderTimelineModel;
import com.hydertechno.mulven.R;

import java.util.List;

public class OrderTimelineAdapter extends RecyclerView.Adapter<OrderTimelineAdapter.ViewHolder> {
    private List<OrderTimelineModel> orderListModelList;
    private Context context;

    public OrderTimelineAdapter(List<OrderTimelineModel> orderListModelList, Context context) {
        this.orderListModelList = orderListModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.order_timeline_layout_design,parent,false);
        return new OrderTimelineAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderTimelineModel model=orderListModelList.get(position);
        holder.timelineDateTV.setText(model.getDate());
        holder.timelineTimeTV.setText(model.getTime());
        holder.timelineStatusTV.setText(model.getStatus());
        holder.timelineDescriptionTV.setText(model.getCommants());
    }

    @Override
    public int getItemCount() {
        return orderListModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView timelineDateTV,timelineTimeTV,timelineStatusTV,timelineDescriptionTV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            timelineDateTV=itemView.findViewById(R.id.timelineDateTV);
            timelineTimeTV=itemView.findViewById(R.id.timelineTimeTV);
            timelineStatusTV=itemView.findViewById(R.id.timelineStatusTV);
            timelineDescriptionTV=itemView.findViewById(R.id.timelineDescriptionTV);
        }
    }
}
