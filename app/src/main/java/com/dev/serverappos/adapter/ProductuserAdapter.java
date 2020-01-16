package com.dev.serverappos.adapter;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.serverappos.R;
import com.dev.serverappos.fragment.Fragment_Home;
import com.dev.serverappos.fragment.Fragment_Menu;
import com.dev.serverappos.user.User;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductuserAdapter extends RecyclerView.Adapter<ProductuserAdapter.CartHolder> {
    private Fragment_Home context;
    private ArrayList<User.Product> products;
    private DatabaseReference mDatabase;

    public ProductuserAdapter(Fragment_Home context, ArrayList<User.Product> products) {
        this.context = context;
        this.products = products;
        ;
    }

    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_manage_product, parent, false);
        return new CartHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartHolder holder, final int position) {
        final User.Product product = products.get(position);
        holder.txtColor.setText("Loại : " + product.getLoaisp());
        holder.txtgia.setText("Giá:" + Double.parseDouble(product.getPriceproduct()) + "đ");
        holder.txtnameproduct.setText("Tên sản phẩm:" + product.getNameproduct());


        Picasso.get().load(product.getUri()).into(holder.imgnameproduct);

       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               context.clickprodut(product);
           }
       });
        holder.btndeletecart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.deletesp(product, products.size());
                Log.e("DELEPRO","DELESS");
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class CartHolder extends RecyclerView.ViewHolder {


        public ImageView btndeletecart;
        public ImageView imgnameproduct;
        public TextView txtnameproduct;
        public TextView txtColor;

        public TextView txtgia;


        public CartHolder(View itemView) {
            super(itemView);
            imgnameproduct = itemView.findViewById(R.id.imgnameproduct);
            txtColor = itemView.findViewById(R.id.txtColor);
            txtnameproduct = itemView.findViewById(R.id.txtnameproduct);
            txtgia = itemView.findViewById(R.id.txtgia);

            btndeletecart = itemView.findViewById(R.id.btndeletecprodut);


        }
    }


}
