package com.example.phone.myadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phone.R;
import com.example.phone.model.Products;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CategoryAdapter  extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{

    private ArrayList<Products> arrayProduct;
    private Context context;

    public CategoryAdapter(ArrayList<Products> arrayProduct, Context context) {
        this.arrayProduct = arrayProduct;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Products product = arrayProduct.get(position);


        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.priceProduct.setText("Giá: " + decimalFormat.format(product.getPrice())  +"Đ");
        holder.nameProduct.setText(product.getProductName());
        Picasso.with(context).load(product.getImgProduct())
                .into(holder.imgProduct); // nếu thành công thì gán ảnh vào
    }

    @Override
    public int getItemCount() {
        return arrayProduct.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imgProduct;
        TextView nameProduct;
        TextView priceProduct;
        RecyclerView recyclerView;
        public ViewHolder (View itemview) {
            super(itemview);
            imgProduct = itemview.findViewById(R.id.imgProduct);
            nameProduct = itemview.findViewById(R.id.nameProduct);
            priceProduct = itemview.findViewById(R.id.priceProduct);
            recyclerView = itemview.findViewById(R.id.itemCate);
        }
    }
}
