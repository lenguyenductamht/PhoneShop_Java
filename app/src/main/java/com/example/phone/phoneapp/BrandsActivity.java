package com.example.phone.phoneapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phone.R;
import com.example.phone.model.Brands;
import com.example.phone.model.Products;
import com.example.phone.myadapter.ProductAdapter;
import com.example.phone.myadapter.RecyclerViewAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BrandsActivity extends AppCompatActivity  implements RecyclerViewAdapter.OnItemClickListener{
    ArrayList<Brands> imageURLs;
    ArrayList<Products> arrayProduct;
    ProductAdapter productAdapter;
    RecyclerViewAdapter recyclerViewAdapter;
    RecyclerView recyclerCate, listProduct;
    int brandId0 = 0;
    private DatabaseReference mData;
    private ImageView imgThongBao;
    private TextView txtThongBao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mapping();
        //Get Brands
        mData.child("Brands").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dt : dataSnapshot.getChildren()){
                    Brands cate = dt.getValue(Brands.class);
                    imageURLs.add(cate);
                    recyclerViewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(BrandsActivity.this, "Error:" +
                        databaseError.getMessage() , Toast.LENGTH_SHORT).show();
            }
        });

        //mac dinh lay id cua loai 1
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            brandId0 = bundle.getInt("brandId");
        }
        mData.child("Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dt : dataSnapshot.getChildren()){
                    Products cate = dt.getValue(Products.class);
                    if(cate.getBrandId() == brandId0) {
                        arrayProduct.add(cate);
                        productAdapter.notifyDataSetChanged();
                    }
                }
                checkData();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(BrandsActivity.this, "Error:" +
                        databaseError.getMessage() , Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_cart:
                //hiện giỏ hàng
                Intent intents = new Intent(this, CartActivity.class);
                intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intents);
                return true;
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
                //this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Giỏ hàng
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navbottom_items,menu);
        return true;
    }

    public void mapping(){
        mData = FirebaseDatabase.getInstance().getReference();
        //cate
        imageURLs = new ArrayList<>();
        recyclerCate = (RecyclerView)findViewById(R.id.recyclerView_Logo);
        recyclerViewAdapter = new RecyclerViewAdapter(this, imageURLs, this);
        recyclerCate.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerCate.setAdapter(recyclerViewAdapter);

        //product
        arrayProduct = new ArrayList<>();
        listProduct = (RecyclerView)findViewById(R.id.listProduct_brand);
        productAdapter = new ProductAdapter(this, arrayProduct);
        listProduct.setHasFixedSize(true);
        listProduct.setLayoutManager(new GridLayoutManager(this, 2));
        listProduct.setAdapter(productAdapter);
        txtThongBao = findViewById(R.id.txtThongBao_brand);
        imgThongBao = findViewById(R.id.imgThongBao_brand);
    }

    @Override
    public void onItemClickListener(int position) {
        final int idBrand = imageURLs.get(position).getBrandId();

        //clear
        arrayProduct.clear();
        productAdapter.notifyDataSetChanged();
        mData.child("Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dt : dataSnapshot.getChildren()){
                    Products pro = dt.getValue(Products.class);
                    if(pro.getBrandId() == idBrand) {
                        arrayProduct.add(pro);
                        productAdapter.notifyDataSetChanged();
                    }
                }
                checkData();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(BrandsActivity.this, "Error:" +
                        databaseError.getMessage() , Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void checkData() {

        if(arrayProduct.size() > 0){ // ẩn
            txtThongBao.setVisibility(View.INVISIBLE);
            imgThongBao.setVisibility(View.INVISIBLE);
            listProduct.setVisibility(View.VISIBLE);
        }else {
            txtThongBao.setText("Không tìm thấy sản phẩm nào!!");
            txtThongBao.setVisibility(View.VISIBLE);
            imgThongBao.setVisibility(View.VISIBLE);
            listProduct.setVisibility(View.INVISIBLE);
        }
    }
}
