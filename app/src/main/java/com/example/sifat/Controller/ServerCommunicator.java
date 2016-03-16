package com.example.sifat.Controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.*;
import android.util.Base64;
import android.widget.Toast;

import com.example.sifat.Utilities.LoopjHttpClient;
import com.example.sifat.gobar.CompleteProfileActivity;
import com.example.sifat.gobar.ImageUploadActivity;
import com.example.sifat.gobar.LoginActivity;
import com.example.sifat.gobar.MapsActivity;
import com.example.sifat.gobar.UploadNIDInfoActivity;
import com.example.sifat.gobar.WelcomeActivity;
import com.loopj.android.http.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import cz.msebera.android.httpclient.Header;

import static com.example.sifat.Utilities.CommonUtilities.*;

/**
 * Created by sifat on 11/3/2015.
 */
public class ServerCommunicator {

    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public ServerCommunicator(Context context)
    {
        this.context=context;
        setSharedPreferences();
    }

    public void sendImage()
    {
        ByteArrayOutputStream proPicByteArrayOutputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream nidPicByteArrayOutputStream = new ByteArrayOutputStream();

        proPic.compress(Bitmap.CompressFormat.JPEG,100,proPicByteArrayOutputStream);
        String encodedProPicImage= android.util.Base64.encodeToString(proPicByteArrayOutputStream.toByteArray(), Base64.DEFAULT);

        NDIPic.compress(Bitmap.CompressFormat.JPEG,100,nidPicByteArrayOutputStream);
        String encodedNidPic= android.util.Base64.encodeToString(nidPicByteArrayOutputStream.toByteArray(), Base64.DEFAULT);


        final RequestParams requestParams = new RequestParams();
        requestParams.put("image", encodedProPicImage);
        requestParams.put("nidImage", encodedNidPic);

        final String signupWebsite = "http://aimsil.com/uber/uploadImage.php";
        LoopjHttpClient.post(signupWebsite, requestParams, new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                showToast(context, new String(responseBody));
                LoopjHttpClient.debugLoopJ(LOG_TAG_SIGNUP, "sendLocationDataToWebsite - success", signupWebsite, requestParams, responseBody, headers, statusCode, null, context);

                changeActivity(WelcomeActivity.class);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    public void sendSignUpInfo() {
        final RequestParams requestParams = new RequestParams();
        requestParams.put(USER_EMAIL, signup_email);
        requestParams.put(USER_NAME, signup_username);
        requestParams.put(USER_PASSWORD, signup_password);

        final String signupWebsite = SIGN_UP_WEBSITE;
        showToast(context, signupWebsite);
        LoopjHttpClient.post(signupWebsite, requestParams, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                showToast(context, new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                showToast(context, "Could not Signed up!");
            }
        });
    }

    public void completeUserInfo() {
        String gcmRegNum = sharedPreferences.getString(GCM_REGISTER_ID, "");

        ByteArrayOutputStream proPicByteArrayOutputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream nidPicByteArrayOutputStream = new ByteArrayOutputStream();

        proPic.compress(Bitmap.CompressFormat.JPEG, 100, proPicByteArrayOutputStream);
        String encodedProPicImage = Base64.encodeToString(proPicByteArrayOutputStream.toByteArray(), Base64.DEFAULT);

        NDIPic.compress(Bitmap.CompressFormat.JPEG, 100, nidPicByteArrayOutputStream);
        String encodedNidPic = Base64.encodeToString(nidPicByteArrayOutputStream.toByteArray(), Base64.DEFAULT);

        final RequestParams requestParams = new RequestParams();
        requestParams.put(USER_FNAME, signup_fname);
        requestParams.put(USER_LNAME, signup_lname);
        requestParams.put(USER_ADDRESS, signup_address);
        requestParams.put(USER_BDAY, signup_bday);
        requestParams.put(USER_GENDER, signup_gender);
        requestParams.put(GCM_REGISTER_ID, gcmRegNum);
        requestParams.put(USER_N_ID, signup_NID);
        requestParams.put(USER_PRO_PIC, encodedProPicImage);
        requestParams.put(USER_NID_PIC, encodedNidPic);
        requestParams.put(USER_PROFESSON, signup_profession);
        requestParams.put(USER_TYPE_STRING, USER_TYPE);
        requestParams.put(USER_MOBILE_NUM, signup_mobile);

        final String signupWebsite = COMPLETE_USER_INFO_WEBSITE;
        Toast.makeText(context, signupWebsite, Toast.LENGTH_SHORT).show();
        LoopjHttpClient.post(signupWebsite, requestParams, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                LoopjHttpClient.debugLoopJ(LOG_TAG_SIGNUP, "sendLocationDataToWebsite - success", signupWebsite, requestParams, responseBody, headers, statusCode, null, context);
                showToast(context, new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                LoopjHttpClient.debugLoopJ(LOG_TAG_SIGNUP, "sendLocationDataToWebsite - failure", signupWebsite, requestParams, responseBody, headers, statusCode, error, context);
                showToast(context, "Error!");
            }

        });
    }

