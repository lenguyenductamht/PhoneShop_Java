package com.example.phone.phoneapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.phone.R;
import com.example.phone.model.Cart;
import com.example.phone.model.Brands;
import com.example.phone.model.Discounts;
import com.example.phone.model.Products;
import com.example.phone.myadapter.ImageAdapter;
import com.example.phone.myadapter.ProductAdapter;
import com.example.phone.myadapter.RecyclerViewAdapter;
import com.example.phone.ultil.LoadingDialog;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.OnItemClickListener,
                NavigationView.OnNavigationItemSelectedListener{
    String id = "";
    String name = "";
    String cateId = "";
    String description = "";
    String backCam = "";
    String battery = "";
    String chip = "";
    String color = "";
    String frontCam = "";
    String ram = "";
    String rom = "";
    String screen = "";
    String sim = "";
    public static boolean anXacNhan = false;
    int price = 0;
    String img = "";
    int index = 0;
    NavigationView navView;
    VideoView mvideoView;
    View headerview;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    private BottomNavigationView mainNav;
    private FrameLayout mainFrame;
    private SearchView searchView;
    private ImageView imgThongBao;
    private TextView txtThongBao;
    private ImageView imageNav;
    public static TextView tenKH, emailKH;
    public static String userId = "";
    ArrayList<Brands> imageURLs;
    ArrayList<Products> arrayProduct;
    ArrayList<Discounts> arrayImageDiscount;
    ImageAdapter adapter;
//    NavigationView navView;
    ProductAdapter productAdapter;
    RecyclerViewAdapter recyclerViewAdapter;
    RecyclerView recyclerView, listProduct;
    ViewPager mViewPager;
    String co = "", login = "";
//    View headerview;
    private FirebaseAuth mAuth;
    public static FirebaseUser mUser;
    private DatabaseReference mData;
    private DatabaseReference mReference;
    private ProgressDialog dg;
    LoadingDialog loadingDialog;
    public static  ArrayList<Cart> arrayCart;
    private StorageReference storageReference;
    boolean isResume = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isResume = true;
        //fullscreen
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        mapping();
        // đóng và mở menu
        t = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);

        dl.addDrawerListener(t);
        t.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nv.setNavigationItemSelectedListener(this);

        //


        //load video
