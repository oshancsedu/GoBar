package com.example.sifat.Receiver;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.example.sifat.Services.GcmIntentService;
import com.example.sifat.Services.OnRideService;
import com.example.sifat.Services.PaymentService;
import com.example.sifat.Utilities.CommonUtilities;

import static com.example.sifat.Utilities.CommonUtilities.*;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

    private ComponentName comp;

    @Override
    public void onReceive(Context context, Intent intent) {

        //String message = intent.getExtras().getString("srcLat");
        String type = intent.getExtras().getString("type");
        //String cost = intent.getExtras().getString("total");
        //Log.i(LOG_TAG_GCM, message);
        Log.i(LOG_TAG_GCM, type);
        //Log.i(LOG_TAG_GCM, ""+cost);

        if(type.equalsIgnoreCase(CommonUtilities.RIDE_REQUEST_RESPONSE))
        {
            comp = new ComponentName(context.getPackageName(),OnRideService.class.getName());
        }

        else if(type.equalsIgnoreCase(RIDE_PAYMENT))
        {
            comp = new ComponentName(context.getPackageName(),PaymentService.class.getName());
        }
        else
        {
            comp = new ComponentName(context.getPackageName(),GcmIntentService.class.getName());
        }

        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
}
