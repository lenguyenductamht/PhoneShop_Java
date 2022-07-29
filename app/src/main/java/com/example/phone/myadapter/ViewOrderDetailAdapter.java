package com.example.phone.myadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phone.model.OrderDetail;
import com.example.phone.model.Products;
import com.example.phone.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ViewOrderDetailAdapter extends RecyclerView.Adapter<ViewOrderDetailAdapter.ViewHolder> {
    ArrayList<OrderDetail> ordersArrayList;
    Context context;
    DatabaseReference mData = FirebaseDatabase.getInstance().getReference();


    public ViewOrderDetailAdapter(Context context, ArrayList<OrderDetail> arr) {
        this.ordersArrayList = arr;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sold_detail_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderDetail orderDetail = ordersArrayList.get(position);
        holder.soLuong.setText("Số lượng: " + orderDetail.getQuantity());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.gia.setText("Giá: " + decimalFormat.format(orderDetail.getPrice()) + "Đ");
        //holder.color.setText("Màu: " + products.get);
        //holder.productName.setText(products.getProductName());

        //hiện tên sp và ảnh của sp đó
        // vào bảng product để lấy
        getProduct(orderDetail.getProductId(), holder);


    }



    public void getProduct(final int id1, final ViewHolder holder1){

        mData.child("Products")
                .orderByChild("productId")
                .equalTo(id1)
                .limitToFirst(1)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Products product = dataSnapshot.getValue(Products.class);
                        Picasso.with(context).load(product.getImgProduct())
                                .into(holder1.img);
                        holder1.productName.setText(product.getProductName());
                        notifyDataSetChanged();

                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }



    @Override
    public int getItemCount() {
        return ordersArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView productName;
        TextView color;
        TextView soLuong;
        TextView gia;

        public ViewHolder(View itemview) {
            super(itemview);
            img = itemview.findViewById(R.id.imgPhone);
            productName = itemview.findViewById(R.id.phoneName);
            //color = itemview.findViewById(R.id.phoneColor);
            soLuong = itemview.findViewById(R.id.soLuong);
            gia = itemview.findViewById(R.id.phonePrice);
        }
    }
}
