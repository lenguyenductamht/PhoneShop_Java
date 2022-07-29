package com.example.phone.myadapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.phone.model.Discounts;
import com.example.phone.model.Products;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageAdapter extends PagerAdapter {
    Context mContext;
    ArrayList<Discounts> arrayList;
    ArrayList<Products> arrPro;

    public ImageAdapter(Context context, ArrayList<Discounts> arr) {
        this.mContext = context;
        this.arrayList = arr;
    }

    public ImageAdapter(ArrayList<Products> arr, Context context) {
        this.mContext = context;
        this.arrPro = arr;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }

//    private int[] sliderImageId = new int[]{
//            com.example.shoesapp.R.drawable.image1,
//            com.example.shoesapp.R.drawable.image2,
//            com.example.shoesapp.R.drawable.image4,
//            com.example.shoesapp.R.drawable.image3,
//    };

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Picasso.with(mContext).load(arrayList.get(position).getImage())
                .into(imageView);
        ((ViewPager) container).addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((ImageView) object);
    }
}
