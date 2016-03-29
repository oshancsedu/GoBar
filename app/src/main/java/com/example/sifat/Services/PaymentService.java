package com.example.sifat.Services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.example.sifat.gobar.R;

import static com.example.sifat.Utilities.CommonUtilities.LOG_TAG_GCM;
import static com.example.sifat.Utilities.CommonUtilities.NOTIFICATION_ID;
import static com.example.sifat.Utilities.CommonUtilities.USER_BALANCE;
import static com.example.sifat.Utilities.CommonUtilities.getSharedPref;

/**
 * Created by Md. Sifat-Ul Haque on 3/25/2016.
 */
public class PaymentService extends IntentService {


    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context mContext;
    private Bundle bundle;
    private String message;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;
    private float mBalance;

    public PaymentService() {
        super("PaymentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        init();
        bundle = intent.getExtras();
        message = bundle.getString("cost");
        mBalance = Float.parseFloat(bundle.getString("total"));
        editor.putString(USER_BALANCE,""+mBalance);
        editor.commit();
        message="Taka "+message+" has been added to your account";
        Log.i(LOG_TAG_GCM, message);
        Notify();
        stopSelf();
    }

    private void init() {
        mContext = getApplicationContext();
        sharedPreferences = getSharedPref(this);
        editor = sharedPreferences.edit();
    }


    public void Notify() {
        notificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
       Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        builder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.notificationicon)
                .setContentTitle("GoBar")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message))
                .setContentText(message).setSound(sound).setLights(Color.CYAN, 1, 1)
                .setVibrate(new long[]{100, 100, 100, 100, 100});

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
