package com.example.phone.myadapter;

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

import com.example.phone.R;
import com.example.phone.model.Discounts;
import com.example.phone.phoneapp.InforDiscountActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DiscountAdapter extends RecyclerView.Adapter<DiscountAdapter.ViewHolder>{
    ArrayList<Discounts> arrayDiscount;
    Context context;


    public DiscountAdapter(Context context, ArrayList<Discounts> arrayDiscount) {
        this.arrayDiscount = arrayDiscount;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discount_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Discounts discounts = arrayDiscount.get(position);
        holder.discountId.setText("Ma giảm: " + discounts.getDiscountId());
        holder.titleDiscount.setText(discounts.getTitle());
        holder.txtEndDate.setText(String.format("Ngày hết hạn: %s", discounts.getEndDate()));
        Picasso.with(context).load(discounts.getImage())
                .into(holder.imgDiscount); // nếu thành công thì gán ảnh vào

        holder.lnDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, InforDiscountActivity.class);
                intent.putExtra("discountInfor", discounts);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayDiscount.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgDiscount;
        TextView titleDiscount;
        TextView txtEndDate;
        TextView discountId;
        LinearLayout lnDiscount;

        public ViewHolder(View itemview) {
            super(itemview);
            imgDiscount = itemview.findViewById(R.id.imageDiscount);
            titleDiscount = itemview.findViewById(R.id.txtTitleDiscount);
            discountId = itemview.findViewById(R.id.discountId);
            txtEndDate = itemview.findViewById(R.id.txtEndDate);
            lnDiscount = itemview.findViewById(R.id.lnDiscount);
        }
    }
}
