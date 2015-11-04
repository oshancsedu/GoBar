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

import static com.example.sifat.Utilities.CommonUtilities.*;

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
    private ServerCommunicator serverCommunicator;
    private String gcmRegNum;

    public void getFBInfo(String param, final Context context,AccessToken accessToken, final boolean isSigningup)
    {
        bundle = new Bundle();
        sharedPreferences= getSharedPref(context);
        editor = sharedPreferences.edit();
        serverCommunicator=new ServerCommunicator(context);

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
                            gcmRegNum=sharedPreferences.getString(GCM_REGISTER_ID,"");
                            if(!gcmRegNum.isEmpty() && !gcmRegNum.equalsIgnoreCase(""))
                                serverCommunicator.login(email,"",gcmRegNum);
                            else
                            {
                                GcmRegFetcher gcmRegFetcher = new GcmRegFetcher();
                                gcmRegFetcher.fetchGcmRegNumber(context);
                            }
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
        editor.putString(USER_FACEBOOK_ID, userID);
        editor.commit();
    }
}
