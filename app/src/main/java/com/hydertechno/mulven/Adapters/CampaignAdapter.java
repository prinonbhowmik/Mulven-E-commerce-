package com.hydertechno.mulven.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.service.autofill.TextValueSanitizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.hydertechno.mulven.Activities.CampaignProductActivity;
import com.hydertechno.mulven.Activities.ProductDetailsActivity;
import com.hydertechno.mulven.Api.Config;
import com.hydertechno.mulven.Fragments.CampaignFragment;
import com.hydertechno.mulven.Fragments.HomeFragment;
import com.hydertechno.mulven.Internet.ConnectivityReceiver;
import com.hydertechno.mulven.Models.CampaignModel;
import com.hydertechno.mulven.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.iwgang.countdownview.CountdownView;
import es.dmoral.toasty.Toasty;

public class CampaignAdapter extends RecyclerView.Adapter<CampaignAdapter.ViewHolder> implements ConnectivityReceiver.ConnectivityReceiverListener {
    private List<CampaignModel> campaignModelList;
    private Context context;
    private long millieSecond;
    private Snackbar snackbar;
    private boolean isConnected;
    private ConnectivityReceiver connectivityReceiver;
    private IntentFilter intentFilter;
    public CampaignFragment activity;

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
        int status=model.getPublication_status();
        try{
            Picasso.get()
                    .load(Config.IMAGE_LINE+model.getCampaign_image())
                    .into(holder.campaignImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //holder.campaignCountdown.start(555550000); // Millisecond

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
            millieSecond=date1.getTime()-date2.getTime();
            holder.campaignCountdown.start(millieSecond); // Millisecond
        } catch (ParseException e) {
            e.printStackTrace();
        }


        if(status==1) {
            holder.liveLL.setVisibility(View.VISIBLE);
            holder.campaignCountdown.setVisibility(View.GONE);

        } else if(status==0){
            if(millieSecond<0) {
                holder.overLL.setVisibility(View.VISIBLE);
                holder.campaignCountdown.setVisibility(View.GONE);
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkConnection();
                if (!isConnected) {
                    toastShow(isConnected);
                } else {
                if (status == 1) {

                        Intent intent = new Intent(context, CampaignProductActivity.class);
                        intent.putExtra("id", model.getId());
                        intent.putExtra("title", model.getCampaign_name());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        /*    ((Activity)context).finish();*/


                } else if (status==0){
                    if (millieSecond < 0) {
                        Toasty.normal(context, "Campaign over.", Toasty.LENGTH_SHORT).show();
                    } else if (millieSecond > 0) {
                        Toasty.normal(context, "Coming live soon!", Toasty.LENGTH_SHORT).show();
                    }
                }
            }

            }
        });

    }

    @Override
    public int getItemCount() {
        return campaignModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView campaignCV;
        private TextView liveTxt,campaignTimeDate;
        private ImageView campaignImage;
        private CountdownView campaignCountdown;
        private LinearLayout liveLL,overLL;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            campaignCV=itemView.findViewById(R.id.campaignCV);
            campaignImage=itemView.findViewById(R.id.campaignImage);
            campaignCountdown=itemView.findViewById(R.id.countdown);
            liveLL=itemView.findViewById(R.id.liveLL);
            overLL=itemView.findViewById(R.id.overLL);
        }
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
}
