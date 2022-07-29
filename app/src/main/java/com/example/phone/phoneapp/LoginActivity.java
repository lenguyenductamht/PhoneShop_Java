package com.example.phone.phoneapp;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.example.phone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 101010;
    public static FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private EditText emailID, passWord;
    private Button btnSignIn;
    private TextView tvSignUp, tvForgotPass;
    private ProgressDialog dg;

    private ImageView imgLogin;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    String inf = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Đăng nhập");

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            inf = bundle.getString("inf");

        }
        emailID =findViewById(R.id.edittext_taikhoan);
        passWord = findViewById(R.id.edittext_matkhau);
        btnSignIn = (Button)findViewById(R.id.btnDangNhap);
        tvSignUp  = findViewById(R.id.dangky);
        tvForgotPass = findViewById(R.id.quenmatkhau);

        mAuth =FirebaseAuth.getInstance();
        imgLogin = findViewById(R.id.imgFinger);



        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                Log.d("MY_APP_TAG", "Ứng dụng có thể xác thực vân tay");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                //Log.e("MY_APP_TAG", "No biometric features available on this device.");
                Toast.makeText(LoginActivity.this, "Thiết bị không hỗ trợ vân tay", Toast.LENGTH_SHORT);
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                //Log.e("MY_APP_TAG", "Biometric features are currently unavailable.");
                Toast.makeText(LoginActivity.this, "Vân tay không hợp lệ", Toast.LENGTH_SHORT);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                // Prompts the user to create credentials that your app accepts.
                final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
                enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BIOMETRIC_STRONG | DEVICE_CREDENTIAL);
                startActivityForResult(enrollIntent, REQUEST_CODE);
                break;
        }

        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(LoginActivity.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),
                        "Lỗi xác thực: " + errString, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                String email = "lenguyenductamht@gmail.com";
                String pass = "123456";
                mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        dg.dismiss();
                        if(!task.isSuccessful()){
                            Toast.makeText(LoginActivity.this,"Email hoặc Mật Khẩu sai!! Xin thử lại.",Toast.LENGTH_SHORT).show();
                            Toast.makeText(LoginActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();


                        } else {
                            dg.dismiss();
                            if (mAuth.getCurrentUser().isEmailVerified() == false) {
                                Toast.makeText(LoginActivity.this,"Bạn chưa xác nhận Email!!",Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                if(!inf.trim().isEmpty()){
                                    mUser = FirebaseAuth.getInstance().getCurrentUser();
                                    finish();
                                }else {
                                    Toast.makeText(LoginActivity.this,"Đăng nhập thành công!!",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("login", "Y");
                                    startActivity(intent);
                                }
                                finish();
                            }
                            finish();


                        }
                    }
                });
                Toast.makeText(getApplicationContext(),
                        "Xác thực thành công", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Lỗi xác thực",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Xác thực vân tay để đăng nhập")
                .setSubtitle("Đăng nhập bằng vân tay")
                .setNegativeButtonText("Nhập mật khẩu")
                .build();

        // Prompt appears when user clicks "Log in".
        // Consider integrating with the keystore to unlock cryptographic operations,
        // if needed by your app.
        imgLogin.setOnClickListener(view -> {
            biometricPrompt.authenticate(promptInfo);
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailID.getText().toString().trim();
                String pass = passWord.getText().toString().trim();
                if(email.isEmpty() && pass.isEmpty()){
                    Toast.makeText(LoginActivity.this,"Bạn chưa nhập email và mật khẩu!",Toast.LENGTH_SHORT).show();
                }
                else if(pass.isEmpty()){
                    passWord.setError("Bạn chưa nhập mật khẩu!");
                    passWord.requestFocus();
                }
                else if(email.isEmpty() ){
                    emailID.setError("Bạn chưa nhập Email!");
                    emailID.requestFocus();
                } else if(!email.isEmpty() && pass.length() <6){
                    Toast.makeText(LoginActivity.this,"Mật khẩu bạn dưới 6 ký tự!",Toast.LENGTH_SHORT).show();
                }
                else if(!(email.isEmpty() && pass.isEmpty())){
                    dg.setMessage("Đang vào....");
                    dg.show();
                    mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            dg.dismiss();
                            if(!task.isSuccessful()){
                                Toast.makeText(LoginActivity.this,"Email hoặc Mật Khẩu sai!! Xin thử lại.",Toast.LENGTH_SHORT).show();
                                Toast.makeText(LoginActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();


                            } else {
                                dg.dismiss();
                                if (mAuth.getCurrentUser().isEmailVerified() == false) {
                                    Toast.makeText(LoginActivity.this,"Bạn chưa xác nhận Email!!",Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    if(!inf.trim().isEmpty()){
                                        mUser = FirebaseAuth.getInstance().getCurrentUser();
                                        finish();
                                    }else {
                                        Toast.makeText(LoginActivity.this,"Đăng nhập thành công!!",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.putExtra("login", "Y");
                                        startActivity(intent);
                                    }
                                    finish();
                                }
                                finish();


                            }
                        }
                    });


                }
                else{
                    dg.dismiss();
                    Toast.makeText(LoginActivity.this,"Lỗi!!",Toast.LENGTH_SHORT).show();
                }


            }
        });
        dg = new ProgressDialog(this);


        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });


        tvForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == android.R.id.home)
        {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

