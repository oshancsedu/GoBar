package com.example.sifat.Services;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.example.sifat.Utilities.CommonUtilities.*;

/**
 * Created by Sifat on 10/21/2015.
 */
public class AddressFetcher extends IntentService {

    public ResultReceiver mReceiver;

    public AddressFetcher() {
        super("AddressFetcher");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.i("Service","Handle");
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        LatLng latLng = intent.getParcelableExtra(LATLNG_DATA_EXTRA);
        mReceiver = intent.getParcelableExtra(ADDRESS_RECIEVER);
        Log.i("Service", "Lat: " + latLng.latitude + "\nLng: " + latLng.longitude);

        List<Address> addresses = null;
        String errorMessage = "";

        try {
            addresses = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            errorMessage = "Service Not Available.\nPlease check your internet connection.";
            Log.e(LOG_TAG_SERVICE, errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            errorMessage = "Invalid Latitude & Longitude";
            /*Log.e(LOG_TAG_SERVICE, errorMessage + ". " +
                    "Latitude = " + latLng.latitude +
                    ", Longitude = " +
                    latLng.longitude, illegalArgumentException);*/
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size()  == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = "No Address Found";
                //Log.e(LOG_TAG_SERVICE, errorMessage);
            }
            deliverResultToReceiver(FAILURE_RESULT, errorMessage);
        } else {
            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<String>();

            // Fetch the address lines using getAddressLine,
            // join them, and send them to the thread.
            for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }
            //Log.i(LOG_TAG_SERVICE, "Address Found");
            deliverResultToReceiver(SUCCESS_RESULT,
                    TextUtils.join(System.getProperty("line.separator"),
                            addressFragments));
        }

        stopSelf();
    }

    private void deliverResultToReceiver(int resultCode, String message) {
        //Log.i(LOG_TAG_SERVICE,"Deliver -> "+message);
        Bundle bundle = new Bundle();
        bundle.putString(RESULT_ADDRESS_KEY, message);
        //Log.i(LOG_TAG_SERVICE, "Sending -> " + message);
        mReceiver.send(resultCode, bundle);
    }
}
