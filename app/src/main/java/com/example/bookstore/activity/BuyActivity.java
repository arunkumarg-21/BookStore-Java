package com.example.bookstore;


import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.bookstore.activity.MyReceiver;
/*
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
*/




public class BuyActivity extends AppCompatActivity {

    private static final String CHANNEL_ID ="channel" ;
    int notificationId=1;
    //private NotificationManagerCompat notificationManager;
   // NotificationManager manager=null;
    ImageView iV;
    TextView tV,price;
    EditText address;
    Button mBuy;
    byte[] img;
    String Name;
    Toolbar toolbar;
    MyReceiver receiver;

   /* PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();

    PayUmoneySdkInitializer.PaymentParam paymentParam = null;

    String TAG = "BuyActivity", txtid="text1234",amount="20",phone="914404088"
    ,prodname ="BookStore", firstname ="kamal", email ="ajithk0102@gmail.com",
    merchantId ="6941146", merchantkey="9q0iEeag";
    String salt="3QBoUaGXn4";
    String key = "9q0iEeag";
*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        iV = findViewById(R.id.image);
        tV  =findViewById(R.id.name);
        price=findViewById(R.id.price);
        mBuy=findViewById(R.id.buy);
        toolbar = findViewById(R.id.toolBar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        price.setText("Price: 120");


       /* mBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = getIntent();
                email = intent.getExtras().getString("email");
                amount=intent.getExtras().getString("amount");

                startPay();

            }
        });*/




        Intent intent = getIntent();
        Name = intent.getStringExtra("name");
        tV.setText(Name);
        img = intent.getByteArrayExtra("img");
        Bitmap bitmap = BitmapFactory.decodeByteArray(img,0,img.length);
        iV.setImageBitmap(bitmap);

        configureReceiver();


        //notificationManager = NotificationManagerCompat.from(BuyActivity.this);

    }


    private void configureReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.BookStore");
        receiver =  new MyReceiver();
        registerReceiver(receiver,intentFilter);

    }

    public void notify(View v){
         Intent i = new Intent();
         i.setAction("com.example.BookStore");
         i.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
         sendBroadcast(i);


    }

    public void onDestroy() {

        super.onDestroy();

        unregisterReceiver(receiver);

    }


}
