package com.example.bookstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.se.omapi.Session;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "Login" ;
    TextView tv;
    EditText eTextID,eTextPass;
    Button bt;
   // GoogleSignInClient mGoogleSignInClient;
    private GoogleApiClient googleApiClient;
    private LoginButton mLoginBt;
    CallbackManager callbackManager;
    private SignInButton signInButton;
    private  static final int REQ_CODE=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        tv = findViewById(R.id.signUp);
        bt = findViewById(R.id.login);
        eTextID = findViewById(R.id.userId);
        eTextPass = findViewById(R.id.pass);
        mLoginBt = findViewById(R.id.facebookLogin);
        signInButton = findViewById(R.id.signIn);

        callbackManager = CallbackManager.Factory.create();

        mLoginBt.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        Toast.makeText(Login.this,"Welcome "+object.optString("name"),Toast.LENGTH_SHORT).show();

                    }
                });


                startActivity(new Intent(Login.this,MainActivity.class));

            }

            @Override
            public void onCancel() {

                LoginManager.getInstance().logOut();


            }

            @Override
            public void onError(FacebookException error) {

            }
        });


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        // updateUI(account);



        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(signInIntent, REQ_CODE);
            }
        });


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

    public void onResume() {
        super.onResume();

       @SuppressLint("WrongConstant") SharedPreferences sh = getSharedPreferences("Login",MODE_APPEND);


        String pass = sh.getString("password",null);

        String value = sh.getString("id",null);

        if (value != null && pass != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }

    }

    public  void onPause() {

        super.onPause();

        SharedPreferences sh = getSharedPreferences("Login",MODE_PRIVATE);

        SharedPreferences.Editor editor = sh.edit();

        editor.putString("id", eTextID.getText().toString());
        editor.putString("password",eTextPass.getText().toString());
        editor.commit();

    }


    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        callbackManager.onActivityResult(requestCode,resultCode,data);

        if(requestCode == REQ_CODE){

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);

        }


    }

    private void handleSignInResult(GoogleSignInResult result) {

            if(result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();
                String name = account.getDisplayName();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                Toast.makeText(getApplicationContext(),"Welcome "+name,Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(),"Signin Failed",Toast.LENGTH_SHORT).show();
            }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


}
