package com.example.phone.phoneapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phone.R;
import com.example.phone.model.Orders;
import com.example.phone.myadapter.ViewOrdersAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;

public class SoldActivity extends AppCompatActivity {
    public RecyclerView rcViewOrders;
    public ArrayList<Orders> arrayViewOrders;
    public ViewOrdersAdapter viewOrderAdapter;
    public DatabaseReference mData;
    private DatePickerDialog datePickerDialog;
    private Button btnDate;
    TextView txtThongbao;
    ImageView image;
    String co;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_sold);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Đơn hàng");
        initDatePicker();
        mapping();
        btnDate.setText(getToday());

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

//        if (bundle != null) {
//            co = bundle.getString("order1");
//
//        }
//        if(co != null){
//            loadOrders2();
//        }else {
//            loadOrders();
//        }
        loadOrders();

    }

    private String getToday() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month,year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month;
                String date = makeDateString(dayOfMonth, month, year);
                btnDate.setText(date);
                loadOrders();
            }
        };
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }



    private String makeDateString(int dayOfMonth, int month, int year) {
        return dayOfMonth + "-" + getMonthFormat(month) + "-" +year;
    }

    private String getMonthFormat(int month) {
        if(month==0)
            return "01";
        if(month==1)
            return "02";
        if(month==2)
            return "03";
        if(month==3)
            return "04";
        if(month==4)
            return "05";
        if(month==5)
            return "06";
        if(month==6)
            return "07";
        if(month==7)
            return "08";
        if(month==8)
            return "09";
        if(month==9)
            return "10";
        if(month==10)
            return "11";
        if(month==11)
            return "12";
        return "01";
    }
    public void openDatePicker(View view) {
        datePickerDialog.show();
    }

    public void loadOrders() {
        mData.child("Orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayViewOrders.clear();
                for (DataSnapshot dt : dataSnapshot.getChildren()) {
                    Orders orders = dt.getValue(Orders.class);
                    if((MainActivity.mUser.getUid().trim().equals(orders.getUserId().trim())) && (String.valueOf(orders.getOrderDate()).equals(String.valueOf(btnDate.getText())))){
                        arrayViewOrders.add(orders);
                        Collections.reverse(arrayViewOrders);
                        viewOrderAdapter.notifyDataSetChanged();
                    }
                }
                checkData();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error:" +
                        databaseError.getMessage() , Toast.LENGTH_SHORT).show();
            }


        });

    }

    public void checkData() {
        if(arrayViewOrders.size() <= 0){
            viewOrderAdapter.notifyDataSetChanged();
            //hiện thông báo k có sp
            txtThongbao.setVisibility(View.VISIBLE);
            image.setVisibility(View.VISIBLE);
            rcViewOrders.setVisibility(View.INVISIBLE);

        }else {
            viewOrderAdapter.notifyDataSetChanged();
            //hiện thông báo k có sp
            txtThongbao.setVisibility(View.INVISIBLE);
            image.setVisibility(View.INVISIBLE);
            rcViewOrders.setVisibility(View.VISIBLE);

        }
    }


    public void mapping(){
        rcViewOrders = findViewById(R.id.rcViewOrders);
        txtThongbao = findViewById(R.id.txtThongBao);
        image = findViewById(R.id.imgThongBao);
        mData = FirebaseDatabase.getInstance().getReference();
        arrayViewOrders = new ArrayList<>();
        viewOrderAdapter = new ViewOrdersAdapter(getApplicationContext(), arrayViewOrders);
        rcViewOrders.setHasFixedSize(true);
        rcViewOrders.setLayoutManager(new GridLayoutManager(this, 1));
        rcViewOrders.setAdapter(viewOrderAdapter);
        btnDate = findViewById(R.id.btnDatePicker);


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home)
        {
            if(MainActivity.anXacNhan){
                MainActivity.anXacNhan = false;
            }
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


}
