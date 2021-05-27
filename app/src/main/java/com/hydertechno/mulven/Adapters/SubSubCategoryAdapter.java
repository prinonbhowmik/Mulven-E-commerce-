package com.hydertechno.mulven.Adapters;

import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.hydertechno.mulven.Activities.SeeAllProductsActivity;
import com.hydertechno.mulven.Interface.SubCatIdInterface;
import com.hydertechno.mulven.Interface.SubSubCatIdInterface;
import com.hydertechno.mulven.Internet.ConnectivityReceiver;
import com.hydertechno.mulven.Models.SubCatModel;
import com.hydertechno.mulven.R;

import java.util.List;

public class SubSubCategoryAdapter extends RecyclerView.Adapter<SubSubCategoryAdapter.ViewHolder> implements ConnectivityReceiver.ConnectivityReceiverListener{

    private List<SubCatModel> list;
    private SubSubCatIdInterface idInterface;
    private Context context;
    private Snackbar snackbar;
    private boolean isConnected;
    private ConnectivityReceiver connectivityReceiver;
    private IntentFilter intentFilter;
    public SeeAllProductsActivity activity;
    public int pos=-1;

    public SubSubCategoryAdapter(List<SubCatModel> list, SubSubCatIdInterface idInterface) {
        this.list = list;
        this.idInterface = idInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_sub_category_layout_design, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SubCatModel model = list.get(position);

        holder.subCatTv.setText(model.getCategory_name());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnection();
                if (!isConnected) {
                    toastShow(isConnected);
                } else {
                    pos=holder.getAdapterPosition();
                    pos=position;
                    notifyDataSetChanged();
                    SeeAllProductsActivity.sAll.setTextColor(Color.parseColor("#FF03DAC5"));
                    //holder.subCatTv.setBackground(ContextCompat.getDrawable(context, R.drawable.status_pending));
                    //holder.subCatTv.setBackground(Color.RED);
                    if (idInterface != null) {
                        idInterface.OnSubClick(model.getId());
                    }
                }
            }



        });
        if(pos==position){
            holder.subCatTv.setTextColor(Color.parseColor("#000000"));
        }else {
            holder.subCatTv.setTextColor(Color.parseColor("#FF03DAC5"));

        }

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView subCatTv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            subCatTv = itemView.findViewById(R.id.sub_subCatTv);
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
