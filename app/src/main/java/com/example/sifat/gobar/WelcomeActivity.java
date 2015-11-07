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
        printKeyHash();
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
        } else if (view.getId() == R.id.btSignUp) {
            intent = new Intent(WelcomeActivity.this, SignupActivity.class);
        }
        String gcmRegNum = sharedPreferences.getString(GCM_REGISTER_ID, "");
        if (!gcmRegNum.isEmpty() && !gcmRegNum.equalsIgnoreCase("")) {
            singup.setEnabled(false);
            login.setEnabled(false);
            GcmRegFetcher gcmRegFetcher = new GcmRegFetcher();
            gcmRegFetcher.fetchGcmRegNumber(this, intent);
        } else
            startActivity(intent);
        finish();
    }


    /**
     * Call this method inside onCreate once to get your hash key
     */
    public void printKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.example.sifat.gobar", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("SHA", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                //9hN8uQRyHS+GXtoYDapbZ0BB1ZM=
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
