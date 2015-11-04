package com.example.sifat.Controller;

import android.content.Context;
import android.widget.Toast;

import com.example.sifat.Utilities.LoopjHttpClient;
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
}
