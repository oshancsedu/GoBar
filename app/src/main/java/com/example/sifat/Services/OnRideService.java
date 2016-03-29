package com.example.sifat.Services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.example.sifat.Utilities.CommonUtilities;
import com.example.sifat.gobar.R;
import com.example.sifat.gobar.UserTaxiStatus;

import static com.example.sifat.Utilities.CommonUtilities.*;

/**
 * Created by sifat on 2/5/2016.
 */
public class OnRideService extends IntentService {

    private Intent intent;
    private String userName, userID, status, message;
    private float userRating;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;
    private Bundle bundle;
    private boolean isHired;
    private Context context;

    public OnRideService() {
        super("OnRideService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        init();
        bundle = intent.getExtras();

        status = bundle.getString(HIRE_STATUS_MESSAGE);
        message = bundle.getString(GCM_MESSAGE);
        Log.i(LOG_TAG_GCM, status);
        if (status.equalsIgnoreCase("OK")) {
            isHired = true;
            userName = bundle.getString(SELECTED_DRIVER_NAME);
            Log.i(LOG_TAG_GCM, userName);
            userID = bundle.getString(SELECTED_DRIVER_ID);
            userRating = Float.parseFloat(bundle.getString(SELECTED_DRIVER_RATING));
            Log.i(LOG_TAG_GCM, "user Rate: "+userRating);

        } else {
            isHired = false;
        }
        bundle.remove(HIRE_STATUS_MESSAGE);
        Notify();
        stopSelf();
    }

    private void init() {
        context = getApplicationContext();
        sharedPreferences = CommonUtilities.getSharedPref(this);
        editor = sharedPreferences.edit();
    }


    public void Notify() {
        notificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, UserTaxiStatus.class);
        //bundle.putParcelable(NOTIFICATION_MANAGER, (Parcelable) mNotificationManager);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        builder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.notificationicon)
                .setContentTitle("GoBar")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message))
                .setContentText(message).setSound(sound).setLights(Color.CYAN, 1, 1)
                .setVibrate(new long[]{100, 100, 100, 100, 100});

        if (isHired) {
            builder.setOngoing(true);
            editor.putString(SELECTED_DRIVER_NAME, userName);
            editor.putString(SELECTED_DRIVER_ID, userID);
            editor.putFloat(SELECTED_DRIVER_RATING, userRating);
            editor.putBoolean(IS_ON_HIRE, isHired);
            editor.commit();
            builder.setContentIntent(contentIntent);
        }
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}