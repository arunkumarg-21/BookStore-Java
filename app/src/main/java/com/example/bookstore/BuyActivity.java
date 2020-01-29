package com.example.bookstore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BuyActivity extends AppCompatActivity {

    ImageView iV;
    TextView tV,price;
    EditText address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        iV = findViewById(R.id.image);
        tV  =findViewById(R.id.name);
        price=findViewById(R.id.price);

        price.setText("Price: 120");




        Intent intent = getIntent();
        String Name = intent.getStringExtra("name");
        tV.setText(Name);
        int img = intent.getIntExtra("img",0);
        iV.setImageResource(img);


    }

}
