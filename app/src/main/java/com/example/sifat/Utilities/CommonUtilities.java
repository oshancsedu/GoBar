package com.example.sifat.Utilities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v4.view.GravityCompat;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.sifat.Controller.ServerCommunicator;
import com.example.sifat.gobar.AboutActivity;
import com.example.sifat.gobar.HelpActivity;
import com.example.sifat.gobar.HistoryActivity;
import com.example.sifat.gobar.ProfileActivity;
import com.example.sifat.gobar.R;
import com.example.sifat.gobar.SettingsActivity;
import com.example.sifat.gobar.WelcomeActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Sifat on 10/21/2015.
 */
public class CommonUtilities {

    public static String signup_fname, signup_lname, signup_bday, signup_address, signup_email,profileImgUrl,
            signup_mobile, signup_password, signup_gender, signup_confirmPass,signup_profession,signup_NID,signup_username;
    public static Bitmap proPic,NDIPic;


    //GCM message tag
    public static final String RIDE_PAYMENT="payment";

    public static final String SELECTED_DRIVER_NAME = "selectedDriverName";
    public static final String SELECTED_DRIVER_MOBILE = "selectedDriverMobile";
    public static final String SELECTED_DRIVER_RATING = "selectedDriverRating";
    public static final String SELECTED_DRIVER_ID = "selectedDriverId";
    public static final String HIRE_STATUS_MESSAGE = "hireStatus";
    public static final String GCM_MESSAGE="message";
    public static final String RIDE_REQUEST_RESPONSE = "rideResponse";


    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;
    public static final int NOTIFICATION_ID = 1;
    public static final String RESULT_ADDRESS_KEY="getAddress";
    public static final String LATLNG_DATA_EXTRA ="getLatLng";
    public static final String ADDRESS_RECIEVER="getAddressReviever";
    public static final String USER_TYPE="User";

    public static final String GMAP_KEY="gmap";
    public static final String TAXIDETAIL_RECIEVER="getTaxiDetailReviever";
    public static final String RESULT_TAXIDETAIL_KEY="getTaxiDetail";
    public static final String SRC_LATLNG = "scrLatLng";
    public static final String DIST_LATLNG = "distLatLng";
    public static final String DIST_LAT="distLat";
    public static final String DIST_LNG="distLng";
    public static final String SRC_LAT="srcLat";
    public static final String SRC_LNG="srcLng";


    public static final String NOTIFICATION_MANAGER = "notificationManager";
    public static final String IS_ON_HIRE = "isOnHire";

    /////LOG TAGs
    public static final String LOG_TAG_FACEBOOK="facebook";
    public static final String LOG_TAG_SERVICE = "Service";
    public static final String LOG_TAG_TAXIPOSITIONSERVICE="Taxi Position";
    public static final String LOG_TAG_HIRETAXI = "TaxiHire";
    public static final String LOG_TAG_SIGNUP="singup";
    public static final String LOG_TAG_GCM = "gcmloginfo";
    public static final String LOG_TAG_LOGIN="login";

    public static final String USER_RATING="userRating";
    public static final String USER_ID="userID";
    public static final String USER_BALANCE="userBalance";
    public static final String USER_PROFESSION="userProfession";
    public static final String USER_PRO_PIC_URL="proPicURL";
    public static final String USER_NID = "userNID";
    public static final String USER_REGISTRATION_ID = "userRegID";
    public static final String USER_FB_INFO="userFbInfo";
    public static final String USER_NAME="username";
    public static final String USER_EMAIL="email";
    public static final String USER_PROFESSON="profession";
    public static final String USER_FACEBOOK_ID="userId";
    public static final String USER_ADDRESS="address";
    public static final String USER_BDAY="bday";
    public static final String USER_MOBILE_NUM="mobileNum";
    public static final String USER_FNAME="fname";
    public static final String USER_LNAME="lname";
    public static final String USER_GENDER="gender";
    public static final String USER_PASSWORD="password";
    public static final String USER_N_ID="nationalID";
    public static final String USER_PRO_PIC="profile_Picture";
    public static final String USER_NID_PIC="nidPic";
    public static final String USER_TYPE_STRING="userType";
    public static final String USER_STATUS_1="not completed";
    public static final String USER_STATUS_2="completed";
    public static final String USER_STATUS_3="profile picture not found";
    public static final String USER_STATUS_4="national ID not found";
    public static final String SERVER_ACCESS_TOKEN="serverAccessToken";

