package com.example.sifat.Services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;

import com.example.sifat.gobar.R;

/**
 * Created by Sifat on 10/27/2015.
 */
public class TaxiHireConfirmationNotify extends IntentService {


    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder builder;
    private Context context;
    private Intent intent;
    private String message;

    public TaxiHireConfirmationNotify() {
        super("TaxiHireConfirmationNotify");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Notify();
    }

    public void Notify() {
        mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        android.support.v4.app.NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("GoBar")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(message))
                        .setContentText(message).setSound(sound).setLights(Color.CYAN, 1, 1).setVibrate(new long[]{100, 100, 100, 100, 100});

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
