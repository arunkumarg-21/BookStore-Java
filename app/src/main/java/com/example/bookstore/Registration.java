package com.example.bookstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class Registration extends AppCompatActivity {

    private EditText userId,userPass,userEmail;
    private Button regButton;
    private TextView userLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        regButton=findViewById(R.id.reg);
        userLogin=findViewById(R.id.login);

        userId=findViewById(R.id.userId);
        userPass=findViewById(R.id.pass);
        userEmail=findViewById(R.id.userEmail);



        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userEmail.getText().toString().isEmpty()||userPass.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"please enter all the details",Toast.LENGTH_SHORT).show();
                }else {

                    Toast.makeText(getApplicationContext(),"Registration Successful",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Registration.this,MainActivity.class));

                            }
                        }


    });

        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registration.this,Login.class));
            }
        });


}
}
