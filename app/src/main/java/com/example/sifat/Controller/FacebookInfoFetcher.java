package com.example.sifat.Controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.sifat.Domain.FacebookInfo;
import com.example.sifat.gobar.MapsActivity;
import com.example.sifat.gobar.R;
import com.example.sifat.gobar.ValidationActivity;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.sifat.Utilities.CommonUtilities.LOG_TAG_FACEBOOK;
import static com.example.sifat.Utilities.CommonUtilities.USER_EMAIL;
import static com.example.sifat.Utilities.CommonUtilities.USER_FB_INFO;
import static com.example.sifat.Utilities.CommonUtilities.USER_ID;
import static com.example.sifat.Utilities.CommonUtilities.USER_NAME;

/**
 * Created by sifat on 10/31/2015.
 */
public class FacebookInfoFetcher {

    FacebookInfo facebookInfo;
    String lastName,firstName,email,address,bday,gender,userID;
    private Intent intent;
    private Bundle bundle;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public void getFBInfo(String param, final Context context,AccessToken accessToken, final boolean isSigningup)
    {
        bundle = new Bundle();
        sharedPreferences= context.getSharedPreferences(String.valueOf(R.string.sharedPref),context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        GraphRequest request= GraphRequest.newMeRequest(accessToken,new GraphRequest.GraphJSONObjectCallback(){
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                JSONObject json = response.getJSONObject();
                Log.i(LOG_TAG_FACEBOOK, response.toString());
                try {
                    if(json != null){
                        email = json.getString("email");
                        firstName=json.getString("first_name");
                        lastName=json.getString("last_name");
                        userID=json.getString("id");
                        Log.i(LOG_TAG_FACEBOOK, email);
                        if(isSigningup)
                        {
                            bday=json.getString("birthday");
                            //JSONObject loc=json.getJSONObject("location");
                            address=json.getJSONObject("location").getString("name");
                            gender=json.getString("gender");
                            Log.i(LOG_TAG_FACEBOOK,firstName+lastName+bday+address+gender+email);
                            facebookInfo = new FacebookInfo(email,lastName,firstName,address,bday,gender);
                            bundle.putSerializable(USER_FB_INFO,facebookInfo);
                            intent=new Intent(context, ValidationActivity.class);
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                        }
                        else
                        {
                            saveLoginInfo();
                            intent=new Intent(context, MapsActivity.class);
                            context.startActivity(intent);
                        }
                    }

                } catch (JSONException e) {
                    Toast.makeText(context, "Problem with login", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", param);
        request.setParameters(parameters);
        request.executeAsync();
    }
    private void saveLoginInfo() {
        editor.putString(USER_NAME,firstName+" "+lastName);
        editor.putString(USER_EMAIL,email);
        editor.putString(USER_ID, userID);
        editor.commit();
    }
}
