
package com.example.phone.phoneapp;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.phone.R;
import com.example.phone.model.Cart;
import com.example.phone.model.Discounts;
import com.example.phone.myadapter.CartAdapter;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    ListView lvOrder;
    public  static TextView txtTongTien, txtTienGiamGia;
    TextView txtThongbao , txtmagiamgia1, txtFast, txtThanhToan;
    Button btnMuaHang, btnKhong, btnCo, btnChonMa, btnXoa;
    ImageView iconGioHang;
    EditText txtGiamGia;
    CartAdapter cartAdapter;
    Dialog dialog;
    public  static Discounts discount1 = null;
    private DatabaseReference mData;
    int tongtienFinal = 0;
    static int total;
    boolean flag = false;
    public static String discountId2 = "0";
    public static double totalFinal = total;
    ArrayList<Cart> carts;

    EditText edtTitle, edtDescription, edtUrl;
    Button btnShareLink, btnShareImage, btnPickVideo, btnShareVideo;
    ImageView imgHinhAnh;
    VideoView videoView;
    ShareDialog shareDialog;
    ShareLinkContent shareLinkContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cart);
        mapping();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Giỏ hàng");

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            discount1 = (Discounts) bundle.getSerializable("discounts");
            btnChonMa.setText(discount1.getDiscountId());
            flag = true;


        }


        eventUltil();
        checkData();
        cactchOnItemListView();

        orderProducts();
        setDiscount();
        btnChonMa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DiscountsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
                setDiscount();
            }
        });
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnChonMa.setText("Chọn mã giảm");
                btnXoa.setVisibility(View.INVISIBLE);
                txtTienGiamGia.setText("");
                flag = false;
                discount1 = null;
                InforDiscountActivity.discounts = null;
                txtThanhToan.setVisibility(View.INVISIBLE);
            }
        });

        txtTongTien.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setDiscount();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void setDiscount(){
        // nếu có dùng mã giảm giá.... xét đk min, max
        // dùng dùng mã giảm giá... mặc định 0
        if(discount1 != null) {
            int count1 = 0; // sl giày của hanxg đang mua
            for(int i = 0; i < carts.size(); i ++){
                if(discount1.getCateId() == carts.get(i).getCateId()
                ){
                    count1 += carts.get(i).getQuantity();
                }
            }
            if(discount1.getMinQuantity() <= count1 && count1 <= discount1.getMaxQuantity()){
                txtThanhToan.setVisibility(View.VISIBLE);
                discountId2 = discount1.getDiscountId();
                totalFinal = total - (total * discount1.getPercentage());
                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                txtTienGiamGia.setText(decimalFormat.format(totalFinal) + " Đ");

                flag = true;
            }else {
                flag = false;
                btnChonMa.setText(":(");
//                btnChonMa.setTooltipText("Không đủ điều kiện đề áp dụng mã giảm giá!");
                discount1 = null;
                InforDiscountActivity.discounts = null;
                btnXoa.setVisibility(View.INVISIBLE);
                txtTienGiamGia.setText("");
                txtThanhToan.setVisibility(View.INVISIBLE);
            }
        }
    }
    public void orderProducts() {
        btnMuaHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1. Xét có sp trong giỏ hàng k
                //2. xét đã đăng nhập hay chưa
                totalFinal = total;
                if(carts.size() > 0){
                        Intent intent = new Intent(getApplicationContext(), EnterPhoneNumber.class);
                        intent.putExtra("discountId2", discountId2);
                        intent.putExtra("totalFinal", totalFinal);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

//                        cartAdapter.notifyDataSetChanged();
//                        eventUltil();
//                        //hiện thông báo k có sp
//                        txtThongbao.setVisibility(View.VISIBLE);
//                        iconGioHang.setVisibility(View.VISIBLE);
//                        txtFast.setVisibility(View.VISIBLE);
//                        lvOrder.setVisibility(View.VISIBLE);
//                        // ẩn btn xóa giảm giá
//                        btnXoa.setVisibility(View.INVISIBLE);
//                        btnChonMa.setText("Chọn mã giảm");
//                         // vô hiệu hóa btn chọn mã
//                        btnChonMa.setEnabled(false);

                }else {
                    Toast.makeText(CartActivity.this,
                    "Giỏ hàng của bạn chưa có sản phẩm để mua!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    // bắt sự kiện click trên listView

    public void cactchOnItemListView() {
        lvOrder.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                dialog.setContentView(R.layout.delete_dialog);
                dialog.setCanceledOnTouchOutside(false);
                btnCo = dialog.findViewById(R.id.btnCo);
                btnKhong = dialog.findViewById(R.id.btnKhong);
                btnCo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btnXoa.setVisibility(View.INVISIBLE);
                        btnChonMa.setText("Chọn mã giảm");
                        if(carts.size() <= 0){
                            txtThongbao.setVisibility(View.VISIBLE);
                            iconGioHang.setVisibility(View.VISIBLE);
                            txtFast.setVisibility(View.VISIBLE);

                        }else {
                            carts.remove(position);
                            //cập nhật lại giỏ hàng
                            cartAdapter.notifyDataSetChanged();
                            eventUltil();
                            setDiscount();
                            //nếu remove hết thì sẽ hiện thông báo
                            if(MainActivity.arrayCart.size() <= 0) {
                                txtThongbao.setVisibility(View.VISIBLE);
                                iconGioHang.setVisibility(View.VISIBLE);
                                txtFast.setVisibility(View.VISIBLE);
                                btnChonMa.setEnabled(false);
                            }else {
                                txtThongbao.setVisibility(View.INVISIBLE);
                                txtFast.setVisibility(View.INVISIBLE);
                                iconGioHang.setVisibility(View.INVISIBLE);
                                cartAdapter.notifyDataSetChanged();
                                btnChonMa.setEnabled(true);
                                eventUltil();
                            }
                        }
                        dialog.dismiss();
                    }
                });
                btnKhong.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        //cập nhật lại giỏ hàng
                        cartAdapter.notifyDataSetChanged();
                        eventUltil();
                        setDiscount();
                    }
                });
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                return true;
            }
        });
    }

    public static void eventUltil() {
        int tongtien = 0;
        for(int i = 0; i < MainActivity.arrayCart.size(); i++){
            tongtien += (MainActivity.arrayCart.get(i).getPrice() *
                    MainActivity.arrayCart.get(i).getQuantity());
        }
        total = tongtien;
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        txtTongTien.setText(decimalFormat.format(tongtien) + " Đ");
        //txtTienGiamGia.setText(decimalFormat.format(tongtien) + " Đ");


    }

    public void checkData() {
        if(carts.size() <= 0){
            cartAdapter.notifyDataSetChanged();
            //hiện thông báo k có sp
            txtThongbao.setVisibility(View.VISIBLE);
            iconGioHang.setVisibility(View.VISIBLE);
            txtFast.setVisibility(View.VISIBLE);
            lvOrder.setVisibility(View.INVISIBLE);
            btnChonMa.setEnabled(false);
        }else {
            cartAdapter.notifyDataSetChanged();
            //ẩn thông báo k có sp
            txtThongbao.setVisibility(View.INVISIBLE);
            txtFast.setVisibility(View.INVISIBLE);
            iconGioHang.setVisibility(View.INVISIBLE);
            lvOrder.setVisibility(View.VISIBLE);
            btnChonMa.setEnabled(true);
        }
    }

    public  void mapping(){
        lvOrder = (ListView) findViewById(R.id.listviewGioHang);
        txtThongbao = (TextView)findViewById(R.id.textthongbao);
        txtFast = (TextView)findViewById(R.id.textFast);
        txtTongTien = (TextView)findViewById(R.id.txtTongTien);
        btnMuaHang = (Button)findViewById(R.id.btnMuaHang);
        iconGioHang = (ImageView) findViewById(R.id.icongiohang);
        cartAdapter = new CartAdapter(CartActivity.this, MainActivity.arrayCart);
        lvOrder.setAdapter(cartAdapter);
        dialog = new Dialog(this);
        mData = FirebaseDatabase.getInstance().getReference();
        //txtGiamGia = findViewById(R.id.txtGiamGia);
        btnChonMa = findViewById(R.id.btnChonMa);
        btnXoa = findViewById(R.id.btnXoa);
        btnXoa.setVisibility(View.INVISIBLE);
        txtTienGiamGia = findViewById(R.id.txtTienGiamGia);
        txtmagiamgia1 = findViewById(R.id.txtmagiamgia);
        txtThanhToan = findViewById(R.id.txtThanhToan);
        txtThanhToan.setVisibility(View.INVISIBLE);
        //txtTienGiamGia.setText(txt);
        carts = MainActivity.arrayCart;

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

    @Override
    protected void onResume() {
        //flag = false;
        super.onResume();
        if(InforDiscountActivity.discounts != null){
            btnChonMa.setText(InforDiscountActivity.discounts.getDiscountId());
            discount1 = InforDiscountActivity.discounts;
            setDiscount();
        }
        if(flag){
            btnXoa.setVisibility(View.VISIBLE);
        }
    }
}


