package com.example.phone.phoneapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.phone.R;
import com.example.phone.model.Customer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EnterPhoneNumber extends AppCompatActivity {
    private EditText phoneNumber;
    private Button btnTiepTuc;
    public DatabaseReference mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_phone_number);
        mapping();
        btnTiepTuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = phoneNumber.getText().toString().trim();
                if(number.isEmpty()){
                    Toast.makeText(EnterPhoneNumber.this, "Bạn chưa nhập số điện thoại", Toast.LENGTH_SHORT);
                }
                else{
                    mData.child("Customer").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot dt : dataSnapshot.getChildren()) {
                                Customer customer = dt.getValue(Customer.class);

                                if(customer.getPhoneNumber().equals(number)){
                                    Intent intent = new Intent(EnterPhoneNumber.this, CustomerInfoActivity.class);
                                    intent.putExtra("phone", number);
                                    startActivity(intent);
                                }
                                else{
                                    Toast.makeText(EnterPhoneNumber.this, "Số điện thoại chưa đăng ký", Toast.LENGTH_SHORT);
                                    Intent intent = new Intent(EnterPhoneNumber.this, CustomerActivity.class);
                                    intent.putExtra("phone", number);
                                    startActivity(intent);
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
            }
        });

    }

    public void mapping(){
        mData = FirebaseDatabase.getInstance().getReference();
        phoneNumber = (EditText) findViewById(R.id.edit_PhoneNumber);
        btnTiepTuc = (Button) findViewById(R.id.btnContinue);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Thông tin khách hàng");
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}