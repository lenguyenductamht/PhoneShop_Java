package com.example.phone.phoneapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.phone.R;
import com.example.phone.model.Customer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class CustomerActivity extends AppCompatActivity {

    private EditText edtPhonNumber, edtName, edtAddress, edtEmail;
    private Button btnAddCustomer;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mData;
    private DatabaseReference mReference;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        mapping();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        edtPhonNumber.setText(bundle.getString("phone"));
        btnAddCustomer.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if(edtName.getText().toString().isEmpty()){
                    Toast.makeText(CustomerActivity.this, "Bạn chưa nhập họ tên", Toast.LENGTH_SHORT);
                }
                else if(edtAddress.getText().toString().isEmpty()){
                    Toast.makeText(CustomerActivity.this, "Bạn chưa nhập địa chỉ", Toast.LENGTH_SHORT);

                }
                else if(edtEmail.getText().toString().isEmpty()){
                    Toast.makeText(CustomerActivity.this, "Bạn chưa nhập email", Toast.LENGTH_SHORT);
                }
                else{
                    //Lưu thông tinlên firebase
                    // Lưu vào bảng Customer
                    String ID = mData.child("Customer").push().getKey(); // lấy key cua firbase
                    Customer customer= new Customer(ID, edtPhonNumber.getText().toString(), edtName.getText().toString(), edtEmail.getText().toString(), edtAddress.getText().toString());
                    mData.child("Customer").child(ID).setValue(customer);
                    Toast.makeText(CustomerActivity.this, "Thêm thành công", Toast.LENGTH_SHORT);
                    Intent intent1 = new Intent(CustomerActivity.this, CustomerInfoActivity.class);
                    intent1.putExtra("phone", edtPhonNumber.getText().toString());
                    startActivity(intent1);


                }
            }
        });
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home)
        {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public void mapping(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.them_khach_hang);

        edtPhonNumber = (EditText) findViewById(R.id.sodienthoaiKH);
        edtName = (EditText) findViewById(R.id.hotenKH);
        edtAddress = (EditText) findViewById(R.id.diachiKH);
        edtEmail = (EditText) findViewById(R.id.emailKH);
        btnAddCustomer = (Button) findViewById(R.id.btnThemKH);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("UserInfor");
        mData = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
    }
}