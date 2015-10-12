package com.example.sifat.gobar;

import com.example.sifat.Utilities.LocationProvider;
import com.github.polok.routedrawer.RouteApi;
import com.github.polok.routedrawer.RouteDrawer;
import com.github.polok.routedrawer.RouteRest;
import com.github.polok.routedrawer.model.Routes;
import com.github.polok.routedrawer.model.TravelMode;
import com.github.polok.routedrawer.parser.RouteJsonParser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;


/**
 * This shows how to create a simple activity with a map and a marker on the map.
 */
public class MapsActivity extends ActionBarActivity implements OnMapReadyCallback,
        View.OnClickListener,RouteApi,
        SearchView.OnQueryTextListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    private UiSettings mUiSettings;
    private EditText etLocation;
    private Button btSearch;
    private Geocoder geocoder;
    List<Address> addressList;
    private Toolbar toolbar;
    private SearchView searchView;
    private LocationProvider locationProvider;
    private LocationRequest mLocationRequest;
    private int UPDATE_INTERVAL = 10000; // 10 sec
    private int FATEST_INTERVAL = 5000; // 5 sec
    private int DISPLACEMENT = 10; // 10 meters
    private int count;
    private float minAccuracy;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private CameraPosition showMyLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Toast.makeText(this,"On Create",Toast.LENGTH_SHORT).show();
        init();

        MapFragment mapFragment =
                (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void init() {
        toolbar= (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        minAccuracy = 5.0f;
        getMyLocation();
    }

    @Override
    public void onClick(View view) {

    }


    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        mMap=map;

        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        mMap.setMyLocationEnabled(true);
        mMap.setBuildingsEnabled(true);
        mUiSettings = mMap.getUiSettings();

        // Keep the UI Settings state in sync with the checkboxes.
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setCompassEnabled(true);
        mUiSettings.setMyLocationButtonEnabled(true);
        mMap.setMyLocationEnabled(true);
        mUiSettings.setScrollGesturesEnabled(true);
        mUiSettings.setZoomGesturesEnabled(true);
        mUiSettings.setTiltGesturesEnabled(true);
        mUiSettings.setRotateGesturesEnabled(true);

        //getRoute();

    }

    /********
     *
     *  Get My Location & set starting marker
     *
     * *******/

    private void getMyLocation()
    {
        if (checkPlayServices()) {
            buildGoogleApiClient();
            mGoogleApiClient.connect();
        }
    }

    /**
     * Creating google api client object
     * */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
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
            Toast.makeText(this,"Lat : "+latitude+"\nLong : "+longitude+"\nAccuracy : "+
                    mLastLocation.getAccuracy()+"\nMin : "+minAccuracy,Toast.LENGTH_SHORT).show();
            stopLocationUpdates();
            LatLng myLatLng = new LatLng(latitude,longitude);

            mMap.addMarker(new MarkerOptions()
                            .position(myLatLng)
                            .title("Marker")
            );

            mMap.addMarker(new MarkerOptions()
                            .position(myLatLng)
                            .title("Marker")
                            .draggable(true)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.myposition))
            );

            showMyLocation = new CameraPosition.Builder().target(myLatLng)
                    .zoom(15.5f)
                    .bearing(340)
                    .tilt(50)
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(showMyLocation),4000,null);

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
        Toast.makeText(this,"startLocationUpdates",Toast.LENGTH_SHORT).show();
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

    }

    /**
     * Stopping the location updates
     * */
    private void stopLocationUpdates() {
        Toast.makeText(this,"stopLocationUpdates",Toast.LENGTH_SHORT).show();
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
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
        Toast.makeText(this,"Failed to detect current location",Toast.LENGTH_SHORT).show();
        return;
    }

    /**
     * Method to verify google play services on the device
     * */
    private boolean checkPlayServices() {
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS) {
            return true;
        } else {
            Toast.makeText(this, "Google play service not found", Toast.LENGTH_LONG).show();
            return false;
        }
    }


    /********
     *
     *  Get A route Between source & destination
     *
     * ********/


    private void getRoute()
    {
        final RouteDrawer routeDrawer = new RouteDrawer.RouteDrawerBuilder(mMap)
                .withColor(Color.BLUE)
                .withWidth(5)
                .withAlpha(0.5f)
                .withMarkerIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                .build();

        RouteRest routeRest= new RouteRest();
        routeRest.getJsonDirections(new LatLng(50.126922, 19.015261), new LatLng(50.200206, 19.175603), TravelMode.DRIVING)
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<String, Routes>() {
                    @Override
                    public Routes call(String s) {
                        return new RouteJsonParser<Routes>().parse(s, Routes.class);
                    }
                })
                .subscribe(new Action1<Routes>() {
                    @Override
                    public void call(Routes r) {
                        routeDrawer.drawPath(r);
                    }
                });

    }


    @Override
    public Observable<String> getJsonDirections(LatLng latLng, LatLng latLng1, TravelMode travelMode) {
        return null;
    }



    /*******
     *
     *  Search bar Operation
     *
     * ******/


    @Override
    public boolean onQueryTextSubmit(String location) {
        Toast.makeText(this,location,Toast.LENGTH_SHORT).show();
        if(location!=null && location!="")
        {
            geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location,1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        //Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
        return false;
    }



    /******
     *
     *  Menu Settings
     *
     * ****/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.Search);
        searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setQueryHint("Search Location");
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        Intent p;
        switch (item.getItemId()) {

            case R.id.Search:
                break;

            case R.id.exit:
                finish();
                break;
        }

        return false;
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();
    }
}