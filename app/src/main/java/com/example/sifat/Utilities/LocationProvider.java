package com.example.sifat.Utilities;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Sifat on 10/12/2015.
 */
public class LocationProvider implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private LocationRequest mLocationRequest;
    private int UPDATE_INTERVAL = 10000; // 10 sec
    private int FATEST_INTERVAL = 5000; // 5 sec
    private int DISPLACEMENT = 10; // 10 meters
    private int count;
    private float minAccuracy;
    private Context context;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    public LocationProvider(Context c){
        context=c;
    }

    public void getMyLocaton(){
        if (checkPlayServices()) {
            init();
            buildGoogleApiClient();
            mGoogleApiClient.connect();
        }
        //return null;
    }

    /**
     * Creating google api client object
     * */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    private void init() {
        minAccuracy = 50.0f;
    }


    @Override
    public void onConnected(Bundle bundle) {
        createLocationRequest();
        startLocationUpdates();
    }

    private void getLatLng() {
        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();
            float accuracy=mLastLocation.getAccuracy();
            Toast.makeText(context,"Lat : "+latitude+"\nLong : "+longitude+"\nAccuracy : "+
                    mLastLocation.getAccuracy()+"\nMin : "+minAccuracy,Toast.LENGTH_SHORT).show();
            if(accuracy < minAccuracy)
            {
                stopLocationUpdates();

            }
        }

    }


    /**
     * Creating location request object
     * */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    /**
     * Starting the location updates
     **/
    protected void startLocationUpdates() {
        Toast.makeText(context,"startLocationUpdates",Toast.LENGTH_SHORT).show();
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

    }

    /**
     * Stopping the location updates
     * */
    private void stopLocationUpdates() {
        Toast.makeText(context,"stopLocationUpdates",Toast.LENGTH_SHORT).show();
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation=location;
        getLatLng();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(context,"Failed to detect current location",Toast.LENGTH_SHORT).show();
        return;
    }

    /**
     * Method to verify google play services on the device
     * */
    private boolean checkPlayServices() {
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS) {
            return true;
        } else {
            Toast.makeText(context, "Google play service not found", Toast.LENGTH_LONG).show();
            return false;
        }
    }
}
