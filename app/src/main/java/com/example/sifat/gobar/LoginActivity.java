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
import android.widget.ImageButton;
import android.widget.Toast;

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
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.sifat.Utilities.CommonUtilities.*;
/**
 * Created by Sifat on 10/28/2015.
 */
public class LoginActivity extends ActionBarActivity implements View.OnClickListener {

    private ImageButton loginButton;
    private FacebookCallback<LoginResult> facebookCallback;
    private CallbackManager callbackManager;
    private List<String> permission;
    private Set<String> grantedPermissions,declinedPermissions;
    private Profile profile;
    private AccessToken accessToken;
    private LoginManager loginManager;
    private String profileName,profileID;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.login);
        init();
    }

    private void init() {
        loginManager=LoginManager.getInstance();
        callbackManager = CallbackManager.Factory.create();
        loginButton = (ImageButton) findViewById(R.id.btFBLogin);
        loginButton.setOnClickListener(this);
        permission=new ArrayList<>();
        grantedPermissions=new HashSet<>();
        declinedPermissions=new HashSet<>();
        permission.add("user_friends");
        permission.add("user_status");
        permission.add("email");
        permission.add("user_birthday");
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
                Log.i(LOG_TAG_FACEBOOK,"Cancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.i(LOG_TAG_FACEBOOK,"Error");
            }
        };
        loginManager.registerCallback(callbackManager, facebookCallback);
    }

    private void login(LoginResult loginResult) {
        accessToken = loginResult.getAccessToken();
        Log.i(LOG_TAG_FACEBOOK,"access token: "+accessToken);
        profile = Profile.getCurrentProfile();
        if (profile != null) {
            Log.i(LOG_TAG_FACEBOOK, profile.getName());
            Log.i(LOG_TAG_FACEBOOK, profile.getLastName());
            Log.i(LOG_TAG_FACEBOOK, profile.getId());
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
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    public void onClick(View v) {

        LoginManager.getInstance().logInWithReadPermissions(this,permission);
    }
}
