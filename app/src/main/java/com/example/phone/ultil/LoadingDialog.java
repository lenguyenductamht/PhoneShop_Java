package com.example.phone.ultil;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;

import com.example.phone.R;

public class LoadingDialog {
    private  Activity activity;
    private AlertDialog alertDialog;

    public LoadingDialog(Activity activity){
        this.activity = activity;
    }
    public void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater layoutInflater = activity.getLayoutInflater();
        builder.setView(layoutInflater.inflate(R.layout.custom_dialog, null));
        builder.setCancelable(false);

        alertDialog = builder.create();

        alertDialog.show();
        //bỏ nền của background
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }
    public void dismissDialog(){
        alertDialog.dismiss();
    }
}
