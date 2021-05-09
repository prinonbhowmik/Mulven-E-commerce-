package com.hydertechno.mulven.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hydertechno.mulven.Interface.SubCatIdInterface;
import com.hydertechno.mulven.Models.OrderListModel;
import com.hydertechno.mulven.Models.SubCatModel;
import com.hydertechno.mulven.R;

import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.ViewHolder> {

    private List<SubCatModel> list;
    private SubCatIdInterface idInterface;
    private Context context;

    public SubCategoryAdapter(List<SubCatModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subcat_recycler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SubCatModel model = list.get(position);

        holder.subCatTv.setText(model.getCategory_name());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (idInterface!=null){
                    idInterface.OnClick(model.getId());
                    Log.d("hoiseki", String.valueOf(model.getId()));
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView subCatTv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            subCatTv = itemView.findViewById(R.id.subCatTv);
        }
    }
}
