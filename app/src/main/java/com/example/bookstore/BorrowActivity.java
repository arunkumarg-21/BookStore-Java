package com.example.bookstore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BorrowActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    TextView head;
    ImageView img;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow);

        head=findViewById(R.id.name);
        img=findViewById(R.id.img);
        toolbar=findViewById(R.id.toolBar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Intent intent=getIntent();
        String  name = intent.getStringExtra("name");
        head.setText(name);
        byte[] imge=intent.getByteArrayExtra("img");
        Bitmap bitmap = BitmapFactory.decodeByteArray(imge,0,imge.length);
        img.setImageBitmap(bitmap);

        List<String> ai = new ArrayList<>();
        ai.add("3 days");
        ai.add("5 days");
        ai.add("1 Week");
        ai.add("2 Week");
        ai.add("1 Month");
        ai.add("2 Months");
        ai.add("3 Months");

        Spinner spinner = findViewById(R.id.days);
        spinner.setOnItemSelectedListener(this);

        ArrayAdapter<String> aa = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,ai);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);
    }

    public void onItemSelected(AdapterView<?> parent, View args1, int position, long id)
    {
        String pos = parent.getItemAtPosition(position).toString();

    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}
