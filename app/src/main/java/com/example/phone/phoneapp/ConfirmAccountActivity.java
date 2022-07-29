package com.example.phone.phoneapp;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.phone.R;
import com.example.phone.model.OrderDetail;
import com.example.phone.model.Orders;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ConfirmAccountActivity extends AppCompatActivity {
    private TextView txttenNguoiDung;
    private TextView txtSdt;
    private TextView txtDiaChi;
    private TextView txtEmail;
    private ImageView imgNguoiDung;
    private Button btnXacNhan, btnChinhSua, btnOk;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mData;
    private DatabaseReference mReference;
    String profileI = "imgUser";
    private StorageReference storageReference;
    static String discountId2;
    static double totalFinal;
    Dialog dialog;
    public static boolean keyReturn = false; // đang k dùng layout cho confirm
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_account);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Thông tin khách hàng");
        dialog = new Dialog(this);
        txttenNguoiDung = findViewById(R.id.edit_TenNguoiDung);
        txtDiaChi =findViewById(R.id.edit_DiaChi);
        txtSdt = findViewById(R.id.edit_Sdt);
        txtEmail = findViewById(R.id.edit_EmailNguoiDung);
        btnXacNhan = (Button)findViewById(R.id.btn_XacNhan);
        imgNguoiDung = findViewById(R.id.avt_account);
        btnChinhSua = findViewById(R.id.btn_ChinhSua);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("UserInfor");
        mData = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        StorageReference anhNguoiDungRef = storageReference.child("users/" + mAuth.getCurrentUser().getUid() + "AnhNguoiDung.jpg");
        anhNguoiDungRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(getApplicationContext()).load(uri).into(imgNguoiDung);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("ERROR ","Failed loaded uri: " + e.getMessage());
            }
        });






        Query query = mReference.orderByChild("emailUser").equalTo(mUser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    //get data
                    String ten = ""+ds.child("fullName").getValue();
                    String dc = ""+ds.child("address").getValue();
                    String sdt = ""+ds.child("phoneNumber").getValue();
                    String e = ""+ds.child("emailUser").getValue();

                    //set data
                    txttenNguoiDung.setText(ten);
                    txtDiaChi.setText(dc);
                    txtSdt.setText(sdt);
                    txtEmail.setText(e);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if(totalFinal != 0){
                    //Lưu thông tin mua hàng lên firebase
                    // Lưu vào bảng Orders
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    String orderId = mData.child("Orders").push().getKey(); // lấy kye cua firbase
                    String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                    Orders od1= new Orders(userId, orderId, currentDate, discountId2, totalFinal);
                    mData.child("Orders").child(orderId).setValue(od1);


                    //Lưu vào bảng Order Detail

                    for (int i = 0; i < MainActivity.arrayCart.size(); i++){
                        int productId = MainActivity.arrayCart.get(i).getProductId();
                        int price1 = MainActivity.arrayCart.get(i).getPrice();
                        int quantity = MainActivity.arrayCart.get(i).getQuantity();
                        OrderDetail orderDetail = new OrderDetail(orderId, productId,
                                price1, quantity);
                        mData.child("Order Detail").push().setValue(orderDetail);


                    }


                    //xóa sp trong giỏ

                    MainActivity.arrayCart.clear();

                    dialog.setContentView(R.layout.dialog_order);
                    dialog.setCanceledOnTouchOutside(false);
                    btnOk = dialog.findViewById(R.id.btnOk);
                    btnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                            Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
                            intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent2);
                            dialog.dismiss();
                            CartActivity.discount1= null;
                            InforDiscountActivity.discounts = null;
                        }
                    });

                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();

                }

            }
        });

        btnChinhSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyReturn = true;
                Intent chinhsua = new Intent(getApplicationContext(), SettingActivity.class);

                startActivity(chinhsua);
                finish();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id =  item.getItemId();
        if(android.R.id.home == id) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent1 = getIntent();
        Bundle bundle = intent1.getExtras();
        if (bundle != null) {
            discountId2 = bundle.getString("discountId2");
            totalFinal = bundle.getDouble("totalFinal");


        }

    }
}
