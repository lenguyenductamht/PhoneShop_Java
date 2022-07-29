package com.example.phone.myadapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phone.model.Brands;
import com.example.phone.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>
{
    private static final String TAG = "RecyclerViewAdapter";
    private ArrayList<Brands> imageURLs;
    private Context context;
    private OnItemClickListener mOnClick;

    public RecyclerViewAdapter(Context context, ArrayList<Brands> imageURLs,
                               OnItemClickListener ol) {
        this.imageURLs = imageURLs;
        this.context = context;
        this.mOnClick = ol;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //gọi view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.circleimageviewlogo, parent, false);
        return new ViewHolder(view, mOnClick);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        //set, gét các thuộc tính cho layout
        Log.d(TAG, "onCreateViewHolder: called.");
        //Glide.with(context).asBitmap().load(imageURLs.get(position)).into(holder.imageLogo);
        Picasso.with(context).load(imageURLs.get(position).getImgBrand())
                .into(holder.imageLogo); // nếu thành công thì gán ảnh vào
    }

    @Override
    public int getItemCount() {
        return imageURLs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //RelativeLayout relativeLayout;
        ImageView imageLogo;
        OnItemClickListener onItemClickListener;
        public ViewHolder (View itemview, OnItemClickListener onItemClickListener) {
            super(itemview);
            imageLogo = itemview.findViewById(R.id.imageLogo);
            //relativeLayout = itemview.findViewById(R.id.itemCate);
            this.onItemClickListener = onItemClickListener;
            itemview.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClickListener(getAdapterPosition());
        }
    }
    public interface OnItemClickListener{
        void onItemClickListener(int position);
    }
}
