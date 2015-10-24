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
    public ResultReceiver mReceiver;
    @Override
    public void onReceive(Context context, Intent intent) {
        String name= intent.getExtras().getString("test");
        mReceiver=intent.getParcelableExtra(TAXIDETAIL_RECIEVER);
        if(mReceiver!=null)
        Toast.makeText(context,"Into the Receiver: "+name,Toast.LENGTH_SHORT).show();
        startTaxiDetailService(context);
    }


    protected  void startTaxiDetailService(Context context)
    {
        Intent intent = new Intent(context, TaxiLocation.class);
        intent.putExtra(TAXIDETAIL_RECIEVER, mReceiver);
        Toast.makeText(context,"Start Loc service ",Toast.LENGTH_SHORT).show();
        context.startService(intent);
    }
}
