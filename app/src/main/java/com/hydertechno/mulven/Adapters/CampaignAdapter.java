package com.hydertechno.mulven.Adapters;

import android.content.Context;
import android.service.autofill.TextValueSanitizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hydertechno.mulven.Models.CampaignModel;
import com.hydertechno.mulven.R;

import java.util.List;

import cn.iwgang.countdownview.CountdownView;

public class CampaignAdapter extends RecyclerView.Adapter<CampaignAdapter.ViewHolder> {
    private List<CampaignModel> campaignModelList;
    private Context context;

    public CampaignAdapter(List<CampaignModel> campaignModelList, Context context) {
        this.campaignModelList = campaignModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public CampaignAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.campaign_layout_design,parent,false);
        return new CampaignAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull CampaignAdapter.ViewHolder holder, int position) {
        CampaignModel model=campaignModelList.get(position);
        holder.title.setText(model.getCampaign_name());
        holder.campaignTitle.setText(model.getCampaign_name());
        holder.campaignTimeDate.setText(model.getStart_date()+" "+model.getStart_time());
        holder.campaignCountdown.start(555550000); // Millisecond
        int status=model.getPublication_status();
        if(status==0){
            holder.liveTxt.setText("coming live on");
        }
        else if(status==1){
            holder.liveTxt.setText("is live now.");
        }


    }

    @Override
    public int getItemCount() {
        return campaignModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout campaignRl;
        private TextView title, campaignTitle,liveTxt,campaignTimeDate;
        private CountdownView campaignCountdown;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            campaignRl=itemView.findViewById(R.id.campaignRL);
            title=itemView.findViewById(R.id.title);
            campaignTitle=itemView.findViewById(R.id.campaignTitle);
            liveTxt=itemView.findViewById(R.id.liveTxt);
            campaignTimeDate=itemView.findViewById(R.id.campaignTimeDate);
            campaignCountdown=itemView.findViewById(R.id.countdown);
        }
    }
}
