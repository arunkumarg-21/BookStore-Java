package com.example.bookstore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookstore.R;
import com.example.bookstore.util.SharedPreferenceHelper;

public class SplashActivity extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferenceHelper sph = new SharedPreferenceHelper();
                if ((sph.getSharedName(getApplicationContext())).contentEquals("")){
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                } else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
                finish();
            }
        }, 3000);

    }

}
