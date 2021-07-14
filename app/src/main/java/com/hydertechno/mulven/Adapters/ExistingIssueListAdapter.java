package com.hydertechno.mulven.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.hydertechno.mulven.Models.ExistingIssueModel;
import com.hydertechno.mulven.R;

import java.util.List;

public class ExistingIssueListAdapter extends RecyclerView.Adapter<ExistingIssueListAdapter.ViewHolder> {

    private List<ExistingIssueModel> issueModelList;
    private Context context;

    public ExistingIssueListAdapter(List<ExistingIssueModel> issueModelList, Context context) {
        this.issueModelList = issueModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_existing_issue,parent,false);
        return new ExistingIssueListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ExistingIssueModel model = issueModelList.get(position);

        holder.reportTitle.setText(model.getIssueType() != null ? model.getIssueType() : "");
        holder.reportDescription.setText(model.getDescription() != null ? model.getDescription() : "");
        holder.reportTime.setText(model.getCreatedAt() != null ? model.getCreatedAt() : "");
        String  status = model.getStatus() != null ? model.getStatus() : "";
        holder.reportStatus.setText(status);
        switch (status) {
            case "Pending":
                holder.reportStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.status_pending));
                break;
            default:
                holder.reportStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.status_default));
        }

    }

    @Override
    public int getItemCount() {
        return issueModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView reportTitle,reportDescription,reportTime,reportStatus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            reportTitle=itemView.findViewById(R.id.reportTitleTV);
            reportDescription=itemView.findViewById(R.id.reportBodyTV);
            reportTime=itemView.findViewById(R.id.createTimeTV);
            reportStatus=itemView.findViewById(R.id.reportStatusTV);
        }
    }
}
