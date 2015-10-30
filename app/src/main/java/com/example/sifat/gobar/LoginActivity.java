package com.example.sifat.gobar;

import android.content.Context;
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
import android.widget.ImageButton;
import android.widget.Toast;

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
public class LoginActivity extends ActionBarActivity implements View.OnClickListener {

    private ImageButton loginButton;
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
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Intent loggedInIntent;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.login);
        init();
    }

    private void init() {
        sharedPreferences = getSharedPreferences(String.valueOf(R.string.sharedPref), Context.MODE_PRIVATE);
        editor= sharedPreferences.edit();
        loggedInIntent=new Intent(LoginActivity.this,MapsActivity.class);
        loginManager=LoginManager.getInstance();
        callbackManager = CallbackManager.Factory.create();
        loginButton = (ImageButton) findViewById(R.id.btFBLogin);
        loginButton.setOnClickListener(this);
        permission=new ArrayList<>();
        grantedPermissions=new HashSet<>();
        declinedPermissions=new HashSet<>();
        //permission.add("user_friends");
        //permission.add("user_status");
        permission.add("email");
        //permission.add("user_birthday");
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
        Log.i(LOG_TAG_FACEBOOK,"access token: "+accessToken);
        profile = Profile.getCurrentProfile();
        profileName=profile.getName();
        profileID=profile.getId();


        if (profile != null) {
            GraphRequest request= GraphRequest.newMeRequest(accessToken,new GraphRequest.GraphJSONObjectCallback(){
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    JSONObject json = response.getJSONObject();
                    Log.i(LOG_TAG_FACEBOOK,response.toString());
                    try {
                        if(json != null){
                            userEmail = json.getString("email");
                            Log.i(LOG_TAG_FACEBOOK,userEmail);
                            saveLoginInfo();
                            startActivity(loggedInIntent);
                            finish();
                        }

                    } catch (JSONException e) {
                        Toast.makeText(LoginActivity.this,"Problem with login",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "email");
            request.setParameters(parameters);
            request.executeAsync();
        }
        else
        {
            Toast.makeText(LoginActivity.this, "This profile is null", Toast.LENGTH_SHORT).show();
            grantedPermissions=accessToken.getPermissions();
            declinedPermissions=accessToken.getDeclinedPermissions();
            Log.i(LOG_TAG_FACEBOOK,""+declinedPermissions.toString());
        }
    }

    private void saveLoginInfo() {
        editor.putString(USER_NAME,profileName);
        editor.putString(USER_EMAIL,userEmail);
        editor.putString(USER_ID,profileID);
        editor.commit();
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
        LoginManager.getInstance().logInWithReadPermissions(this,permission);
    }
}