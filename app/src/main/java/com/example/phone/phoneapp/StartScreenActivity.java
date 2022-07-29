package com.example.phone.phoneapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.phone.R;

public class StartScreenActivity extends AppCompatActivity {
    Animation topAnim, bottomAnim;
    ImageView img, img2;
    private static int splash_sreen = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        img = findViewById(R.id.logoStart);
        img2 = findViewById(R.id.logoStart2);

        img.setAnimation(topAnim);
        img2.setAnimation(bottomAnim);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent callMainAcc = new Intent(getApplicationContext(), MainActivity.class);
                callMainAcc.putExtra("co", "Y"); // để clear acivity khi back
                callMainAcc.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getApplicationContext().startActivity(callMainAcc);
                finish();
            }
        },splash_sreen);


    }
}
