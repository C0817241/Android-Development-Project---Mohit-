package com.android.androidassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class ViewingActivity extends AppCompatActivity {

    ListView listView;
    Button buttonedit,buttonlocation;

    ProductDBHelper dbHelper;
    Product product;
    String[] data;
    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewing);

        listView = findViewById(R.id.listview);
        buttonedit = findViewById(R.id.button_edit);
        buttonlocation = findViewById(R.id.button_location);

        int id = getIntent().getIntExtra("id",1);

        dbHelper = new ProductDBHelper(this);
        dbHelper.openDataBase();

        product = dbHelper.getProductbyID(id);

        dbHelper.close();

        data = new String[]{"Product Id :"+product.getId(),"Product Name :"+product.getProductname(),
                "Product Desc :"+product.getProductdesc(),"Product Price :"+product.getProductprice(),
                "Latitude :"+product.getLatitude(),"Longitude :"+product.getLongitude()};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,data);

        listView.setAdapter(adapter);

        getSupportActionBar().setTitle(product.getProductname());

        buttonedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewingActivity.this,EntryActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("activity","viewing");
                startActivity(intent);
            }
        });

        buttonlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewingActivity.this,MapsActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("activity","viewing");
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}