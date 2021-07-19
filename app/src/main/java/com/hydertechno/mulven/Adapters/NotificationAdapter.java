package com.hydertechno.mulven.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hydertechno.mulven.Activities.NotificationDetailsActivity;
import com.hydertechno.mulven.Activities.ProductDetailsActivity;
import com.hydertechno.mulven.Models.NotificationModel;
import com.hydertechno.mulven.R;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private List<NotificationModel> list;
    private Context context;

    public NotificationAdapter(List<NotificationModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_layout_design,parent,false);
        return new NotificationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificationModel model=list.get(position);
        holder.notificationTitle.setText(model.getTitle());
        holder.notificationBody.setText(model.getBody());
        holder.notificationTime.setText(model.getDateTime());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    try {
                        Intent intent = new Intent(context, NotificationDetailsActivity.class);
                        intent.putExtra("title", model.getTitle());
                        intent.putExtra("body", model.getBody());
                        intent.putExtra("image", model.getImage());
                        context.startActivity(intent);
                    } catch (Exception e) {
                    }
                }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView notificationTitle,notificationBody,notificationTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            notificationTitle=itemView.findViewById(R.id.notificationTitle);
            notificationBody=itemView.findViewById(R.id.notificationBody);
            notificationTime=itemView.findViewById(R.id.notificationTime);
        }
    }
}
