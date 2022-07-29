package com.example.phone.myadapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phone.model.Products;
import com.example.phone.phoneapp.InfoProductsActivity;
import com.example.phone.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";

    ArrayList<Products> arrayProduct;
    Context context;


    public ProductAdapter(Context context, ArrayList<Products> arrayProduct) {
        this.arrayProduct = arrayProduct;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, null);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Products product = arrayProduct.get(position);


        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.priceProduct.setText("Giá: " + decimalFormat.format(product.getPrice()) + "Đ");
        if(product.getQuantity()<=0){
            holder.nameProduct.setText(product.getProductName()+"\nHết hàng");
        }
        else{
            holder.nameProduct.setText(product.getProductName());
       }

        Picasso.with(context).load(product.getImgProduct())
                .into(holder.imgProduct); // nếu thành công thì gán ảnh vào

        holder.product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, InfoProductsActivity.class);
                intent.putExtra("productInfor", product);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // để clear acivity khi back
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayProduct.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView nameProduct;
        TextView priceProduct;
        LinearLayout product;

        public ViewHolder(View itemview) {
            super(itemview);
            imgProduct = itemview.findViewById(R.id.imgProduct);
            nameProduct = itemview.findViewById(R.id.nameProduct);
            priceProduct = itemview.findViewById(R.id.priceProduct);
            product = itemview.findViewById(R.id.product);
        }
    }
}
