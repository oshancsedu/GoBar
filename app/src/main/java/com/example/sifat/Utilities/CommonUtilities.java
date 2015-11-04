package com.example.sifat.Utilities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.sifat.gobar.R;
import com.example.sifat.gobar.WelcomeActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Created by Sifat on 10/21/2015.
 */
public class CommonUtilities {

    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;
    public static final int NOTIFICATION_ID = 1;
    public static final String RESULT_ADDRESS_KEY="getAddress";
    public static final String LATLNG_DATA_EXTRA ="getLatLng";
    public static final String ADDRESS_RECIEVER="getAddressReviever";

    public static final String GMAP_KEY="gmap";
    public static final String TAXIDETAIL_RECIEVER="getTaxiDetailReviever";
    public static final String RESULT_TAXIDETAIL_KEY="getTaxiDetail";
    public static final String HIRE_STATUS_MESSAGE = "hireTaxiStatus";
    public static final String SRC_LATLNG = "scrLatLng";
    public static final String DIST_LATLNG = "distLatLng";
    public static final String SELECTED_DRIVER_NAME = "selectedDriverName";
    public static final String SELECTED_DRIVER_MOBILE = "selectedDriverMobile";
    public static final String SELECTED_DRIVER_RATING = "selectedDriverRating";
    public static final String SELECTED_DRIVER_ID = "selectedDriverId";
    public static final String NOTIFICATION_MANAGER = "notificationManager";
    public static final String IS_ON_HIRE = "isOnHire";

    /////LOG TAGs
    public static final String LOG_TAG_FACEBOOK="facebook";
    public static final String LOG_TAG_SERVICE = "Service";
    public static final String LOG_TAG_TAXIPOSITIONSERVICE="Taxi Position";
    public static final String LOG_TAG_HIRETAXI = "Taxi Hire";
    public static final String LOG_TAG_SIGNUP="singup";

    public static final String USER_FB_INFO="userFbInfo";
    public static final String USER_NAME="userName";
    public static final String USER_EMAIL="email";
    public static final String USER_ID="userId";
    public static final String USER_ADDRESS="address";
    public static final String USER_BDAY="bday";
    public static final String USER_MOBILE_NUM="mobileNum";
    public static final String USER_FNAME="fname";
    public static final String USER_LNAME="lname";
    public static final String USER_GENDER="gender";
    public static final String USER_PASSWORD="password";

    public static final String SIGN_UP_WEBSITE="http://inspireitl.com/gober/signup.php";


    ////GCM Registration number

    public static final String PROPERTY_APP_VERSION = "appVersion";
    public static final String SENDER_PROJECT_ID="307986532903";
    public static final String GCM_REGISTER_ID="gcmRegID";
    public static final String LOG_TAG_GCM="gcmloginfo";

    public static void Logout(Context context)
    {
        SharedPreferences sharedPreferences = getSharedPref(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_NAME,"");
        editor.putString(USER_ID,"");
        editor.putString(USER_EMAIL, "");
        editor.commit();
        Intent loginActivityIntent = new Intent(context, WelcomeActivity.class);
        context.startActivity(loginActivityIntent);
    }

    public static SharedPreferences getSharedPref(Context context)
    {
        SharedPreferences sharedPreferences= context.getSharedPreferences(String.valueOf(R.string.sharedPref), Context.MODE_PRIVATE);
        return sharedPreferences;
    }

    /**
     * Method to verify google play services on the device
     * */
    public static boolean checkPlayServices(Context context) {
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS) {
            return true;
        } else {
            Toast.makeText(context, "Google play service not found", Toast.LENGTH_LONG).show();
            return false;
        }
    }
}
