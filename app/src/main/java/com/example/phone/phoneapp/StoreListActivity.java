package com.example.phone.phoneapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.example.phone.R;
import com.example.phone.model.CustomListAdapter;
import com.example.phone.model.Store;

import java.util.ArrayList;
import java.util.List;

public class StoreListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_list);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Store List");

        List<Store> image_details = getListData();
        final ListView listview = (ListView) findViewById(R.id.listViewStore);
        listview.setAdapter(new CustomListAdapter(this, image_details));

        // When the user clicks on the GridItem
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Store selectedItem = (Store) a.getItemAtPosition(position);
                //Toast.makeText(MainActivity.this, selectedItem.getId() + ": " + selectedItem.getName(), Toast.LENGTH_LONG).show();
                Intent _intent = new Intent(StoreListActivity.this, MapActivity.class);

                String name = selectedItem.getName();
                Double latitude = selectedItem.getLatitude();
                Double longitude = selectedItem.getLongitude();

                _intent.putExtra("name",name);
                _intent.putExtra("latitude",latitude);
                _intent.putExtra("longitude",longitude);
                startActivity(_intent);
            }
        });
    }
    private  List<Store> getListData() {
        List<Store> list = new ArrayList<Store>();
        Store s1 = new Store("TTShop Lê Trọng Tấn", "140 Lê Trọng Tấn, Tây Thạnh, Tân Phú, HCM", 10.8065, 106.6289);
        Store s2 = new Store("TTShop Lương Thế Vinh", "48/9C Lương Thế Vinh, Tân Thới Hòa, Tân Phú, HCM", 10.7641, 106.6291);

        list.add(s1);
        list.add(s2);

        return list;

    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home)
        {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}