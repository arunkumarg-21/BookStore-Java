package com.example.bookstore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow);

        head=findViewById(R.id.name);
        img=findViewById(R.id.img);

        Intent intent=getIntent();
        String  name = intent.getStringExtra("name");
        head.setText(name);
        int imge=intent.getIntExtra("img",0);
        img.setImageResource(imge);

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
        //Toast.makeText(getApplicationContext(),null , Toast.LENGTH_SHORT).show();

    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}
