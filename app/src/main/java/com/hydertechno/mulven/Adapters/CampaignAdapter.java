package com.hydertechno.mulven.Adapters;

import android.content.Context;
import android.service.autofill.TextValueSanitizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hydertechno.mulven.Models.CampaignModel;
import com.hydertechno.mulven.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.iwgang.countdownview.CountdownView;
import es.dmoral.toasty.Toasty;

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
        //holder.campaignCountdown.start(555550000); // Millisecond
        int status=model.getPublication_status();
        if(status==0){
            holder.liveTxt.setText("coming live on");
        }
        else if(status==1){
            holder.liveTxt.setText("is live now.");
        }

        String n=model.getStart_time();
        String a=new String();
        for(int i=0;i<n.length();i++){
            a+=n.charAt(i);
            if(i==4){
                a+=":00";
            }
        }
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");

        String start_Date=model.getStart_date()+" "+a;
        //String start_Date="2021-04-23 "+a;
        String currentDate =sdf.format(new Date());
        //String currentDate ="2021-04-22 08:00 PM";
        Date date1,date2;
        try {
            date1=sdf.parse(start_Date);
            date2=sdf.parse(currentDate);
            long millieSecond=date1.getTime()-date2.getTime();
            holder.campaignCountdown.start(millieSecond); // Millisecond
        } catch (ParseException e) {
            e.printStackTrace();
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
