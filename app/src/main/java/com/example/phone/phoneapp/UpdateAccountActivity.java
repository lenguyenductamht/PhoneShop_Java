package com.example.phone.phoneapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.phone.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class UpdateAccountActivity extends AppCompatActivity {

    private EditText editTextTenNguoiDung, editTextDiaChi, editTextEmail, editTextSdt;
    private Button btnXacNhap;

    private DatabaseReference mReference;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private String ten;
    private String diaChi;
    private String email;
    private String sdt;

//    String TEN, DIACHI, SDT, EMAIL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_st_use);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Cập nhật thông tin");

        editTextTenNguoiDung = findViewById(R.id.edit_TenNguoiDung);
        editTextDiaChi = findViewById(R.id.edit_DiaChi);
        editTextEmail = findViewById(R.id.edit_Email);
        editTextSdt = findViewById(R.id.edit_Sdt);
        btnXacNhap = findViewById(R.id.btn_XacNhan);

        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mReference = FirebaseDatabase.getInstance().getReference("UserInfor");

        showInforUser();
        btnXacNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
//                Toast.makeText(UpdateAccountActivity.this, ""+ten, Toast.LENGTH_LONG).show();
            }
        });

//        editTextEmail.setVisibility(View.INVISIBLE);
        editTextEmail.setFocusable(false);

    }

    private void updateUser(){

        ten = editTextTenNguoiDung.getText().toString();
        diaChi = editTextDiaChi.getText().toString();
        email = editTextEmail.getText().toString();
        sdt = editTextSdt.getText().toString();

        HashMap<String, Object> result = new HashMap<>();
        result.put("address",""+ diaChi);
        result.put("fullName",""+ ten);
        result.put("phoneNumber",""+ sdt);
        //                result.put("imgUser", imgUser);
        result.put("emailUser",""+ email);
        mReference.child(mAuth.getUid()).updateChildren(result).addOnSuccessListener(new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void aVoid) {
                Toast.makeText(UpdateAccountActivity.this, "Cập nhật thông tin!", Toast.LENGTH_SHORT).show();
                if(ConfirmAccountActivity.keyReturn == true){
                    ConfirmAccountActivity.keyReturn = false;
                    Intent i = new Intent(UpdateAccountActivity.this, ConfirmAccountActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    showInforUser();
                    finish();
                }else {
                    Intent i = new Intent(UpdateAccountActivity.this, AccountActivity.class);
                    startActivity(i);
                    showInforUser();
                    finish();
                }


            }
        }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            Toast.makeText(UpdateAccountActivity.this, "errrror", Toast.LENGTH_SHORT).show();

            }
        });



    }

    private void showInforUser() {
        Query query = mReference.orderByChild("emailUser").equalTo(mUser.getEmail());
//        Query query = mReference.child(mAuth.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //get data
                    ten = "" + ds.child("fullName").getValue();
                    diaChi = "" + ds.child("address").getValue();
                    sdt = "" + ds.child("phoneNumber").getValue();
                    email = "" + ds.child("emailUser").getValue();

                    //set data
                    editTextTenNguoiDung.setText(ten);
                    editTextDiaChi.setText(diaChi);
                    editTextSdt.setText(sdt);
                    editTextEmail.setText(email);



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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