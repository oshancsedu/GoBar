package com.example.sifat.gobar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sifat.Controller.FacebookInfoFetcher;
import com.example.sifat.Controller.ServerCommunicator;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.sifat.Utilities.CommonUtilities.*;
/**
 * Created by Sifat on 10/28/2015.
 */
public class SignupActivity extends ActionBarActivity implements View.OnClickListener {

    private ImageButton ibSignup;
    private FacebookCallback<LoginResult> facebookCallback;
    private CallbackManager callbackManager;
    private List<String> permission;
    private Set<String> grantedPermissions,declinedPermissions;
    private Profile profile;
    private AccessToken accessToken;
    private LoginManager loginManager;
    private String profileName, profileID, userEmail;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private FacebookInfoFetcher facebookInfoFetcher;
    private EditText etFirstName, etLastName, etAddress, etPhoneNumber, etEmail, etBday, etPassword, etConfirmPass,etProfession;
    private Button btSignup;
    private Spinner spGender;
    private Toolbar toolbar;
    private Intent intent;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_signup);
        init();
    }

    private void init() {

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);


        loginManager=LoginManager.getInstance();
        callbackManager = CallbackManager.Factory.create();
        ibSignup = (ImageButton) findViewById(R.id.btFBSignup);
        ibSignup.setOnClickListener(this);

        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etConfirmPass = (EditText) findViewById(R.id.etConfirmPass);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etBday = (EditText) findViewById(R.id.etBday);
        etAddress = (EditText) findViewById(R.id.etAddress);
        spGender = (Spinner) findViewById(R.id.gender);
        etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);
        etProfession= (EditText) findViewById(R.id.etProfession);
        btSignup = (Button) findViewById(R.id.btSignUp);
        btSignup.setOnClickListener(this);

        ArrayList<String> genderArray = new ArrayList<String>();
        genderArray.add("-Gender-");
        genderArray.add("Male");
        genderArray.add("Female");
        genderArray.add("Other");

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, genderArray);
        spGender.setAdapter(spinnerArrayAdapter);

        permission=new ArrayList<>();
        grantedPermissions=new HashSet<>();
        declinedPermissions=new HashSet<>();
        //permission.add("user_friends");
        permission.add("user_status");
        permission.add("email");
        permission.add("user_birthday");
        permission.add("user_location");
        //loginButton.setReadPermissions(permission);

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                accessToken=currentAccessToken;
                profileID=currentAccessToken.getUserId();
                Log.i(LOG_TAG_FACEBOOK,""+currentAccessToken+"-"+profileID);
            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                profile=currentProfile;
                profileName=currentProfile.getName();
                Log.i(LOG_TAG_FACEBOOK,"Tracker Name: "+profileName);
            }
        };


        facebookCallback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                login(loginResult);
            }

            @Override
            public void onCancel() {
                Toast.makeText(SignupActivity.this, "Login has been canceled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(SignupActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
            }
        };
        loginManager.registerCallback(callbackManager, facebookCallback);
    }

    private void login(LoginResult loginResult) {
        accessToken = loginResult.getAccessToken();
        Log.i(LOG_TAG_FACEBOOK, "access token: " + accessToken);
        profile = Profile.getCurrentProfile();
        profileName=profile.getName();
        profileID=profile.getId();

        if (profile != null) {
            facebookInfoFetcher = new FacebookInfoFetcher();
            facebookInfoFetcher.getFBInfo("id,email,first_name,last_name,birthday,gender,location",this,accessToken,true);
        }
        else
        {
            Toast.makeText(SignupActivity.this, "This profile is null", Toast.LENGTH_SHORT).show();
            grantedPermissions=accessToken.getPermissions();
            declinedPermissions=accessToken.getDeclinedPermissions();
            Log.i(LOG_TAG_FACEBOOK,""+declinedPermissions.toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(LOG_TAG_FACEBOOK,"Call Back");
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }



    @Override
    protected void onResume() {
        super.onResume();
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
        profileTracker.startTracking();
        accessTokenTracker.startTracking();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
        profileTracker.stopTracking();
        accessTokenTracker.stopTracking();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btFBSignup) {
            LoginManager.getInstance().logInWithReadPermissions(this, permission);
        } else if (v.getId() == R.id.btSignUp) {
            Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
            signup_fname = etFirstName.getText().toString();
            signup_lname = etLastName.getText().toString();
            signup_bday = etBday.getText().toString();
            signup_address = etAddress.getText().toString();
            signup_email = etEmail.getText().toString();
            signup_mobile = etPhoneNumber.getText().toString();
            signup_password = etPassword.getText().toString();
            signup_confirmPass = etConfirmPass.getText().toString();
            signup_profession = etProfession.getText().toString();
            signup_gender = spGender.getSelectedItem().toString();

            if (!signup_password.equals(signup_confirmPass))
                showToast(this, "Password hasn't matched");
            else if ( !signup_profession.isEmpty() && !signup_fname.isEmpty() && !signup_lname.isEmpty() &&
                    !signup_bday.isEmpty() && !signup_address.isEmpty() && !signup_email.isEmpty()
                    && !signup_mobile.isEmpty() && !signup_password.isEmpty() && !signup_gender.equalsIgnoreCase("-Gender-"))
            {
                intent = new Intent(SignupActivity.this,ImageUploadActivity.class);
                startActivity(intent);
            }

            else
                showToast(this, "Fill up all the field");
        }
    }
}