    ///Web URL
    public static final String SIGN_UP_WEBSITE = "http://khep.finder-lbs.com:8001/auth/register/";
    public static final String LOGIN_WEBSITE = "http://khep.finder-lbs.com:8001/auth/login/";
    public static final String LOGOUT_WEBSITE = "http://inspireitl.com/gober/logout.php";
    public static final String COMPLETE_USER_INFO_WEBSITE="http://aimsil.com/uber/uploadImage.php";
    public static final String TAXI_POSITION_ADDRESS = "http://aimsil.com/uber/taxiposition.txt";
    public static final String PROFILE_INFO_URL = "";
    public static final String LOGIN_WITH_FB = "isLoginWithFacebook";
    public static final String USER_STATUS_WEBSITE="http://aimsil.com/uber/userInfo.php";
    public static final String RIDE_REQUEST_WEBSITE="http://aimsil.com/uber/rideRequest.php";
    public static final String END_RIDE_WEBSITE="http://aimsil.com/uber/endRide.php";


    ///GCM Registration number
    public static final String PROPERTY_APP_VERSION = "appVersion";
    public static final String SENDER_PROJECT_ID="307986532903";
    public static final String GCM_REGISTER_ID="gcmRegID";

    static ServerCommunicator serverCommunicator;

    public static void Logout(Context context)
    {
        SharedPreferences sharedPreferences = getSharedPref(context);

        String user_reg_num = sharedPreferences.getString(USER_REGISTRATION_ID, "");
        String gcmID = sharedPreferences.getString(GCM_REGISTER_ID, "");
        serverCommunicator = new ServerCommunicator(context);
        serverCommunicator.logout(gcmID, user_reg_num);
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

    public static void showToast(Context context,String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }

    public static boolean menuNavigation(MenuItem item,Context context)
    {
        Intent intent;
        switch (item.getItemId()) {

            case R.id.Search:
                showToast(context,"Search");
                break;

            case R.id.navigation_item_profile:
                intent = new Intent(context,ProfileActivity.class);
                context.startActivity(intent);
                break;

            case R.id.navigation_item_about:
                intent = new Intent(context,AboutActivity.class);
                context.startActivity(intent);
                break;

            case R.id.navigation_item_help:
                intent = new Intent(context,HelpActivity.class);
                context.startActivity(intent);
                break;

            case R.id.navigation_item_history:
                intent = new Intent(context,HistoryActivity.class);
                context.startActivity(intent);
                break;

            case R.id.navigation_item_settings:
                intent = new Intent(context,SettingsActivity.class);
                context.startActivity(intent);
                break;
        }
        return false;
    }

    public static boolean isEmail(String email)
    {
        boolean matchFound1;
        boolean returnResult=true;
        email=email.trim();
        if(email.equalsIgnoreCase(""))
            returnResult=false;
        else if(!Character.isLetter(email.charAt(0)))
            returnResult=false;
        else
        {
            Pattern p1 = Pattern.compile("^\\.|^\\@ |^_");
            Matcher m1 = p1.matcher(email.toString());
            matchFound1=m1.matches();

            Pattern p = Pattern.compile("^[a-zA-z0-9._-]+[@]{1}+[a-zA-Z0-9]+[.]{1}+[a-zA-Z]{2,4}$");
            // Match the given string with the pattern
            Matcher m = p.matcher(email.toString());

            // check whether match is found
            boolean matchFound = m.matches();

            StringTokenizer st = new StringTokenizer(email, ".");
            String lastToken = null;
            while (st.hasMoreTokens())
            {
                lastToken = st.nextToken();
            }
            if (matchFound && lastToken.length() >= 2
                    && email.length() - 1 != lastToken.length() && matchFound1==false)
            {

                returnResult= true;
            }
            else returnResult= false;
        }
        return returnResult;
    }

}