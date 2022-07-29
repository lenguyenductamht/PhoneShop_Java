package com.example.phone.phoneapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phone.model.Discounts;
import com.example.phone.model.OrderDetail;
import com.example.phone.model.Orders;
import com.example.phone.myadapter.ViewOrderDetailAdapter;
import com.example.phone.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SoldDetailActivity extends AppCompatActivity {
    public RecyclerView rcViewOrderDetail;
    public ArrayList<OrderDetail> orderDetailArrayList;
    public ViewOrderDetailAdapter viewOrderDetailAdapter;
    public DatabaseReference mData;
    public TextView txtMaGiamGia, txtTongTien, txtTienGiamGia, tvThanhToan;
    public Button btnXacNHan;
    Orders orders;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_sold_detail);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Xác nhận đơn hàng đã giao");
        mapping();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            orders = (Orders) bundle.getSerializable("order_1");
        }
        if(MainActivity.anXacNhan){
           // MainActivity.anXacNhan = false;
            btnXacNHan.setVisibility(View.INVISIBLE);
        }else
            btnXacNHan.setVisibility(View.VISIBLE);
        showInfor();
        loadOrderDetail();
        eventUpdate();
    }

    private void eventUpdate() {
        btnXacNHan.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Map<String, Object> updateCheck = new HashMap<>();
               updateCheck.put(String.format("%s/check", orders.getOrderId()), true); // cập nhật giao
                mData.child("Orders").updateChildren(updateCheck);

                Toast.makeText(SoldDetailActivity.this, "Xác nhận thành công!!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    public void loadOrderDetail(){

        mData.child("Order Detail").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dt : dataSnapshot.getChildren()){
                    OrderDetail od = dt.getValue(OrderDetail.class);
                    if(od.getOrderId().equals(orders.getOrderId())){
                        orderDetailArrayList.add(od);
                        viewOrderDetailAdapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error:" +
                        databaseError.getMessage() , Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void showInfor(){
        if(orders != null){
            // hiện mã Giảm giá
            if(orders.getDiscountId().equals("0")){
                txtMaGiamGia.setText("Không dùng mã!");
                txtTienGiamGia.setVisibility(View.INVISIBLE); // ẩn tv tiền giảm giá
                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                txtTongTien.setText(decimalFormat.format(orders.getTotal()) + " Đ");
            }else {
                tvThanhToan.setVisibility(View.VISIBLE);
                txtMaGiamGia.setText(orders.getDiscountId());
                txtTienGiamGia.setVisibility(View.VISIBLE);
                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                txtTienGiamGia.setText(decimalFormat.format(orders.getTotal()) + " Đ");
                //hiện số tiền chưa giảm
                //lấy phần trăm giảm
                mData.child("Discounts")
                        .orderByChild("discountId")
                        .equalTo(orders.getDiscountId())
                        .limitToFirst(1)
                        .addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                Discounts d = dataSnapshot.getValue(Discounts.class);
                                double tongTien = orders.getTotal()/(1-d.getPercentage());
                                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                                txtTongTien.setText(decimalFormat.format(tongTien) + " Đ");

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

        }
    }
    public void mapping(){

        rcViewOrderDetail = findViewById(R.id.rcGioHang);
        txtTongTien = findViewById(R.id.txtTongTien);
        txtTienGiamGia = findViewById(R.id.txtTienGiamGia);
        txtMaGiamGia = findViewById(R.id.txtMaGiamGia);
        btnXacNHan = findViewById(R.id.btnXacNHan);
        mData = FirebaseDatabase.getInstance().getReference();
        orderDetailArrayList = new ArrayList<>();
        viewOrderDetailAdapter = new ViewOrderDetailAdapter(getApplicationContext(), orderDetailArrayList);
        rcViewOrderDetail.setHasFixedSize(true);
        rcViewOrderDetail.setLayoutManager(new GridLayoutManager(this, 1));
        rcViewOrderDetail.setAdapter(viewOrderDetailAdapter);
        tvThanhToan = findViewById(R.id.tvThanhToan);
        tvThanhToan.setVisibility(View.INVISIBLE);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home)
        {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
