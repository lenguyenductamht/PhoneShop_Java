package com.example.phone.phoneapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.phone.R;

public class SettingActivity extends AppCompatActivity {

    private Button btnSuaThongTin, btnSuaMatKhau;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_setting);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Sửa thông tin");

        btnSuaThongTin = findViewById(R.id.btn_HoSo);
        btnSuaMatKhau = findViewById(R.id.btn_DoiMK);
        if(ConfirmAccountActivity.keyReturn){
            btnSuaMatKhau.setEnabled(false);
        }else
            btnSuaMatKhau.setEnabled(true);
        btnSuaThongTin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tt = new Intent(SettingActivity.this, UpdateAccountActivity.class);
                startActivity(tt);

            }
        });

        btnSuaMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mk = new Intent(SettingActivity.this, UpdatePasswordActivity.class);
                startActivity(mk);

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
