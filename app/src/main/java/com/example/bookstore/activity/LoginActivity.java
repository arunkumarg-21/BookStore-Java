package com.example.bookstore.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookstore.R;
import com.example.bookstore.util.DatabaseHelper;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
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

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "LoginActivity";
    TextView newUserRegister;
    EditText userId, userPassword;
    Button loginButton;
    GoogleSignInClient mGoogleSignInClient;
    private GoogleApiClient googleApiClient;
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
        loginButtonClickListener();
        newUserRegisterClickListener();
    }


    private void initializeView() {
        newUserRegister = findViewById(R.id.signUp);
        loginButton = findViewById(R.id.login);
        userId = findViewById(R.id.userId);
        userPassword = findViewById(R.id.pass);
        mLoginBt = findViewById(R.id.facebookLogin);
        signInButton = findViewById(R.id.signIn);
        myDb = new DatabaseHelper(this);
    }

    private void facebookLogin() {
        callbackManager = CallbackManager.Factory.create();
        mLoginBt.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
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
       /* googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();*/
        // GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        // updateUI(account);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(signInIntent, REQ_CODE);*/
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, REQ_CODE);
            }
        });
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


    private void loginButtonClickListener() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean task = myDb.checkUser(userId.getText().toString(), userPassword.getText().toString());
                if (task) {
                    SharedPreferences sh = getSharedPreferences("LoginActivity", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sh.edit();

                    editor.putString("id", userId.getText().toString());
                    editor.putString("password", userPassword.getText().toString());
                    editor.apply();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void newUserRegisterClickListener() {
        newUserRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
            }
        });
    }


    public void onResume() {
        super.onResume();

        SharedPreferences sh = getSharedPreferences("LoginActivity", Context.MODE_PRIVATE);
        if (sh.getString("id", null) != null) {

            String pass = sh.getString("password", null);
            String id = sh.getString("id", null);
            if (!(id.isEmpty() && pass.isEmpty())) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE) {

            /*GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);*/
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);


        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> result) {
       try{
           GoogleSignInAccount account = result.getResult(ApiException.class);
           //updateUI(account);
           startActivity(new Intent(LoginActivity.this,MainActivity.class));
       }catch (Exception e){
           Toast.makeText(getApplicationContext(), "Signing in Failed", Toast.LENGTH_SHORT).show();
       }

    }

   /* @Override
    protected void onStart() {

        super.onStart();
    }*/

    /*private void handleSignInResult(GoogleSignInResult result) {
        System.out.println("result===="+result.getStatus());
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            String name = account.getDisplayName();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            Toast.makeText(getApplicationContext(), "Welcome " + name, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Signin Failed", Toast.LENGTH_SHORT).show();
        }

    }*/



    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


}
