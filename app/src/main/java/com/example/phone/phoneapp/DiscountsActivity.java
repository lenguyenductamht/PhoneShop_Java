package com.example.phone.phoneapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phone.R;
import com.example.phone.model.Discounts;
import com.example.phone.myadapter.DiscountAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DiscountsActivity extends AppCompatActivity {
    public ArrayList<Discounts> arrayDiscount;
    public DiscountAdapter discountAdapter;
    public RecyclerView rcDiscount;
    public DatabaseReference mData;
    public SearchView searchView;
    TextView txtThongbao;
    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discounts);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Discount");
        mapping();
        loadDiscount();
        //checkData();
        if(searchView != null){
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText);
                    return true;
                }
            });
        }

    }

    public void checkData() {
        if(arrayDiscount.size() <= 0){
            discountAdapter.notifyDataSetChanged();
            //hiện thông báo k có sp
            txtThongbao.setVisibility(View.VISIBLE);
            image.setVisibility(View.VISIBLE);
            rcDiscount.setVisibility(View.INVISIBLE);

        }else {
            discountAdapter.notifyDataSetChanged();
            //hiện thông báo k có sp
            txtThongbao.setVisibility(View.INVISIBLE);
            image.setVisibility(View.INVISIBLE);
            rcDiscount.setVisibility(View.VISIBLE);

        }
    }

    public void search(String str) {
        ArrayList<Discounts> arraySearch = new ArrayList<>();
        for(Discounts p : arrayDiscount){
            if(p.getDiscountId().toLowerCase().contains(str.toLowerCase())){
                arraySearch.add(p);
            }
        }

        if(arraySearch.size() > 0){
            txtThongbao.setVisibility(View.INVISIBLE);//ẩn
            image.setVisibility(View.INVISIBLE);
            rcDiscount.setVisibility(View.VISIBLE);
            DiscountAdapter allProAdapter = new DiscountAdapter(getApplicationContext(), arraySearch);
            rcDiscount.setHasFixedSize(true);
            rcDiscount.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
            rcDiscount.setAdapter(allProAdapter);
        }else {
            txtThongbao.setText("Không tìm thấy sản phẩm nào!!");
            image.setVisibility(View.VISIBLE);
            txtThongbao.setVisibility(View.VISIBLE);
            rcDiscount.setVisibility(View.INVISIBLE);
        }
    }

    public void loadDiscount() {
        mData.child("Discounts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dt : dataSnapshot.getChildren()) {
                    Discounts discounts = dt.getValue(Discounts.class);
                    try {
                        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                        if ((!(df.parse(discounts.getStartDate()).after(new Date()))
                                && !(df.parse(discounts.getEndDate())).before(new Date()))
                                || !df.parse(discounts.getStartDate()).before(new Date())) {
                            /* startDate <= dateNow <= endDate */
                            arrayDiscount.add(discounts);
                            discountAdapter.notifyDataSetChanged();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                checkData();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DiscountsActivity.this, "Error:" +
                        databaseError.getMessage() , Toast.LENGTH_SHORT).show();
            }


        });
    }

    public void mapping() {
        mData = FirebaseDatabase.getInstance().getReference();
        arrayDiscount = new ArrayList<>();
        discountAdapter = new DiscountAdapter(DiscountsActivity.this, arrayDiscount);
        rcDiscount = findViewById(R.id.rcDiscount);
        rcDiscount.setHasFixedSize(true);
        rcDiscount.setLayoutManager(new GridLayoutManager(this, 1));
        rcDiscount.setAdapter(discountAdapter);
        searchView = (SearchView)findViewById(R.id.cardSearch1);
        txtThongbao = (TextView)findViewById(R.id.textthongbao1);
        image = (ImageView) findViewById(R.id.image1);
//        txtThongbao.setVisibility(View.INVISIBLE);//ẩn
//        image.setVisibility(View.INVISIBLE);
//        //rcDiscount.setVisibility(View.VISIBLE);
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
