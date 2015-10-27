package com.example.sifat.Services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import static com.example.sifat.Utilities.CommonUtilities.*;


import com.example.sifat.gobar.MapsActivity;
import com.example.sifat.gobar.R;
import com.example.sifat.gobar.UserTaxiStatus;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Sifat on 10/27/2015.
 */
public class TaxiHireConfirmationNotify extends IntentService {


    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder builder;
    private Context context;
    private LatLng srcLatLng, distLatLng;
    private String message, status;
    private boolean isHired;
    private Bundle bundle;

    public TaxiHireConfirmationNotify() {
        super("TaxiHireConfirmationNotify");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        context = getApplicationContext();
        bundle = intent.getExtras();
        status = bundle.getString(HIRE_STATUS_MESSAGE);
        if (status.equalsIgnoreCase("OK")) {
            isHired = true;
            message = "You have hired your taxi seccessfully";
        } else {
            isHired = false;
            message = "Sorry !! This taxi couldn't be hired.Try another one.";
        }
        bundle.remove(HIRE_STATUS_MESSAGE);
        Notify();
        stopSelf();
    }

    public void Notify() {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, UserTaxiStatus.class);
        intent.putExtras(bundle);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        android.support.v4.app.NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("GoBar")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(message))
                        .setContentText(message).setSound(sound).setLights(Color.CYAN, 1, 1).setVibrate(new long[]{100, 100, 100, 100, 100});
        if (isHired)
            mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}