    public void login(String username, String pass, String gcm_regId, boolean isFacebook) {
        final RequestParams requestParams = new RequestParams();
        requestParams.put(USER_NAME, username);
        requestParams.put(USER_PASSWORD, pass);
        requestParams.put(GCM_REGISTER_ID, gcm_regId);
        requestParams.put(LOGIN_WITH_FB, isFacebook);

        final String loginWebsite = LOGIN_WEBSITE;
        Toast.makeText(context,loginWebsite,Toast.LENGTH_SHORT).show();

        LoopjHttpClient.post(loginWebsite, requestParams, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                //LoopjHttpClient.debugLoopJ(LOG_TAG_SIGNUP, "sendLocationDataToWebsite - success", loginWebsite, requestParams, responseBody, headers, statusCode, null, context);
                String response = new String(responseBody);
                if (saveAccessToken(response)) {
                    getUserStatus();
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                //LoopjHttpClient.debugLoopJ(LOG_TAG_SIGNUP, "sendLocationDataToWebsite - failure", loginWebsite, requestParams, responseBody, headers, statusCode, error, context);
                String response = new String(responseBody);
                checkResponse(response);
            }
        });
    }

    private void getUserStatus() {

        String accessToken = sharedPreferences.getString(SERVER_ACCESS_TOKEN,"");
        final String userStatusWebsite = USER_STATUS_WEBSITE;
        final RequestParams requestParams = new RequestParams();
        requestParams.put(SERVER_ACCESS_TOKEN, accessToken);

        LoopjHttpClient.post(userStatusWebsite, requestParams, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                showToast(context,response);
                checkUserStatus(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void checkUserStatus(String res) {

        try {
            JSONObject response = new JSONObject(res);
            String status = response.getString("status");
            if(status.equalsIgnoreCase(USER_STATUS_1))
            {
                changeActivity(CompleteProfileActivity.class);
            }
            else if(status.equalsIgnoreCase(USER_STATUS_2))
            {
                saveUserInfo(response);
                changeActivity(MapsActivity.class);
            }
            else if(status.equalsIgnoreCase(USER_STATUS_3))
            {
                changeActivity(ImageUploadActivity.class);
            }
            else if(status.equalsIgnoreCase(USER_STATUS_4))
            {
                changeActivity(UploadNIDInfoActivity.class);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    public void logout(String gcmRegID, String userRegID) {
        final RequestParams requestParams = new RequestParams();
        requestParams.put(USER_REGISTRATION_ID, userRegID);
        requestParams.put(GCM_REGISTER_ID, gcmRegID);

        final String logoutWebsite = LOGOUT_WEBSITE;
        //showToast(context, logoutWebsite);

        LoopjHttpClient.get(logoutWebsite, requestParams, new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                LoopjHttpClient.debugLoopJ(LOG_TAG_SIGNUP, "sendLocationDataToWebsite - success", logoutWebsite, requestParams, responseBody, headers, statusCode, null, context);
                String response = new String(responseBody);

                if (response.equalsIgnoreCase("-1"))
                    showToast(context, "Couldn't Logout");
                else {
                    showToast(context, "Logout Successfully!");
                    eraseUserInfo();
                    changeActivity(WelcomeActivity.class);
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                LoopjHttpClient.debugLoopJ(LOG_TAG_SIGNUP, "sendLocationDataToWebsite - failure", logoutWebsite, requestParams, responseBody, headers, statusCode, error, context);
            }
        });

    }

    private void eraseUserInfo() {
        editor.remove(USER_EMAIL);
        editor.remove(USER_BDAY);
        editor.remove(USER_GENDER);
        editor.remove(USER_MOBILE_NUM);
        editor.remove(USER_ADDRESS);
        editor.remove(USER_FNAME);
        editor.remove(USER_LNAME);
        editor.remove(USER_REGISTRATION_ID);
        editor.remove(USER_NAME);
        editor.remove(USER_FACEBOOK_ID);
        editor.commit();
    }

    private void saveUserInfo(JSONObject userInfo) {
        try {
            Log.e("JSON", "1");
            editor.putString(USER_RATING, userInfo.getString(USER_RATING));
            editor.putString(USER_BALANCE, userInfo.getString(USER_BALANCE));

            editor.putString(USER_PRO_PIC_URL, userInfo.getString(USER_PRO_PIC_URL));
            editor.putString(USER_PROFESSION, userInfo.getString(USER_PROFESSION));
            //editor.putString(USER_NID, userInfo.getString("userNID"));
            Log.e("JSON", "2");
            editor.putString(USER_EMAIL, userInfo.getString(USER_EMAIL));
            editor.putString(USER_BDAY, userInfo.getString(USER_BDAY));
            editor.putString(USER_GENDER, userInfo.getString(USER_GENDER));
            editor.putString(USER_MOBILE_NUM, userInfo.getString(USER_MOBILE_NUM));
            editor.putString(USER_ADDRESS, userInfo.getString(USER_ADDRESS));
            editor.putString(USER_FNAME, userInfo.getString(USER_FNAME));
            editor.putString(USER_LNAME, userInfo.getString(USER_LNAME));
            editor.putString(USER_NAME, userInfo.getString(USER_FNAME) + " " + userInfo.getString(USER_LNAME));
            Log.e("JSON", "3");
            //editor.putString(USER_REGISTRATION_ID, userInfo.getString("id"));
            editor.commit();

            showToast(context, sharedPreferences.getString(USER_ADDRESS, "") + sharedPreferences.getString(USER_EMAIL, "") +
                    sharedPreferences.getString(USER_FNAME, "") + sharedPreferences.getString(USER_LNAME, "") +
                    sharedPreferences.getString(USER_BDAY, "") + sharedPreferences.getString(USER_GENDER, "") +
                    sharedPreferences.getString(USER_MOBILE_NUM, ""));


        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("JSON", "ERROR");
        }
    }


    private void setSharedPreferences() {
        sharedPreferences = getSharedPref(context);
        editor = sharedPreferences.edit();
    }

    private void changeActivity(Class activityclass) {

        Intent intent = new Intent(context,activityclass);
        context.startActivity(intent);
    }

    private void checkResponse(String response) {

        try {
            JSONObject errorStatus= new JSONObject(response);
            Log.i(LOG_TAG_LOGIN,errorStatus.getString("non_field_errors"));
            String res=errorStatus.getString("non_field_errors");
            res= res.substring(2,res.length()-2);
            showToast(context,res);

        } catch (JSONException e) {
            Log.i(LOG_TAG_LOGIN,"Error");
        }
    }

    public boolean saveAccessToken(String response) {
        JSONObject accessToken;
        try {
            accessToken = new JSONObject(response);
            if(accessToken.has("auth_token"))
            {
                showToast(context, accessToken.getString("auth_token"));
                editor.putString(SERVER_ACCESS_TOKEN, accessToken.getString("auth_token"));
                editor.commit();
                return true;
            }
        } catch (JSONException e) {
            showToast(context, "Problem with login. Try again later");
            return false;
        }
        return false;
    }
}