//        public void loadVideo(){
//
//        }
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.giay);
        try {
            mvideoView.setVideoURI(uri);
            mvideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp){
                    mp.setLooping(true);
                }
            });
        } catch (NullPointerException techmaster1) {
            System.out.println("Loi khong load duoc video" + techmaster1);
        }
        mvideoView.start();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            co = bundle.getString("co");
            login = bundle.getString("login");
            if (co != null) {
                loadingDialog = new LoadingDialog(this);
                loadingDialog.startLoadingDialog();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //hide the dialog after 4550 milliseconds
                        loadingDialog.dismissDialog();
                    }
                }, 3000);
            }
            if(login != null){
                mUser = FirebaseAuth.getInstance().getCurrentUser();
                mAuth =FirebaseAuth.getInstance();
                mReference = FirebaseDatabase.getInstance().getReference("UserInfor");
                loadInforUser();
            }
        }

        //get cate
        mData.child("Category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dt : dataSnapshot.getChildren()){
                    Brands cate = dt.getValue(Brands.class);
                    imageURLs.add(cate);
                    recyclerViewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Error:" +
                        databaseError.getMessage() , Toast.LENGTH_SHORT).show();
            }
        });
        //get product
        mData.child("Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayProduct.clear();
                for (DataSnapshot dt : dataSnapshot.getChildren()){
                    Products pro = dt.getValue(Products.class);
                    arrayProduct.add(pro);
                    productAdapter.notifyDataSetChanged();


                }
                checkData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Error:" +
                        databaseError.getMessage() , Toast.LENGTH_SHORT).show();
            }
        });
        if(searchView != null){
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {

                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    mvideoView.setVisibility(View.INVISIBLE);
                    search(newText);
                    return true;
                }
            });
        }

        headerview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUser != null) {
                    // User is signed in

                    Intent intent = new Intent(MainActivity.this, AccountActivity.class);
                    startActivity(intent);
                } else {
                    // No user is signed in
                    finish();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    //intent.putExtra("in", "ye");
                    startActivity(intent);
                }

            }
        });
        loadImageDiscount();
    }

    public void loadImageDiscount() {
        mData.child("Discounts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dt : dataSnapshot.getChildren()){
                    Discounts disc = dt.getValue(Discounts.class);
                    arrayImageDiscount.add(disc);
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Error:" +
                        databaseError.getMessage() , Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkData() {

        if(arrayProduct.size() > 0){ // ẩn
            txtThongBao.setVisibility(View.INVISIBLE);
            imgThongBao.setVisibility(View.INVISIBLE);
            listProduct.setVisibility(View.VISIBLE);
        }else {
            txtThongBao.setText("Không tìm thấy sản phẩm nào!!");
            txtThongBao.setVisibility(View.VISIBLE);
            imgThongBao.setVisibility(View.VISIBLE);
            listProduct.setVisibility(View.INVISIBLE);
        }
    }

    public void search(String str) {
        ArrayList<Products> arraySearch = new ArrayList<>();

        for(Products p : arrayProduct){
            if(p.getProductName().toLowerCase().contains(str.toLowerCase())){
                arraySearch.add(p);
            }
        }

        if(arraySearch.size() > 0){
            txtThongBao.setVisibility(View.INVISIBLE);//ẩn
            imgThongBao.setVisibility(View.INVISIBLE);
            listProduct.setVisibility(View.VISIBLE);
            ProductAdapter allProAdapter = new ProductAdapter(getApplicationContext(), arraySearch);
            listProduct.setHasFixedSize(true);
            listProduct.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
            listProduct.setAdapter(allProAdapter);
        }else {
            txtThongBao.setText("Không tìm thấy sản phẩm nào!!");
            txtThongBao.setVisibility(View.VISIBLE);
            imgThongBao.setVisibility(View.VISIBLE);
            listProduct.setVisibility(View.INVISIBLE);
        }
    }
    //Giỏ hàng
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navbottom_items,menu);
        return true;
    }

    public void mapping() {
        mData = FirebaseDatabase.getInstance().getReference();
        dl = (DrawerLayout) findViewById(R.id.activity_info_products);
        nv = (NavigationView) findViewById(R.id.nv);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mvideoView = (VideoView) findViewById(R.id.videoView);
        searchView = (SearchView)findViewById(R.id.cardSearch);
        txtThongBao = findViewById(R.id.txtThongBao);
        imgThongBao = findViewById(R.id.imgThongBao);
        //Discount
        arrayImageDiscount = new ArrayList<>();
        adapter = new ImageAdapter(this, arrayImageDiscount);
        mViewPager.setAdapter(adapter);

        //brand
        imageURLs = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_Logo);
        recyclerViewAdapter = new RecyclerViewAdapter(this, imageURLs, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(recyclerViewAdapter);
        //product
        arrayProduct = new ArrayList<>();
        listProduct = (RecyclerView) findViewById(R.id.listProduct);
        productAdapter = new ProductAdapter(getApplicationContext(), arrayProduct);
        listProduct.setHasFixedSize(true);
        listProduct.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        listProduct.setAdapter(productAdapter);
        dg = new ProgressDialog(this);
        navView = (NavigationView) findViewById(R.id.nv);
        headerview = navView.getHeaderView(0);
        tenKH = (TextView)headerview.findViewById(R.id.tenKH);
        emailKH = (TextView) headerview.findViewById(R.id.emailKH);
        imageNav = (ImageView)headerview.findViewById(R.id.imageView);
        if(arrayCart != null){

        }else {
            arrayCart = new ArrayList<>();
        }


    }

    public void loadInforUser(){
        if(mUser != null) {
            userId = mUser.getUid();
            Query query = mReference.orderByChild("emailUser").equalTo(mUser.getEmail());
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        //get data
                        String ten = ""+ds.child("fullName").getValue();
                        String e = ""+ds.child("emailUser").getValue();
                        String i = ""+ds.child("imgUser").getValue();
                        if(i.isEmpty()){
                            emailKH.setText(e);
                            tenKH.setText(ten);
                        } else {
                            Picasso.with(MainActivity.this).load(i).into(imageNav);
                            emailKH.setText(e);
                            tenKH.setText(ten);
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //load video
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.giay);
        try {
            mvideoView.setVideoURI(uri);
        } catch (NullPointerException techmaster1) {
            System.out.println("Loi khong load duoc video" + techmaster1);
        }
        mvideoView.start();
    }

    @Override
    public void onItemClickListener(int position) {

        Intent intent = new Intent(this, BrandsActivity.class);
        intent.putExtra("brandId", imageURLs.get(position).getBrandId());
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //Hành động khi click vào giỏ hàngo
        if(t.onOptionsItemSelected(item))
            return true;
        switch (id){
            case R.id.nav_cart:
                //đóng dl
                dl.closeDrawer(Gravity.LEFT, false);
                //hiện giỏ hàng
                Intent intents = new Intent(this, CartActivity.class);
                startActivity(intents);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();
        switch (id) {
            case R.id.map:
                Intent intent4 = new Intent(getApplicationContext(), StoreListActivity.class);
                intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent4);
                break;
            case R.id.category:
                Intent intent = new Intent(getApplicationContext(), BrandsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.thongKe:
                if(mUser != null){
                    anXacNhan = true;
                    Intent intent3 = new Intent(getApplicationContext(), SoldActivity.class);
                    intent3.putExtra("order1", "y");
                    startActivity(intent3);
                }else {
                    Toast.makeText(MainActivity.this, "Vui lòng đăng nhập để xem đơn hàng!!",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.khuyenMai:
                Intent intent2 = new Intent(getApplicationContext(), DiscountsActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent2);
                break;
            case R.id.dangxuat:
                dg.setMessage("Đang đăng xuất....");
                dg.show();
                if (mUser != null) {
                    mAuth.signOut();
                    mUser = null;
                    MainActivity.arrayCart.clear();
                    Thread bamgio=new Thread(){
                        public void run()
                        {
                            try {
                                sleep(2000);

                            } catch (Exception e) {

                            }
                            finally
                            {
                                dg.dismiss();
                                finish();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                            }
                        }
                    };

                    bamgio.start();

                } else {
                    // No user is signed in
                    dg.dismiss();
                    Toast.makeText(MainActivity.this, "Bạn chưa đăng nhập!", Toast.LENGTH_SHORT).show();
                }

                break;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(LoginActivity.mUser != null){
            mUser = LoginActivity.mUser;
            mAuth =FirebaseAuth.getInstance();
            mReference = FirebaseDatabase.getInstance().getReference("UserInfor");
            loadInforUser();
        }
        else{
            LoginActivity.mUser = null;

        }



    }


}

