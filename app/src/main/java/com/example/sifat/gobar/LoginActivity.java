package com.example.sifat.gobar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.sifat.Controller.FacebookInfoFetcher;
import com.example.sifat.Controller.GcmRegFetcher;
import com.example.sifat.Controller.ServerCommunicator;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.sifat.Utilities.CommonUtilities.*;
/**
 * Created by Sifat on 10/28/2015.
 */
public class LoginActivity extends ActionBarActivity implements View.OnClickListener {

    private ImageButton btFacebookLogin;
    private FacebookCallback<LoginResult> facebookCallback;
    private CallbackManager callbackManager;
    private List<String> permission;
    private Set<String> grantedPermissions,declinedPermissions;
    private Profile profile;
    private AccessToken accessToken;
    private LoginManager loginManager;
    private String profileName,profileID,userEmail;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private Intent loggedInIntent;
    private Button btLogin;
    private EditText etUsername, etPassword;
    private FacebookInfoFetcher facebookInfoFetcher;
    private boolean isFacebook;
    private SharedPreferences sharedPreferences;
    private ServerCommunicator serverCommunicator;
    private Toolbar toolbar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        isFacebook = false;
        sharedPreferences = getSharedPref(this);
        serverCommunicator = new ServerCommunicator(this);
        loggedInIntent=new Intent(LoginActivity.this,MapsActivity.class);
        loginManager=LoginManager.getInstance();
        callbackManager = CallbackManager.Factory.create();
        btFacebookLogin = (ImageButton) findViewById(R.id.btFBLogin);
        btLogin = (Button) findViewById(R.id.btLogin);
        btFacebookLogin.setOnClickListener(this);
        btLogin.setOnClickListener(this);
        etUsername = (EditText) findViewById(R.id.etUserEmail);
        etPassword = (EditText) findViewById(R.id.etUserPassword);
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
                Toast.makeText(LoginActivity.this, "Login has been canceled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
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
            facebookInfoFetcher.getFBInfo("id,email,first_name,last_name",this,accessToken,false);
        }
        else
        {
            Toast.makeText(LoginActivity.this, "This profile is null", Toast.LENGTH_SHORT).show();
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
        if (v.getId() == R.id.btFBLogin) {
            LoginManager.getInstance().logInWithReadPermissions(this, permission);
        } else if (v.getId() == R.id.btLogin) {
            String email = etUsername.getText().toString();
            String password = etPassword.getText().toString();
            if (!email.equalsIgnoreCase("") && !password.equalsIgnoreCase("") && email != null && password != null) {
                String gcmRegNum = sharedPreferences.getString(GCM_REGISTER_ID, "");
                serverCommunicator.login(email, password, gcmRegNum, isFacebook);
            } else {
                showToast(this, "Please enter email and password!");
            }
        }
    }
}