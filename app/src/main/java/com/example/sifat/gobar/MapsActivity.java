package com.example.sifat.gobar;

import com.example.sifat.Domain.TaxiDetail;
import com.example.sifat.Receiver.TaxiDetailReceiver;
import com.example.sifat.Services.AddressFetcher;
import com.example.sifat.Services.TaxiLocation;
import com.example.sifat.Utilities.LocationProvider;
import com.github.polok.routedrawer.RouteApi;
import com.github.polok.routedrawer.RouteDrawer;
import com.github.polok.routedrawer.RouteRest;
import com.github.polok.routedrawer.model.Routes;
import com.github.polok.routedrawer.model.TravelMode;
import com.github.polok.routedrawer.parser.RouteJsonParser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.ui.PlacePicker;
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
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.os.SystemClock;
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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.example.sifat.Utilities.CommonUtilities.*;


/**
 * This shows how to create a simple activity with a map and a marker on the map.
 */
public class MapsActivity extends ActionBarActivity implements OnMapReadyCallback,
        View.OnClickListener,RouteApi,
        SearchView.OnQueryTextListener,
        GoogleMap.OnCameraChangeListener {

    private GoogleMap mMap;
    private UiSettings mUiSettings;
    private TextView tvAddress;
    private ImageButton btBacktoMyPosition,btNextAction;
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
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    private AddressResultReceiver mAddressResultReceiver;
    private TaxiDetailResultReceiver taxiDetailResultReceiver;
    private ArrayList<TaxiDetail> taxiDetails= new ArrayList<>();
    private ArrayList<Marker> markers=new ArrayList<>();
    private AlarmManager alarmManager;
    private Intent taxiDetailIntent;
    private PendingIntent pendingIntent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        init();

        MapFragment mapFragment =
                (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Starting service for the taxi position
        //startTaxiDetailService();

        startAlarmManager();
    }

    private void init() {
        sharedpreferences = getSharedPreferences(String.valueOf(R.string.sharedPref), Context.MODE_PRIVATE);
        editor=sharedpreferences.edit();
        editor.putBoolean("flag",true);
        editor.putBoolean("init",false);
        editor.commit();
        toolbar= (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        minAccuracy = 5.0f;
        tvAddress= (TextView) findViewById(R.id.tvAddrss);
        btBacktoMyPosition= (ImageButton) findViewById(R.id.ibMyLocation);
        btBacktoMyPosition.setOnClickListener(this);
        btNextAction= (ImageButton) findViewById(R.id.ibNextAction);
        btNextAction.setOnClickListener(this);

        //getMyLocation();

        //initializing My Custom receiver

        mAddressResultReceiver= new AddressResultReceiver(new Handler());
        taxiDetailResultReceiver = new TaxiDetailResultReceiver(new Handler());
        Log.i(LOG_TAG_TAXIPOSITIONSERVICE,"inti receiver");
        //mResultReceiver.setReceiver(this);

    }

    @Override
    public void onClick(View view) {
        if(view.getId()== R.id.ibMyLocation)
        {
            locationProvider.getMyLocaton();
        }
        else if(view.getId()==R.id.ibNextAction)
        {
            int PLACE_PICKER_REQUEST = 1;
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            //PlacePicker.get
            try {
                startActivityForResult(builder.build(MapsActivity.this), PLACE_PICKER_REQUEST);
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        mMap=map;
        mMap.setOnCameraChangeListener(this);
        mMap.setMyLocationEnabled(true);
        mMap.setBuildingsEnabled(true);

        // Keep the UI Settings state in sync with the checkboxes.
        mUiSettings = mMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setCompassEnabled(true);
        mUiSettings.setMyLocationButtonEnabled(true);
        mUiSettings.setScrollGesturesEnabled(true);
        mUiSettings.setZoomGesturesEnabled(true);
        mUiSettings.setTiltGesturesEnabled(true);
        mUiSettings.setRotateGesturesEnabled(true);

        locationProvider = new LocationProvider(this,mMap,editor,sharedpreferences);
        locationProvider.getMyLocaton();

        //getRoute();
        //startAlarmManager();
    }

    /**********
     *
     * Get New LatLong When Camera is changed
     *
     * ********/

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

        boolean flag=sharedpreferences.getBoolean("flag",true);
        boolean initMarker= sharedpreferences.getBoolean("init", true);
        if(!flag && initMarker)
        locationProvider.finish();
        LatLng newLatLng= cameraPosition.target;
        //new LocationAsynctask().execute(newLatLng);
        startLocationIntentService(newLatLng);
        //Toast.makeText(this,getMyLocationAddress(newLatLng),Toast.LENGTH_SHORT).show();

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
    protected void onPause() {
        super.onPause();
        cancelAlarmManager();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationProvider.finish();
        //mGoogleApiClient.disconnect();
    }

    /*****
     *
     * Get My Location From LatLong through starting a service
     *
     ******/


    protected void startLocationIntentService(LatLng latlng) {
        Intent intent = new Intent(this, AddressFetcher.class);
        intent.putExtra(ADDRESS_RECIEVER, mAddressResultReceiver);
        intent.putExtra(LATLNG_DATA_EXTRA, latlng);
        //Toast.makeText(this,"Start",Toast.LENGTH_SHORT).show();
        startService(intent);
    }

    /*protected  void startTaxiDetailService()
    {
        Intent intent = new Intent(this, TaxiLocation.class);
        intent.putExtra(TAXIDETAIL_RECIEVER, taxiDetailResultReceiver);
        //Toast.makeText(this,"Start",Toast.LENGTH_SHORT).show();
        startService(intent);
    }*/

    /*****
     *
     *  Get Resulted address from service
     *
     * ****/

    class AddressResultReceiver extends ResultReceiver {

        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            Log.i(LOG_TAG_SERVICE,"OnRecieve");
            // Display the address string
            // or an error message sent from the intent service.
            String address = resultData.getString(RESULT_ADDRESS_KEY);
            tvAddress.setText(address);

            // Show a toast message if an address was found.
            /*if (resultCode == SUCCESS_RESULT) {
                Toast.makeText(MapsActivity.this,"Address Found",Toast.LENGTH_SHORT).show();
            }*/

        }
    }


    /*****
     *
     *  Get Resulted TaxiDetail Information from service
     *
     * ****/

    class TaxiDetailResultReceiver extends ResultReceiver {

        public TaxiDetailResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            Log.i(LOG_TAG_TAXIPOSITIONSERVICE,"On Recieve");
            // Display the address string
            // or an error message sent from the intent service.
            taxiDetails = (ArrayList<TaxiDetail>) resultData.getSerializable(RESULT_TAXIDETAIL_KEY);

            for(int i=0;i<taxiDetails.size();i++)
            {
                Log.i(LOG_TAG_TAXIPOSITIONSERVICE,"Name: "+taxiDetails.get(i).getDriverName());
            }

            setMarkers(taxiDetails);

            // Show a toast message if an address was found.
            /*if (resultCode == SUCCESS_RESULT) {
                Toast.makeText(MapsActivity.this,"Address Found",Toast.LENGTH_SHORT).show();
            }*/

        }
    }

    /********
     *
     * Add Taxi Marker to map
     *
     * ********/
    void setMarkers(ArrayList<TaxiDetail> taxiDetails)
    {
        Log.i(LOG_TAG_TAXIPOSITIONSERVICE,"Setting marker");
        Marker marker;
        if(markers.size()==0 || markers==null)
        {
            Log.i(LOG_TAG_TAXIPOSITIONSERVICE,"Size "+taxiDetails.size());
            for(int i =0;i<taxiDetails.size();i++)
            {
                Log.i(LOG_TAG_TAXIPOSITIONSERVICE,"Driver name "+taxiDetails.get(i).getDriverName());
                LatLng latlng=new LatLng(taxiDetails.get(i).getLatitude(),taxiDetails.get(i).getLongitude());
                marker = mMap.addMarker(new MarkerOptions().position(latlng).title(taxiDetails.get(i).getDriverName()).flat(true));
                //markers.add(marker);
            }
            //mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
        }
        else
        {
            Log.i(LOG_TAG_TAXIPOSITIONSERVICE,"Size "+markers.size());
            /*for(int i =0;i<taxiDetails.size();i++)
            {
                LatLng latlng=new LatLng(taxiDetails.get(i).getLatitude(),taxiDetails.get(i).getLongitude());
                marker = mMap.addMarker(new MarkerOptions().position(latlng).title(taxiDetails.get(i).getDriverName()).flat(true));
                markers.add(marker);
            }*/
        }
    }


    /*******
     *
     * Starting Alarm Manager
     *
     * ******/

    private void startAlarmManager() {
        Log.i(LOG_TAG_TAXIPOSITIONSERVICE, "startAlarmManager");

        Context context = getBaseContext();
        alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        taxiDetailIntent = new Intent(context, TaxiDetailReceiver.class);
        taxiDetailIntent.putExtra(TAXIDETAIL_RECIEVER, taxiDetailResultReceiver);
        //taxiDetailIntent.putExtra(GMAP_KEY, mMap);
        taxiDetailIntent.putExtra("test", "Oshan");
        pendingIntent = PendingIntent.getBroadcast(context, 0, taxiDetailIntent, 0);
        //pendingIntent.

        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(),
                10000, // 10 sec
                pendingIntent);
    }


    /*******
     *
     * Stoping Alarm Manager
     *
     * ******/
    private void cancelAlarmManager() {
        Log.i(LOG_TAG_TAXIPOSITIONSERVICE, "cancelAlarmManager");

        Context context = getBaseContext();
        //Intent gpsTrackerIntent = new Intent(context, TaxiDetailReceiver.class);
        //PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, gpsTrackerIntent, 0);
        //AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
}