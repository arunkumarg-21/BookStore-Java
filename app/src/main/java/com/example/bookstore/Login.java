package com.example.bookstore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    TextView tv;
    EditText eTextID,eTextPass;
    Button bt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        tv=findViewById(R.id.signUp);
        bt=findViewById(R.id.login);
        eTextID=findViewById(R.id.userId);
        eTextPass=findViewById(R.id.pass);

        eTextID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(eTextID.getText().toString().isEmpty()){
                    eTextID.setError("Cannot Be Null");
                }else{
                    eTextID.setError(null);
                }
            }
        });
        eTextPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(eTextPass.getText().toString().isEmpty()){
                    eTextPass.setError("Cannot Be Null");
                }else{
                    eTextPass.setError(null);
                }
            }
        });

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(eTextID.getText().toString().isEmpty()&&eTextPass.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please Enter Details",Toast.LENGTH_LONG).show();
                }else{
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }
            }
        });

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),Registration.class));
            }
        });


    }


}
