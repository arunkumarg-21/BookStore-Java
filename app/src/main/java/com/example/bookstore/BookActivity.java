package com.example.bookstore;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

public class BookActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        //ActionBar actionBar = getSupportActionBar();
        ImageView img;
        TextView txt, txt1;
        final Button btCart, btBorrow;
       final int imge;
       EditText address;


        img = findViewById(R.id.img);
        txt = findViewById(R.id.textViewHead);
        txt1 = findViewById(R.id.desc);
        btCart = findViewById(R.id.btCart);
        btBorrow = findViewById(R.id.btBorrow);
        address=findViewById(R.id.address);
        Intent intent = getIntent();
        final String Head = intent.getStringExtra("head");
        txt.setText(Head);
        String Desc = intent.getStringExtra("desc");
        txt1.setText(Desc);
        imge = intent.getIntExtra("image", 0);
        img.setImageResource(imge);

       /* if(ContextCompat.checkSelfPermission(BookActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
            finish();
            return;
        }*/

        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity(BookActivity.this)
                        .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                startActivity(new Intent(getApplicationContext(),MapActivity.class));
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {
                                if(response.isPermanentlyDenied()){
                                    AlertDialog.Builder builder= new AlertDialog.Builder(BookActivity.this);
                                    builder.setTitle("Permission Denied")
                                            .setMessage("permission to access device location is denied.You need to go to the settings to give permmision")
                                            .setNegativeButton("cancel",null)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent();
                                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                    intent.setData(Uri.fromParts("package",getPackageName(),null));
                                                }
                                            })
                                            .show();
                                }else{
                                    Toast.makeText(getApplicationContext(),"Permission Denied",Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        })
                        .check();
            }
        });
        /* String imge = intent.getStringExtra("img");
         Uri fimg = Uri.parse(imge);
         img.setImageURI(fimg);*/

        List<Integer> ai = new ArrayList<Integer>();
        ai.add(1);
        ai.add(2);
        ai.add(3);
        ai.add(4);
        ai.add(5);
        ai.add(6);
        ai.add(7);
        ai.add(8);
        ai.add(9);
        ai.add(10);

        Spinner spinner = findViewById(R.id.quantity);
        spinner.setOnItemSelectedListener(this);

        ArrayAdapter<Integer> aa = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,ai);
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
                    intent1.putExtra("name",Head);
                    intent1.putExtra("img",imge);
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
                Intent i = new Intent(getApplicationContext(),BorrowActivity.class);
                i.putExtra("name",Head);
                i.putExtra("img",imge);
                startActivity(i);

            }
        });
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
