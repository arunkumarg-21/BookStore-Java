package com.example.bookstore.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookstore.R;
import com.example.bookstore.model.UserList;
import com.example.bookstore.util.DatabaseHelper;
import com.example.bookstore.util.SharedPreferenceHelper;

import java.io.ByteArrayOutputStream;


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

        userEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(userEmail.getText().toString()) && Patterns.EMAIL_ADDRESS.matcher(userEmail.getText().toString()).matches()) {
                    userEmail.setError(null);
                } else {
                    userEmail.setError("Invalid Email");
                }
            }
        });
    }

    private void regButtonClickListener() {
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(userName.getText().toString().isEmpty() || userPass.getText().toString().isEmpty())) {
                    if (isValidEmail()) {
                        UserList userList = new UserList(userName.getText().toString(), userEmail.getText().toString(), userPass.getText().toString(), "aminjikarai", drawableToByte(getResources().getDrawable(R.drawable.profile_pic)));
                        boolean task = myDb.insertUser(userList);
                        if (task) {
                            SharedPreferenceHelper sph=new SharedPreferenceHelper();
                            sph.initialize(getApplicationContext(),userName.getText().toString(),userPass.getText().toString());
                            Toast.makeText(RegistrationActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                        } else {
                            Toast.makeText(RegistrationActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegistrationActivity.this, "Not a Valid Email", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegistrationActivity.this, "please enter all the details", Toast.LENGTH_SHORT).show();
                }
            }


        });
    }

    private byte[] drawableToByte(Drawable book) {
        Bitmap bitmap = ((BitmapDrawable) book).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        long size = bitmap.getByteCount();
        if (size > 20000) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, stream);
        } else {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        }
        return stream.toByteArray();
    }

    public boolean isValidEmail() {
        CharSequence target = userEmail.getText().toString();

        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
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
