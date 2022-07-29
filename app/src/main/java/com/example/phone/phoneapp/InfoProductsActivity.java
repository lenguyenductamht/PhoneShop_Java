package com.example.phone.phoneapp;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.example.phone.R;
import com.example.phone.model.Cart;
import com.example.phone.model.Products;
import com.example.phone.myadapter.ImageAdapter;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

public class InfoProductsActivity extends AppCompatActivity {
    private static final int REQUEST_PHOTO_CODE = 1000;
    TextView gia, thongtin, tenSp, manHinh, heDieuHanh, CamSau, CamTruoc, chip, Ram, Rom, Sim, Pin, soLuongCon;
    ArrayList<Products> arr;
    ImageAdapter imageAdapter;
    int id = -1;
    Products products;
    private DatabaseReference mData;
    private Button btntru, btncong, btnDh, btnMuaNgay;
    private CallbackManager callbackManager;
    private TextView txtsoluong;
    private ShareDialog shareDialog;
    public ShareButton shareButton;
    static public int slConLai;
    private ImageView imgViewDetailPro;
    private String proDuctID;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //fullscreen
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_infoproducts);
        mapping1();
        setImage(this,imgViewDetailPro, String.valueOf(products.getProductId()));
        shareButton.setEnabled(true);

        //Chia sẻ
        shareButton.setOnClickListener(v -> {
            File file = SaveImage();
            if(file!=null){
                share(file);
            }
        });


        //congsl
        btncong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btntru.setEnabled(true);
                int sl = Integer.parseInt(txtsoluong.getText().toString()) + 1;
                txtsoluong.setText(String.valueOf(sl));
                btncong.setVisibility(View.VISIBLE);
                btntru.setVisibility(View.VISIBLE);
                if(sl >= 10 || sl >= slConLai) {
                    btncong.setEnabled(false);
                    btntru.setEnabled(true);

                }
            }
        });
        //trusl
        btntru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btncong.setEnabled(true);
                int sl = Integer.parseInt(txtsoluong.getText().toString()) -1;
                btncong.setVisibility(View.VISIBLE);
                btntru.setVisibility(View.VISIBLE);
                txtsoluong.setText(String.valueOf(sl));
                if(sl <= 1) {
                    btntru.setEnabled(false);
                    btncong.setEnabled(true);
                }
            }
        });
        txtsoluong.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(Integer.parseInt(txtsoluong.getText().toString()) >= 10 || Integer.parseInt(txtsoluong.getText().toString())>=slConLai){
                    btncong.setEnabled(false);
                }
                if(Integer.parseInt(txtsoluong.getText().toString()) <= 1){
                    btntru.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        AddCartProduct();

    }

    private void share(File file) {
        Uri uri;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
            uri = FileProvider.getUriForFile(this, getPackageName()+".provider", file);

        }else{
            uri=Uri.fromFile(file);
        }

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_SUBJECT, "ScreenShot");
        intent.putExtra(Intent.EXTRA_TEXT, "Hello World");
        intent.putExtra(Intent.EXTRA_STREAM, uri);

        try {
            startActivity(Intent.createChooser(intent, "Share using"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
            shareButton.performClick();
        }
        else{
            Toast.makeText(this, "Truy cập bị từ chối", Toast.LENGTH_SHORT).show();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private File SaveImage() {
        if(!CheckPermission()){
            return null;
        }
        try {
            String path= Environment.getExternalStorageDirectory().toString()+"/AppName";
            File fileDir = new File(path);
            if(!fileDir.exists())
                fileDir.mkdir();
            String mPath=path+"/ScreenShot_"+new Date().getTime()+".png";
            Bitmap bitmap = screenShot();
            File file = new File(mPath);
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            Toast.makeText(this, "Đã lưu ảnh", Toast.LENGTH_LONG).show();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Bitmap screenShot() {
        View v = findViewById(R.id.activity_info_products);
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(),v.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas= new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }

    private boolean CheckPermission() {
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permission!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            return false;
        }
        return true;
    }
    public static boolean checkImageExists(Context context, String name)
    {
        try{
            ContextWrapper cw = new ContextWrapper(context);
            File directory = cw.getDir("AppName", Context.MODE_PRIVATE);
            File mypath = new File(directory, name);
            if(mypath.exists())
                return true;
            return false;
        } catch (Exception ex){
            return false;
        }
    }

    public static void saveImage(Context context, Bitmap bitmap, String name){
        FileOutputStream fileOutputStream;
        try {
            ContextWrapper cw = new ContextWrapper(context);
            File directory = cw.getDir("AppName", Context.MODE_PRIVATE);
            File mypath = new File(directory, name);
            fileOutputStream = new FileOutputStream(mypath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void setImage(Context context, ImageView imageView, String name){
        if(checkImageExists(context, name)){
            imageView.setImageBitmap(loadImageBitmap(context,name));
        }
        else{
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference islandRef = storageRef.child("Products/"+name+".jpg");
            final long ONE_MEGABYTE = 1024 * 1024 *5; islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    saveImage(context, bitmap, name);
                    imageView.setImageBitmap(loadImageBitmap(context,name));
                }
            }).addOnFailureListener(new OnFailureListener() {
                    @Override public void onFailure(@NonNull Exception exception) {
                        imageView.setImageBitmap(loadImageBitmap(context,"no_image.jpg"));
                    }
                });
        }
    }
    public static Bitmap loadImageBitmap(Context context,String name){
        FileInputStream fileInputStream;
        Bitmap bitmap = null;
        try{ ContextWrapper cw = new ContextWrapper(context);
            File directory = cw.getDir("AppName", Context.MODE_PRIVATE);
            File mypath = new File(directory, name);
            fileInputStream = new FileInputStream(mypath);
            bitmap = BitmapFactory.decodeStream(fileInputStream);
            fileInputStream.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public void AddCartProduct(){
        btnDh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.mUser != null || LoginActivity.mUser != null){
                    if(Integer.parseInt(txtsoluong.getText().toString()) > slConLai) {
                        Toast.makeText(InfoProductsActivity.this, "Không đủ sản phẩm", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if(themVaoGioHang()){
                            Toast.makeText(InfoProductsActivity.this, "Thêm vào giỏ hàng thành công",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(InfoProductsActivity.this, "Lỗi",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                }else {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.putExtra("inf", "yes");
                    startActivity(intent);
                }
            }
        });

        btnMuaNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.mUser != null || LoginActivity.mUser != null){
                    if(Integer.parseInt(txtsoluong.getText().toString()) > slConLai) {
                        Toast.makeText(InfoProductsActivity.this, "Không đủ sản phẩm", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if(themVaoGioHang()){
                            Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(InfoProductsActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                        }

                    }
                }else {
                    Intent intent = new Intent(InfoProductsActivity.this, LoginActivity.class);
                    intent.putExtra("inf", "yes");
                    startActivity(intent);
                }

            }
        });
    }

    public boolean themVaoGioHang(){
        //Order
        try {
            boolean exists = false;
            if(MainActivity.arrayCart.size() > 0){
                int soluong = Integer.parseInt(txtsoluong.getText().toString());
                //kiểm tra nếu sp đã đặt thì tăng sl
                for(int i = 0; i < MainActivity.arrayCart.size(); i++){
                    if(MainActivity.arrayCart.get(i).getProductId() == (products.getProductId())){
                        // cap nhat tong sl
                        if((MainActivity.arrayCart.get(i).getQuantity() + soluong) > slConLai)
                        {
                            Toast.makeText(InfoProductsActivity.this, "Không đủ sản phẩm", Toast.LENGTH_SHORT);
                            return false;
                        }
                        else
                        {
                            MainActivity.arrayCart.get(i).setQuantity(
                                    MainActivity.arrayCart.get(i).getQuantity() + soluong);
                            //MainActivity.arrayCart.get(i).setQuantity(soluong);
                            //k đặt quá 10sp
                            if(MainActivity.arrayCart.get(i).getQuantity() > 10)
                                Toast.makeText(InfoProductsActivity.this, "Chỉ được mua tối đa 10 sản phẩm", Toast.LENGTH_SHORT);
                            // cap nhat tong gia tien
//                                MainActivity.arrayCart.get(i).setPrice(
//                                        products.getPrice() * MainActivity.arrayCart.get(i).getQuantity());
                            exists = true;
                        }

                    }
                }
                if(exists == false){
                    int sl = Integer.parseInt(txtsoluong.getText().toString());
                    //int giaMoi = sl * products.getPrice();
                    MainActivity.arrayCart.add(new Cart(products.getProductId(),
                            products.getProductName(), products.getImgProduct(),
                            products.getPrice(), sl, products.getBrandId()));
                }
            }else {
                int sl = Integer.parseInt(txtsoluong.getText().toString());
                //int giaMoi = sl * products.getPrice();
                MainActivity.arrayCart.add(new Cart(products.getProductId(),
                        products.getProductName(), products.getImgProduct(),
                        products.getPrice(), sl, products.getBrandId()));
            }
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public void mapping1(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mData = FirebaseDatabase.getInstance().getReference();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            products = (Products) bundle.getSerializable("productInfor");

            mapping(products.getProductName(), products.getDescription(), products.getPrice(), products.getScreen(), products.getOS(), products.getBackCam(), products.getFrontCam(), products.getChip(), products.getRam(), products.getRom(), products.getSim(), products.getBackCam(), products.getQuantity());
            slConLai = products.getQuantity();
            proDuctID = String.valueOf(products.getProductId());
        }
        imgViewDetailPro = findViewById(R.id.imageDetail);
        btntru = (Button) findViewById(R.id.btntru);
        btntru.setEnabled(false);
        btncong = (Button) findViewById(R.id.btncong);
        txtsoluong = (TextView) findViewById(R.id.txtsoluong);
        btnDh = (Button) findViewById(R.id.btn_themGioHang);
        btnMuaNgay = findViewById(R.id.btn_muangay);
        callbackManager = CallbackManager.Factory.create();
        shareButton = findViewById(R.id.shareButton);
    }

    public void mapping(String name, String des, int pri, String MH, String OS, String BackCam, String FrontCam, String chipSet, String ram, String rom, String sim, String pin, int slCon){

        gia = findViewById(R.id.gia);
        tenSp = findViewById(R.id.tensanpham);
        thongtin = findViewById(R.id.thongtin);
        manHinh = findViewById(R.id.tvScreenInfo);
        heDieuHanh = findViewById(R.id.tvOSInfo);
        CamSau = findViewById(R.id.tvBackCamInfo);
        CamTruoc = findViewById(R.id.tvFrontCamInfo);
        chip = findViewById(R.id.tvChipInfo);
        Ram = findViewById(R.id.tvRamInfo);
        Rom = findViewById(R.id.tvRomInfo);
        Sim = findViewById(R.id.tvSimInfo);
        Pin = findViewById(R.id.tvPinInfo);
        soLuongCon = findViewById(R.id.tvQuantity);
        String[] splitted = des.split("=");

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < splitted.length; i++){
            //s += splitted[i] + System.getProperty("line.separator");
            stringBuilder.append(splitted[i]);
            stringBuilder.append(System.getProperty("line.separator"));
        }
        if(stringBuilder != null)
            thongtin.setText(stringBuilder);

        tenSp.setText(name);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        gia.setText(decimalFormat.format(pri));
        manHinh.setText(MH);
        heDieuHanh.setText(OS);
        CamSau.setText(BackCam);
        CamTruoc.setText(FrontCam);
        chip.setText(chipSet);
        Ram.setText(ram);
        Rom.setText(rom);
        Sim.setText(sim);
        Pin.setText(pin);
        soLuongCon.setText("Còn "+slCon+" sản phẩm");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_cart:
                //hiện giỏ hàng
                Intent intents = new Intent(this, CartActivity.class);
                startActivity(intents);
                return true;
            case android.R.id.home:
                this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Giỏ hàng
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navbottom_items,menu);
        return true;
    }

    private class DEFAULT {
    }
}
