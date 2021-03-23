package com.hydertechno.mulven.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        holder.cartProductTotalPrice.setText(""+cart.getUnit_price()*Integer.parseInt(holder.cardQuantity.getText().toString()));

        cartFragment.cardSubtotalAmount.setText(""+total());
        holder.cardPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count  = Integer.parseInt(holder.cardQuantity.getText().toString());
                count++;
                holder.cardQuantity.setText(String.valueOf(count));
                holder.cartProductTotalPrice.setText(""+list.get(position).getUnit_price()*count);
                holder.cartProductPrice.setText(list.get(position).getUnit_price()+" X "+String.valueOf(count));
                database_helper.addQuantity(list.get(position).getId(),count);
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
                    database_helper.addQuantity(list.get(position).getId(), count);;
                }else{
                    return;
                }
            }
        });

        Picasso.get()
                .load(Config.IMAGE_LINE+cart.getImage())
                .into(holder.cartProductImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, ""+cart.getId(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.cartProductDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database_helper.deleteData(list.get(position).getId());
                list.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  int total(){
        Database_Helper helper = new Database_Helper(context);
        Cursor cursor = helper.getCart();
        int count = 0;
        if (cursor.getCount()>0){
            while (cursor.moveToNext()){
                count+=cursor.getInt(3)*cursor.getInt(5);
            }
        }
        return count;
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
