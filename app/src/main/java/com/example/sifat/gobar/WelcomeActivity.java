package com.example.sifat.gobar;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Sifat on 10/28/2015.
 */
public class WelcomeActivity extends ActionBarActivity implements View.OnClickListener {

    Intent intent;
    Button singup, login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        } else if (view.getId() == R.id.btSignUp) {
            intent = new Intent(WelcomeActivity.this, MapsActivity.class);
            startActivity(intent);
        }
        finish();
    }
}
