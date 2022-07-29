package com.example.phone.myadapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phone.model.Orders;
import com.example.phone.R;
import com.example.phone.phoneapp.SoldDetailActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ViewOrdersAdapter extends RecyclerView.Adapter<ViewOrdersAdapter.ViewHolder>{
    ArrayList<Orders> ordersArrayList;
    Context context;


    public ViewOrdersAdapter(Context context, ArrayList<Orders> arr) {
        this.ordersArrayList = arr;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sold_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Orders orders = ordersArrayList.get(position);
        holder.orderId.setText("Mã đơn hàng: " + orders.getOrderId());
        holder.orderDate.setText("Ngày đặt hàng: " + orders.getOrderDate());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.total.setText("Tổng tiền: " + decimalFormat.format(orders.getTotal()) + "Đ");
        holder.ln.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SoldDetailActivity.class);
                intent.putExtra("order_1", orders);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return ordersArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView orderDate;
        TextView orderId;
        TextView total;
        LinearLayout ln;

        public ViewHolder(View itemview) {
            super(itemview);

            orderDate = itemview.findViewById(R.id.orderDate);
            orderId = itemview.findViewById(R.id.orderId);
            total = itemview.findViewById(R.id.total);
            ln = itemview.findViewById(R.id.ln);
        }
    }
}
