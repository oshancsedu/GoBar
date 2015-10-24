package com.example.sifat.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.ResultReceiver;
import android.widget.Toast;

import com.example.sifat.Services.TaxiLocation;

import static com.example.sifat.Utilities.CommonUtilities.TAXIDETAIL_RECIEVER;

/**
 * Created by Sifat on 10/23/2015.
 */
public class TaxiDetailReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"Into the Receiver",Toast.LENGTH_SHORT).show();
        startTaxiDetailService(context);
    }


    protected  void startTaxiDetailService(Context context)
    {
        Intent intent = new Intent(context, TaxiLocation.class);
        //Toast.makeText(context,"Start Loc service ",Toast.LENGTH_SHORT).show();
        context.startService(intent);
    }
}
