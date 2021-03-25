package com.hydertechno.mulven.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.hydertechno.mulven.Activities.ProductDetailsActivity;
import com.hydertechno.mulven.Api.Config;
import com.hydertechno.mulven.DatabaseHelper.Database_Helper;
import com.hydertechno.mulven.Fragments.CartFragment;
import com.hydertechno.mulven.Models.CartProduct;
import com.hydertechno.mulven.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private List<CartProduct> list;
    private Context context;
    private CartFragment cartFragment;
    private androidx.appcompat.app.AlertDialog.Builder alert;

    public CartAdapter(List<CartProduct> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_layout_design, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartProduct cart = list.get(position);
        Database_Helper database_helper = new Database_Helper(context);
        holder.cartProductName.setText(cart.getProduct_name());
        holder.ProductShopName.setText(cart.getShop_name());
        holder.cartProductPrice.setText(String.valueOf(cart.getUnit_price())+" X "+cart.getQuantity());
       // holder.ProductQuantity.setText(String.valueOf(cart.getQuantity()));
        holder.cardQuantity.setText(""+cart.getQuantity());
        holder.cartProductTotalPrice.setText("৳ "+cart.getUnit_price()*Integer.parseInt(holder.cardQuantity.getText().toString()));

        cartFragment.cardSubtotalAmount.setText("৳ "+database_helper.columnSum());

        holder.cardPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count  = Integer.parseInt(holder.cardQuantity.getText().toString());
                count++;
                holder.cardQuantity.setText(String.valueOf(count));
                holder.cartProductTotalPrice.setText(""+list.get(position).getUnit_price()*count);
                holder.cartProductPrice.setText(list.get(position).getUnit_price()+" X "+String.valueOf(count));
                database_helper.addQuantity(list.get(position).getId(),count);
                cartFragment.cardSubtotalAmount.setText("৳ "+database_helper.columnSum());
            }
        });

        holder.cardMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count  = Integer.parseInt(holder.cardQuantity.getText().toString());
                count--;
                if (count>0){
                    holder.cardQuantity.setText(String.valueOf(count));
                    holder.cartProductTotalPrice.setText("" + list.get(position).getUnit_price() * count);
                    holder.cartProductPrice.setText(list.get(position).getUnit_price() + " X " + String.valueOf(count));
                    database_helper.addQuantity(list.get(position).getId(), count);
                    cartFragment.cardSubtotalAmount.setText("৳ "+database_helper.columnSum());

                }else{
                    return;
                }
            }
        });

        Picasso.get()
                .load(Config.IMAGE_LINE+cart.getImage())
                .into(holder.cartProductImage);
        holder.cartProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.putExtra("id",cart.getId());
                Log.d("productId", String.valueOf(cart.getId()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        holder.cartProductDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alert = new androidx.appcompat.app.AlertDialog.Builder(context);
                alert.setTitle("Delete!");
                alert.setMessage("Want to delete this product??");
                alert.setIcon(R.drawable.applogo);

                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        database_helper.deleteData(list.get(position).getId());
                        list.remove(position);
                        notifyDataSetChanged();
                        cartFragment.cardSubtotalAmount.setText("৳ "+database_helper.columnSum());
                    }
                });
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void swapDataSet(List<CartProduct> newData){

        this.list = newData;

        //now, tell the adapter about the update
        notifyDataSetChanged();

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView cartProductImage,cartProductDelete,cardMinus,cardPlus;
        private TextView cartProductName,ProductShopName,cartProductPrice,ProductQuantity,
                cartProductTotalPrice,cardQuantity;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cartProductImage=itemView.findViewById(R.id.cartProductImage);
            cartProductDelete=itemView.findViewById(R.id.cartProductDelete);
            cardMinus=itemView.findViewById(R.id.cardMinus);
            cardPlus=itemView.findViewById(R.id.cardPlus);
            cartProductName=itemView.findViewById(R.id.cartProductName);
            ProductShopName=itemView.findViewById(R.id.ProductShopName);
            cartProductPrice=itemView.findViewById(R.id.cartProductPrice);
            ProductQuantity=itemView.findViewById(R.id.ProductQuantity);
            cartProductTotalPrice=itemView.findViewById(R.id.cartProductTotalPrice);
            cardQuantity=itemView.findViewById(R.id.cardQuantity);

        }
    }
}
