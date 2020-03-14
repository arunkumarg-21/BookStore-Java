package com.example.bookstore.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookstore.R;
import com.example.bookstore.model.UserList;
import com.example.bookstore.util.DatabaseHelper;
import com.example.bookstore.util.SharedPreferenceHelper;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,View.OnClickListener {

    private static final String TAG = "LoginActivity";
    TextView newUserRegister;
    EditText userId, userPassword;
    Button loginButton;
    GoogleSignInClient mGoogleSignInClient;
    private LoginButton mLoginBt;
    CallbackManager callbackManager;
    private SignInButton signInButton;
    private static final int REQ_CODE = 1;
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeView();
        facebookLogin();
        googleLogin();
        inputListener();

    }


    private void initializeView() {
        newUserRegister = findViewById(R.id.signUp);
        newUserRegister.setOnClickListener(this);
        loginButton = findViewById(R.id.login);
        loginButton.setOnClickListener(this);
        userId = findViewById(R.id.userId);
        userPassword = findViewById(R.id.pass);
        mLoginBt = findViewById(R.id.facebookLogin);
        signInButton = findViewById(R.id.signIn);
        signInButton.setOnClickListener(this);
        myDb = new DatabaseHelper(this);

    }

    private void facebookLogin() {
        //FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        mLoginBt.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            System.out.println("name=======+"+response.getJSONObject().getString("email"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(LoginActivity.this, "Welcome " + object.optString("name"), Toast.LENGTH_SHORT).show();
                    }
                });
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }

            @Override
            public void onCancel() {
                LoginManager.getInstance().logOut();
            }

            @Override
            public void onError(FacebookException error) {
            }
        });
    }

    private void googleLogin() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);
    }

    private void inputListener() {
        userId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (userId.getText().toString().isEmpty()) {
                    userId.setError("Cannot Be Null");
                } else {
                    userId.setError(null);
                }
            }
        });
        userPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (userPassword.getText().toString().isEmpty()) {
                    userPassword.setError("Cannot Be Null");
                } else {
                    userPassword.setError(null);
                }
            }
        });
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.signIn:
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, REQ_CODE);
                break;

            case R.id.login:
                boolean task = myDb.checkUser(userId.getText().toString(), userPassword.getText().toString());
                if (task) {
                    SharedPreferenceHelper sph=new SharedPreferenceHelper();
                    sph.initialize(getApplicationContext(),userId.getText().toString(),userPassword.getText().toString());
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.signUp:
                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
                break;

        }
    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);


        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> result) {
        try {
            GoogleSignInAccount account = result.getResult(ApiException.class);
            if (account != null) {
                updateUI(account);
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Signing in Failed", Toast.LENGTH_SHORT).show();
        }

    }

    private void updateUI(GoogleSignInAccount account) {
        String name = account.getDisplayName();
        String email = account.getEmail();
        String pass = account.getId();
        assert pass != null;
        assert email != null;
        UserList userList = new UserList(Objects.requireNonNull(name), email, pass, "aminjikarai", drawableToByte(getResources().getDrawable(R.drawable.profile_pic)));
        myDb.insertUser(userList);
        SharedPreferenceHelper sph=new SharedPreferenceHelper();
        sph.initialize(getApplicationContext(),name,pass);
        startActivity(new Intent(LoginActivity.this, MainActivity.class));

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


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


}
