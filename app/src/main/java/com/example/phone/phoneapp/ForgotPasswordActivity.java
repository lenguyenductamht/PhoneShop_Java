package com.example.phone.phoneapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.phone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

public class ForgotPasswordActivity extends AppCompatActivity {
    //    private Toolbar toolbarForgotPass;
    private EditText edEmail;
    private Button btSend, btBack;
    //    private ProgressBar progressBar;
    FirebaseAuth mAuth;
    //    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

//        toolbarForgotPass = findViewById(R.id.toolbar_quenmatkhau);
        edEmail = findViewById(R.id.edit_Email);
        btSend = findViewById(R.id.bt_Gui_MK);
        btBack = findViewById(R.id.bt_QuayLai);
        mAuth = FirebaseAuth.getInstance();

        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String e = edEmail.getText().toString().trim();
                if(TextUtils.isEmpty(e)){
                    edEmail.setError("Email đang trống");
                    edEmail.requestFocus();
                } else {
                    mAuth.fetchSignInMethodsForEmail(e).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                        @Override
                        public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                            if (task.getResult().getSignInMethods().size() == 0) {
                                Toast.makeText(ForgotPasswordActivity.this, "Email không tồn tại", Toast.LENGTH_SHORT).show();
                            } else {
//                            Toast.makeText(ForgotPasswordActivity.this, "tồn tại", Toast.LENGTH_SHORT).show();
                                mAuth.sendPasswordResetEmail(edEmail.getText().toString().trim())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(ForgotPasswordActivity.this, "Đang gửi Email đến bạn...", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(ForgotPasswordActivity.this, "Lỗi...", Toast.LENGTH_SHORT).show();

                                                }

                                            }
                                        });

                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    });
                }

            }
        });

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent i = new Intent(ForgotPasswordActivity.this, LoginActivity.class);

                startActivity(i);
            }
        });

    }

}
