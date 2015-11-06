package com.example.sifat.Utilities;

import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;

import static com.example.sifat.Utilities.CommonUtilities.LOG_TAG_FACEBOOK;

/**
 * Created by sifat on 11/6/2015.
 */
public class FacebookLoginUtility {

    /*public static AccessToken getAccessTokenTracker(){
        AccessToken accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                accessToken=currentAccessToken;
                profileID=currentAccessToken.getUserId();
                Log.i(LOG_TAG_FACEBOOK, "" + currentAccessToken + "-" + profileID);
            }
        };
        return accessTokenTracker;
    }*/

}
