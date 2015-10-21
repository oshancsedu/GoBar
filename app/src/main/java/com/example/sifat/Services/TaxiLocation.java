package com.example.sifat.Services;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class TaxiLocation extends IntentService{

    public TaxiLocation() {
        super("TaxiLocation");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

}
