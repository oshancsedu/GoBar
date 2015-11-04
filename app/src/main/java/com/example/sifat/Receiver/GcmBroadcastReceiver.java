package com.example.sifat.Receiver;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.example.sifat.Services.GcmIntentService;

import static com.example.sifat.Utilities.CommonUtilities.*;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Explicitly specify that GcmIntentService will handle the intent.
        ComponentName comp = new ComponentName(context.getPackageName(),GcmIntentService.class.getName());
        // Start the service, keeping the device awake while it is launching.

        String message = intent.getExtras().getString("message");
        String type = intent.getExtras().getString("type");
        Log.i(LOG_TAG_GCM, message);
        Log.i(LOG_TAG_GCM,type);


        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
}
