package com.example.phone.myadapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.phone.R;
import com.example.phone.model.Cart;
import com.example.phone.phoneapp.InfoProductsActivity;
import com.example.phone.phoneapp.MainActivity;
import com.example.phone.phoneapp.CartActivity;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CartAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Cart> arrCart;

    public CartAdapter(Context context, ArrayList<Cart> arrCart) {
        this.context = context;
        this.arrCart = arrCart;
    }

    @Override
    public int getCount() {
        return arrCart.size();
    }

    @Override
    public Object getItem(int position) {
        return arrCart.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder{
        public TextView txtTenGiay, txtSize, txtGia;
        public ImageView img;
        public Button btnCong, btnTru, btnSoLuong;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_cart_item, null);
            viewHolder.txtTenGiay = (TextView) convertView.findViewById(R.id.textphonename);
            viewHolder.txtGia = (TextView) convertView.findViewById(R.id.textgiagiay);
            viewHolder.txtSize = (TextView) convertView.findViewById(R.id.size);
            viewHolder.btnCong = (Button) convertView.findViewById(R.id.buttoncong);
            viewHolder.btnSoLuong = (Button) convertView.findViewById(R.id.buttonsoluong);
            viewHolder.btnTru = (Button) convertView.findViewById(R.id.buttontru);
            viewHolder.img = (ImageView) convertView.findViewById(R.id.imagegiay);
            convertView.setTag(viewHolder);//đưa id vào view của holder
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Cart cart = (Cart) getItem(position);
        viewHolder.txtTenGiay.setText(cart.getProductName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.txtGia.setText("Giá: " + decimalFormat.format(cart.getPrice()) + " Đ");
        viewHolder.btnSoLuong.setText(String.valueOf(cart.getQuantity()));
        Picasso.with(context).load(cart.getImg())
                .into(viewHolder.img); // nếu thành công thì gán ảnh vào

        final int sl = Integer.parseInt(viewHolder.btnSoLuong.getText().toString());
        if(sl >= 10 || sl>= InfoProductsActivity.slConLai){
            viewHolder.btnCong.setEnabled(false);
        }else if(sl <= 1){
            viewHolder.btnTru.setEnabled(false);
        }else {
            viewHolder.btnTru.setEnabled(true);
            viewHolder.btnCong.setEnabled(true);
        }

        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.btnSoLuong.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(Integer.parseInt(finalViewHolder.btnSoLuong.getText().toString()) == 10){
                    finalViewHolder.btnCong.setEnabled(false);
                }
                if(Integer.parseInt(finalViewHolder.btnSoLuong.getText().toString()) == 1){
                    finalViewHolder.btnTru.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        viewHolder.btnCong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int slMoiNhat = Integer.parseInt(finalViewHolder.btnSoLuong.getText().toString()) + 1;
                //int slHt = MainActivity.arrayCart.get(position).getQuantity();

                MainActivity.arrayCart.get(position).setQuantity(slMoiNhat);
                CartActivity.eventUltil();
                finalViewHolder.btnTru.setEnabled(true);
                if(slMoiNhat > 9 || slMoiNhat >= InfoProductsActivity.slConLai){

                    finalViewHolder.btnCong.setEnabled(false);
                    finalViewHolder.btnTru.setEnabled(true);
                    finalViewHolder.btnSoLuong.setText(String.valueOf(slMoiNhat));
                }else {

                    finalViewHolder.btnTru.setEnabled(true);
                    finalViewHolder.btnCong.setEnabled(true);
                    finalViewHolder.btnSoLuong.setText(String.valueOf(slMoiNhat));
                }
            }
        });

        viewHolder.btnTru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalViewHolder.btnCong.setEnabled(true);
                int slMoiNhat = Integer.parseInt(finalViewHolder.btnSoLuong.getText().toString()) - 1;
                //int slHt = MainActivity.arrayCart.get(position).getQuantity();
                MainActivity.arrayCart.get(position).setQuantity(slMoiNhat);
                CartActivity.eventUltil();
                if(slMoiNhat <2){
                    finalViewHolder.btnTru.setEnabled(false);
                    finalViewHolder.btnCong.setEnabled(true);
                    finalViewHolder.btnSoLuong.setText(String.valueOf(slMoiNhat));
                }else {
                    finalViewHolder.btnTru.setEnabled(true);
                    finalViewHolder.btnCong.setEnabled(true);
                    finalViewHolder.btnSoLuong.setText(String.valueOf(slMoiNhat));
                }
            }
        });
        return convertView;
    }
}
