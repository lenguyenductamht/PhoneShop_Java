package com.example.phone.phoneapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.phone.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class StartActivity extends AppCompatActivity {
    VideoView mvideoView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("message");
//
//        myRef.setValue("Hello, World!");
        mvideoView = (VideoView) findViewById(R.id.videoItro);
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.intro );

        try {
            mvideoView.setVideoURI(uri);
        } catch (NullPointerException techmaster1)
        {
            System.out.println("Couldn't load video" + techmaster1);
        }
        mvideoView.start();


//        getSupportActionBar().hide();
        //Dùng cài đặt sau 3 giây màn hình tự chuyển
        Thread bamgio=new Thread(){
            public void run()
            {
                try {

                    sleep(3000);
                } catch (Exception e) {

                }
                finally
                {
                    mvideoView.stopPlayback();
                    mvideoView.resume();
                    Intent callMainAcc=new Intent(StartActivity.this, MainActivity.class);
                    callMainAcc.putExtra("co", "Y");
                    callMainAcc.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(callMainAcc);

                }
            }
        };
        bamgio.start();
    }
    //sau khi chuyển sang màn hình chính, kết thúc màn hình chào
    protected void onPause(){
        super.onPause();
        finish();
    }

}