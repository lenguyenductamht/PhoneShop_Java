package com.example.phone.phoneapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.phone.R;
import com.example.phone.model.Discounts;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InforDiscountActivity extends AppCompatActivity {
    public  static Discounts discounts;
    TextView txtDescription, txtDiscountId, txtStartDate, endDate;
    Button btnSuDung;
    ImageView image2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infor_discount);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Discount");

        mapping();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            discounts = (Discounts) bundle.getSerializable("discountInfor");
            txtStartDate.setText(discounts.getStartDate());
            txtDiscountId.setText(discounts.getDiscountId());

            String[] splitted = discounts.getDescription().split("=");

            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < splitted.length; i++){
                //s += splitted[i] + System.getProperty("line.separator");
                stringBuilder.append(splitted[i]);
                stringBuilder.append(System.getProperty("line.separator"));
            }
            if(stringBuilder != null)
                txtDescription.setText(stringBuilder);
            //txtDescription.setText("gdgfdgd" + System.getProperty("line.separator") + "fdfgdfgdfg");

            endDate.setText(discounts.getEndDate());
            Picasso.with(getApplicationContext()).load(discounts.getImage())
                    .into(image2);

        }

        btnSuDung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.arrayCart.size() > 0){
                    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        if(!df.parse(discounts.getStartDate()).before(new Date())){ // chưa tới ngày áp dụng khuyến mãi
                            Toast.makeText(InforDiscountActivity.this, "Chưa tới ngày áp dụng mã.Vui lòng chọn mã khác", Toast.LENGTH_SHORT).show();
                            //finish();
                            Intent intent = new Intent(getApplicationContext(), DiscountsActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getApplicationContext().startActivity(intent);
                        }else {
                            //finish();
                            Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                            intent.putExtra("discounts", discounts);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getApplicationContext().startActivity(intent);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(InforDiscountActivity.this, "Bạn chỉ được áp dụng mã khuyến mãi khi giỏ hàng có sản phẩm!!", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private void mapping() {
        txtDiscountId = findViewById(R.id.txtDiscountId);
        txtStartDate = findViewById(R.id.startDate);
        endDate = findViewById(R.id.endDate);
        btnSuDung = findViewById(R.id.btnSuDung);
        txtDescription = findViewById(R.id.txtDescription);
        image2 = findViewById(R.id.image2);
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
