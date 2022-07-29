package com.example.phone.phoneapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.phone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdatePasswordActivity extends AppCompatActivity {

    private EditText editTextMatKhauMoi, editTextNhapLaiMKMoi;
    private Button btnXacNhan;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;

    String matKhauMoi;
    String nhapLaiMK;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_st_pw);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Đổi mật khẩu");


//        editTextMatKhauCu = findViewById(R.id.edit_MatKhauCu);
        editTextMatKhauMoi = findViewById(R.id.edit_MatKhauMoi);
        editTextNhapLaiMKMoi = findViewById(R.id.edit_XacNhanMK);
        btnXacNhan = findViewById(R.id.btn_XacNhan);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
//        mReference = FirebaseDatabase.getInstance().getReference("UserInfor");

//        mReference.orderByChild("emailUser").endAt(mUser.getEmail());


        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mUser != null){
                    matKhauMoi = editTextMatKhauMoi.getText().toString().trim();
                    nhapLaiMK = editTextNhapLaiMKMoi.getText().toString().trim();
                    if(TextUtils.isEmpty(matKhauMoi)){
                        editTextMatKhauMoi.setError("Bạn cần nhập mật khẩu mới");
                        editTextMatKhauMoi.requestFocus();
                    } else if(matKhauMoi.length() < 6){
                        editTextMatKhauMoi.setError("Mật khẩu phải trên 6 ký tự");
                        editTextMatKhauMoi.requestFocus();
                    }
                    else if(TextUtils.isEmpty(nhapLaiMK)){
                        editTextNhapLaiMKMoi.setError("Bạn cần nhập lại mật khẩu mới");
                        editTextNhapLaiMKMoi.requestFocus();
                    } else {
                        if(!matKhauMoi.equals(nhapLaiMK)){
                            Toast.makeText(UpdatePasswordActivity.this, "Mật khẩu không trùng khớp!", Toast.LENGTH_SHORT).show();

                        } else {
                            mUser.updatePassword(matKhauMoi).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(UpdatePasswordActivity.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
//                                Intent i = new Intent(UpdatePasswordActivity.this, AccountActivity.class);
//                                startActivity(i);
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(UpdatePasswordActivity.this, ""+ e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    }


                }

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
