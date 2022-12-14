package com.example.phone.phoneapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.phone.R;
import com.example.phone.model.UserInfor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private EditText txtemail;
    private EditText txtpassword, txtHoTen, txtSdt, txtDiaChi, txtNhapLaiPass;

    private Button btnSignUp;

    private FirebaseAuth mfirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;

    private TextView vSignUp;
    private ProgressDialog dg;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        txtemail = findViewById(R.id.signin_email);
        txtpassword = findViewById(R.id.signin_matkhau);
        txtDiaChi = findViewById(R.id.signin_diachi);
        txtSdt = findViewById(R.id.signin_sodienthoai);
        txtHoTen = findViewById(R.id.signin_hoten);
        txtNhapLaiPass = findViewById(R.id.signin_nhaplaimatkhau);
//        imgAnhNguoiDung = (ImageView) findViewById(R.id.imgNguoiDung);

        btnSignUp = (Button)findViewById(R.id.btnDangKi);
        vSignUp = findViewById(R.id.dangnhap);

        mfirebaseAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference("UserInfor");
        mFirebaseUser = mfirebaseAuth.getCurrentUser();



        btnSignUp.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                final String email = txtemail.getText().toString().trim();
                final String pass = txtpassword.getText().toString().trim();
                final String hoTen = txtHoTen.getText().toString().trim();
                final String diaChi = txtDiaChi.getText().toString().trim();
                final String i = "";
                final String sdt = txtSdt.getText().toString().trim();
                final String nhapLaiPass = txtNhapLaiPass.getText().toString().trim();


                if(TextUtils.isEmpty(hoTen) && TextUtils.isEmpty(diaChi) && TextUtils.isEmpty(sdt)
                        && TextUtils.isEmpty(pass) && TextUtils.isEmpty(email) && TextUtils.isEmpty(nhapLaiPass)) {
                    Toast.makeText(SignUpActivity.this,"B???n ??ang ????? tr???ng th??ng tin!",Toast.LENGTH_SHORT).show();

                } else if(TextUtils.isEmpty(hoTen)){
                    txtHoTen.setError("T??n ng?????i d??ng ??ang tr???ng");
                    txtHoTen.requestFocus();

                } else if(TextUtils.isEmpty(diaChi)){
                    txtDiaChi.setError("?????a ch??? ??ang tr???ng");
                    txtDiaChi.requestFocus();

                } else if(TextUtils.isEmpty(sdt)) {
                    txtSdt.setError("S??? ??i???n tho???i b???n ??ang tr???ng");
                    txtSdt.requestFocus();

                } else if(checkNumber(sdt) == false){
                    txtSdt.setError("S??? ??i???n tho???i y??u c???u ph???i l?? s???");
                    txtSdt.requestFocus();

                }  else if(TextUtils.isEmpty(email)){
                    txtemail.setError("B???n ch??a nh???p email! ");
                    txtemail.requestFocus();

                } else if(TextUtils.isEmpty(pass)){
                    txtpassword.setError("B???n ch??a nh???p m???t kh???u!");
                    txtpassword.requestFocus();

                } else if(TextUtils.isEmpty(nhapLaiPass)){
                    txtNhapLaiPass.setError("B???n ch??a nh???p Nh???p l???i m???t kh???u");
                    txtNhapLaiPass.requestFocus();

                } else if(pass.length() < 6){
                    Toast.makeText(SignUpActivity.this,"M???t kh???u ph???i tr??n 6 k?? t???!",Toast.LENGTH_SHORT).show();


                } else if(!email.isEmpty() && !pass.isEmpty()){
//                    if(CheckInternet.haveNetworkConnection(getApplicationContext())){
                        dg.setMessage("??ang T???o T??i Kho???n...");
                        dg.show();
                    final Task<SignInMethodQueryResult> userInfor = mfirebaseAuth.fetchSignInMethodsForEmail(txtemail.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                        @Override
                        public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                            if (task.getResult().getSignInMethods().size() == 0) {
//                                    Toast.makeText(ForgotPasswordActivity.this, "Email kh??ng t???n t???i", Toast.LENGTH_SHORT).show();
                                mfirebaseAuth.createUserWithEmailAndPassword(email, pass).
                                        addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (!task.isSuccessful()) {
                                                    dg.dismiss();
                                                    Toast.makeText(SignUpActivity.this, task.getException().getMessage(),
                                                            Toast.LENGTH_LONG).show();

                                                } else {
                                                    if (pass.equals(nhapLaiPass)) {
                                                        dg.dismiss();
                                                        mFirebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    UserInfor userInfor = new UserInfor(hoTen, diaChi, sdt, email, i);

                                                                    FirebaseDatabase.getInstance().getReference("UserInfor")
                                                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                                            .setValue(userInfor).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            Toast.makeText(SignUpActivity.this, "T???o t??i kho???n th??nh c??ng ^^. Xin ki???m tra v?? x??c nh???n Email.",
                                                                                    Toast.LENGTH_SHORT).show();
                                                                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);

//                                                                          startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                                                                            startActivity(intent);
                                                                            finish();

                                                                        }
                                                                    });
                                                                } else {
                                                                    dg.dismiss();
                                                                    Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                                                                }
                                                            }
                                                        });

                                                    } else {
                                                        dg.dismiss();
                                                        Toast.makeText(SignUpActivity.this, "M???t kh???u kh??ng tr??ng kh???p!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                            }
                                        });
                            } else {
                                dg.dismiss();
                                Toast.makeText(SignUpActivity.this, "Email ???? ???????c s??? d???ng!", Toast.LENGTH_SHORT).show();

                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    });
                    //}

                } else{
                    dg.dismiss();
                    Toast.makeText(SignUpActivity.this, "L???i. Xin th??? l???i!", Toast.LENGTH_SHORT).show();

                }
            }
        });

        dg = new ProgressDialog(this);

        vSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });




    }

    private Boolean checkNumber(String so){
        Boolean check = false;
        String number = "\\d*\\.?\\d+";
        CharSequence inputStr = so;
        Pattern pattern = Pattern.compile(number, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {

            check = true;
        }
        return check;

    }


}
