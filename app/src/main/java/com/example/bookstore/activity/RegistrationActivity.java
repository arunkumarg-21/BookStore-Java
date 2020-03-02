package com.example.bookstore.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookstore.R;
import com.example.bookstore.model.UserList;
import com.example.bookstore.util.DatabaseHelper;


public class RegistrationActivity extends AppCompatActivity {

    private EditText userName, userPass, userEmail;
    private Button regButton;
    private TextView userLogin;
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        initializeView();
        regButtonClickListener();
        userLoginClickListener();
    }

    private void initializeView() {
        regButton = findViewById(R.id.reg);
        userLogin = findViewById(R.id.login);
        userName = findViewById(R.id.userId);
        userPass = findViewById(R.id.pass);
        userEmail = findViewById(R.id.userEmail);
        myDb = new DatabaseHelper(this);
    }

    private void regButtonClickListener() {
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userName.getText().toString().isEmpty() || userPass.getText().toString().isEmpty()) {
                    Toast.makeText(RegistrationActivity.this, "please enter all the details", Toast.LENGTH_SHORT).show();
                } else {
                    System.out.println("else====");
                    UserList userList = new UserList(userName.getText().toString(), userEmail.getText().toString(), userPass.getText().toString(),"aminjikarai");
                    System.out.println("user=======");
                    boolean task = myDb.insertUser(userList);
                    if (task) {
                        System.out.println("register========");
                        SharedPreferences sh = getSharedPreferences("LoginActivity", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sh.edit();

                        editor.putString("id", userName.getText().toString());
                        editor.putString("password",userPass.getText().toString());
                        editor.apply();
                        Toast.makeText(RegistrationActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                    } else {
                        Toast.makeText(RegistrationActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }


        });
    }

    private void userLoginClickListener() {
        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            }
        });
    }
}
