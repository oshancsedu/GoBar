package com.example.sifat.Controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

import static com.example.sifat.Utilities.CommonUtilities.*;

/**
 * Created by sifat on 11/4/2015.
 */
public class GcmRegFetcher {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private GoogleCloudMessaging gcm;
    private Intent intent;
    private String gcmRegID;
    //private ServerCommunicator serverCommunicator;

    public void fetchGcmRegNumber(Context context, Intent intent)
    {
        //serverCommunicator = new ServerCommunicator(context);
        this.intent = intent;
        Log.i(LOG_TAG_GCM, "gcm fetching");
        sharedPreferences = getSharedPref(context);
        registerInBackground(context);
    }

    private void registerInBackground(final Context context) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    gcmRegID = gcm.register(SENDER_PROJECT_ID);
                    msg = "Device registered, registration ID=" + gcmRegID;
                    Log.i(LOG_TAG_GCM, gcmRegID);
                    storeRegistrationId(gcmRegID);
                    //serverCommunicator.login(email, password, gcmRegID,isFacebook);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    Log.i(LOG_TAG_GCM,"Error :" + ex.getMessage());
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                context.startActivity(intent);
                //serverCommunicator.login(email, password, gcmRegID, isFacebook);
            }
        }.execute(null, null, null);
    }

    private void storeRegistrationId(String gcmRegID) {

        editor=sharedPreferences.edit();
        editor.putString(GCM_REGISTER_ID,gcmRegID);
        editor.commit();
    }
}