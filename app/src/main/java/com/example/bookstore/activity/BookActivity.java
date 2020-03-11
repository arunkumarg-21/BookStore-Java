package com.example.bookstore.activity;

import androidx.annotation.NonNull;
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
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.bookstore.R;
import com.example.bookstore.model.ListItem;
import com.example.bookstore.util.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class BookActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, ActivityCompat.OnRequestPermissionsResultCallback {


    private static final int REQ_CODE = 1;
    ImageView img;
    DatabaseHelper myDb;
    Spinner spinner;
    TextView name, desc, price;
    Button buttonCart;
    byte[] byteImage;
    EditText address;
    Toolbar toolbar;
    Double Price;
    String Head, Desc;
    String addr = "select your address";
    RatingBar ratingBar;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        initializeView();
        toolbarInitialize();
        gettingPassedData();
        setAddress();
        addressClickListener();
        spinnerInitialize();
        cartButtonClickListener();

        ratingBar.setRating(3.5f);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.mCart:
                startActivity(new Intent(BookActivity.this,CartActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initializeView() {

        img = findViewById(R.id.img);
        name = findViewById(R.id.name);
        desc = findViewById(R.id.desc);
        buttonCart = findViewById(R.id.btCart);
        address = findViewById(R.id.address);
        toolbar = findViewById(R.id.toolBar);
        ratingBar = findViewById(R.id.rating);
        price = findViewById(R.id.price);
        spinner = findViewById(R.id.quantity);
        myDb = new DatabaseHelper(this);
    }

    private void toolbarInitialize() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void gettingPassedData() {

        Intent intent = getIntent();
        Head = intent.getStringExtra("head");
        name.setText(Head);
        Desc = intent.getStringExtra("desc");
        desc.setText(Desc);
        Price = intent.getDoubleExtra("price",0);
        price.setText("Price:" + Price);
        byteImage = intent.getByteArrayExtra("image");
        if (byteImage != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length);
            img.setImageBitmap(bitmap);
        }
    }

    private void setAddress() {

        String location = myDb.getAddress();
        if (location == null) {
            address.setHint(addr);
        } else {
            address.setText(location);
        }
    }

    private void addressClickListener() {

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
                                .setNegativeButton("Cancel", null)
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
                    Intent in = new Intent(getApplicationContext(), MapActivity.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(in);
                }

            }
        });
    }

    private void spinnerInitialize() {

        List<Integer> ai = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            ai.add(i);
        }
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<Integer> aa = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ai);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);
    }

    private void cartButtonClickListener() {

        buttonCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonCart.getText().equals("Go to cart")) {
                    startActivity(new Intent(BookActivity.this, CartActivity.class));
                } else {
                    ListItem listItem = new ListItem(Head, Desc, byteImage, Price);
                    boolean result = myDb.insertCart(listItem);
                    if (result) {
                        Toast.makeText(BookActivity.this, "Added successful", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(BookActivity.this, "Added failed", Toast.LENGTH_SHORT).show();
                    }
                    buttonCart.setText("Go to cart");
                }
            }
        });
    }


    public void onResume() {
        super.onResume();
        String location = myDb.getAddress();
        if (location == null) {
            address.setHint(addr);
        } else {
            address.setText(location);
        }

    }

    public void onItemSelected(AdapterView<?> parent, View args1, int position, long id) {
        int pos = Integer.parseInt(parent.getItemAtPosition(position).toString());
        Price = Price * pos;
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
