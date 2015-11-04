package com.example.sifat.Controller;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.sifat.Utilities.LoopjHttpClient;
import com.example.sifat.gobar.MapsActivity;
import com.loopj.android.http.*;

import org.apache.http.Header;

import static com.example.sifat.Utilities.CommonUtilities.*;
/**
 * Created by sifat on 11/3/2015.
 */
public class ServerCommunicator {

    Context context;
    public ServerCommunicator(Context context)
    {
        this.context=context;
    }

    public void sendSignupInfo(String fName,String lName,String address,String bday,String gender,String password,String email,String phone) {
        final RequestParams requestParams = new RequestParams();
        requestParams.put(USER_EMAIL, email);
        requestParams.put(USER_FNAME, fName);
        requestParams.put(USER_LNAME, lName);
        requestParams.put(USER_MOBILE_NUM, phone);
        requestParams.put(USER_PASSWORD, password);
        requestParams.put(USER_ADDRESS, address);
        requestParams.put(USER_BDAY, bday);
        requestParams.put(USER_GENDER, gender);

        final String signupWebsite = SIGN_UP_WEBSITE;
        Toast.makeText(context,signupWebsite,Toast.LENGTH_SHORT).show();
        LoopjHttpClient.get(signupWebsite, requestParams, new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                LoopjHttpClient.debugLoopJ(LOG_TAG_SIGNUP, "sendLocationDataToWebsite - success", signupWebsite, requestParams, responseBody, headers, statusCode, null, context);
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                LoopjHttpClient.debugLoopJ(LOG_TAG_SIGNUP, "sendLocationDataToWebsite - failure", signupWebsite, requestParams, responseBody, headers, statusCode, error, context);
            }

        });

    }

    public void login(String email,String pass,String gcm_regId){
        final RequestParams requestParams = new RequestParams();
        requestParams.put(USER_EMAIL,email);
        requestParams.put(USER_PASSWORD,pass);
        requestParams.put(GCM_REGISTER_ID,gcm_regId);

        final String loginWebsite = LOGIN_WEBSITE;
        Toast.makeText(context,loginWebsite,Toast.LENGTH_SHORT).show();
        LoopjHttpClient.get(loginWebsite, requestParams, new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                LoopjHttpClient.debugLoopJ(LOG_TAG_SIGNUP, "sendLocationDataToWebsite - success", loginWebsite, requestParams, responseBody, headers, statusCode, null, context);
                String response= new String(responseBody);
                if(response.equalsIgnoreCase("failed"))
                    showToast(context,"Login Failed");
                else
                {
                    showToast(context,response);
                    changeActivity(MapsActivity.class);
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                LoopjHttpClient.debugLoopJ(LOG_TAG_SIGNUP, "sendLocationDataToWebsite - failure", loginWebsite, requestParams, responseBody, headers, statusCode, error, context);
            }
        });
    }

    private void changeActivity(Class activityclass) {

        Intent intent = new Intent(context,activityclass);
        context.startActivity(intent);
    }
}





