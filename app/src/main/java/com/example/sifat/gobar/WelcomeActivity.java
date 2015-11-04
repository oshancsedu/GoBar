package com.example.sifat.gobar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.sifat.Controller.GcmRegFetcher;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.example.sifat.Utilities.CommonUtilities.*;
/**
 * Created by Sifat on 10/28/2015.
 */
public class WelcomeActivity extends ActionBarActivity implements View.OnClickListener {

    private Intent intent;
    private Button singup, login;
    private SharedPreferences sharedPreferences;
    private String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = new Intent(WelcomeActivity.this, MapsActivity.class);
        sharedPreferences = getSharedPreferences(String.valueOf(R.string.sharedPref),this.MODE_PRIVATE);
        email=sharedPreferences.getString(USER_EMAIL,"");
        if(!email.equalsIgnoreCase(""))
        {
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.welcome);
        singup = (Button) findViewById(R.id.btSignUp);
        singup.setOnClickListener(this);
        login = (Button) findViewById(R.id.btLogIn);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btLogIn) {
            intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intent);
            //GcmRegFetcher gcmRegFetcher = new GcmRegFetcher();
            //gcmRegFetcher.fetchGcmRegNumber(this);
        } else if (view.getId() == R.id.btSignUp) {
            intent = new Intent(WelcomeActivity.this, SignupActivity.class);
            startActivity(intent);
        }
        finish();
    }
}
