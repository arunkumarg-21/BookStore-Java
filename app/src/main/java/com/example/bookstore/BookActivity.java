package com.example.bookstore;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

public class BookActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int REQ_CODE=1;
    ImageView img;
    TextView txt, txt1;
    Button btCart, btBorrow;
    byte[] imge;
    EditText address;
    Toolbar toolbar;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        //ActionBar actionBar = getSupportActionBar();


        img = findViewById(R.id.img);
        txt = findViewById(R.id.textViewHead);
        txt1 = findViewById(R.id.desc);
        btCart = findViewById(R.id.btCart);
        btBorrow = findViewById(R.id.btBorrow);
        address = findViewById(R.id.address);
        toolbar = findViewById(R.id.toolBar);



        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });


        Intent intent = getIntent();
        final String Head = intent.getStringExtra("head");
        txt.setText(Head);
        String Desc = intent.getStringExtra("desc");
        txt1.setText(Desc);
        imge = intent.getByteArrayExtra("image");
        Bitmap bitmap = BitmapFactory.decodeByteArray(imge,0,imge.length);
        img.setImageBitmap(bitmap);


       /* if(ContextCompat.checkSelfPermission(BookActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
            finish();
            return;
        }*/

        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (ContextCompat.checkSelfPermission(BookActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(BookActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(BookActivity.this);
                               builder.setTitle("Permission Required")
                                .setMessage("Location Permission is needed")
                                .setNegativeButton("Cancel",null)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ActivityCompat.requestPermissions(BookActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQ_CODE);
                                    }
                                }).show();
                    } else {

                        ActivityCompat.requestPermissions(BookActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQ_CODE);

                    }
                } else {
                    startActivity(new Intent(getApplicationContext(), MapActivity.class));
                }


            }
        });


        List<Integer> ai = new ArrayList<>();

       for(int i=1;i<=10;i++){
           ai.add(i);
       }

        Spinner spinner = findViewById(R.id.quantity);
        spinner.setOnItemSelectedListener(this);

        ArrayAdapter<Integer> aa = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, ai);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);


        /*byte[] mBytes = getIntent().getByteArrayExtra("image");

         Bitmap bitmap = BitmapFactory.decodeByteArray(mBytes,0,mBytes.length);

         img.setImageBitmap(bitmap);*/

        btCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btCart.getText().equals("Go to cart")) {
                    Intent intent1 = new Intent(getApplicationContext(), BuyActivity.class);
                    intent1.putExtra("name", Head);
                    intent1.putExtra("img", imge);
                    startActivity(intent1);
                } else {
                    Toast.makeText(getApplicationContext(), "Successfully Added to Cart", Toast.LENGTH_SHORT).show();
                    btCart.setText("Go to cart");
                }
            }
        });

        btBorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), BorrowActivity.class);
                i.putExtra("name", Head);
                i.putExtra("img", imge);
                startActivity(i);

            }
        });
    }

    public void onItemSelected(AdapterView<?> parent, View args1, int position, long id) {
        String pos = parent.getItemAtPosition(position).toString();
        //Toast.makeText(getApplicationContext(),null , Toast.LENGTH_SHORT).show();

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQ_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(new Intent(getApplicationContext(), MapActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }


}
