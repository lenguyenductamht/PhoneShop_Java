package com.example.phone.phoneapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.phone.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AccountActivity extends AppCompatActivity {

    private TextView txttenNguoiDung;
    private TextView txtSdt;
    private TextView txtDiaChi;
    private TextView txtEmail;
    private ImageView imgNguoiDung;
    private Button btnAnhNguoiDung, btnChinhSua;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    String profileI = "imgUser";
    public static String tenNhanVien;

    private StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Thông tin cá nhân");

        txttenNguoiDung = findViewById(R.id.edit_TenNguoiDung);
        txtDiaChi =findViewById(R.id.edit_DiaChi);
        txtSdt = findViewById(R.id.edit_Sdt);
        txtEmail = findViewById(R.id.edit_EmailNguoiDung);
        btnAnhNguoiDung = (Button)findViewById(R.id.btn_XacNhan);
        imgNguoiDung = findViewById(R.id.avt_account);
        btnChinhSua = findViewById(R.id.btn_ChinhSua);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("UserInfor");

        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference anhNguoiDungRef = storageReference.child("users/" + mAuth.getCurrentUser().getUid() + "AnhNguoiDung.jpg");
        anhNguoiDungRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(AccountActivity.this).load(uri).into(imgNguoiDung);
//                imgNguoiDung.setImageURI(uri);
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

                    tenNhanVien = ten;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnAnhNguoiDung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mở gallery
                Intent opGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                opGallery.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // để clear acivity khi back
                opGallery.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivityForResult(opGallery, 1000);
            }
        });

        btnChinhSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chinhsua = new Intent(AccountActivity.this, SettingActivity.class);
                startActivity(chinhsua);
                finish();

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                Uri imgUrl = data.getData();
                //imgNguoiDung.setImageURI(imgUrl);

                upLoadImgToFirebase(imgUrl);
                imgNguoiDung.setImageURI(imgUrl);

            }
        }


    }

    private void upLoadImgToFirebase(final Uri i){
        //upload img từ firebase
        final StorageReference s2 = storageReference.child("users/" + mAuth.getCurrentUser().getUid() + "AnhNguoiDung.jpg");

        s2.putFile(i).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                final Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                final Uri downUri = uriTask.getResult();
                if(uriTask.isSuccessful()){
                    HashMap<String, Object> results = new HashMap<>();
                    results.put(profileI, downUri.toString());

                    mReference.child(mAuth.getUid()).updateChildren(results)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
//                                    imgNguoiDung.setImageURI(downUri);

                                    Toast.makeText(AccountActivity.this, "Đang thay đổi ảnh người dùng", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AccountActivity.this, "ERROR", Toast.LENGTH_SHORT).show();

                        }
                    });
                }else{
                    Toast.makeText(AccountActivity.this, "ERROR", Toast.LENGTH_SHORT).show();

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AccountActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();

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
}

