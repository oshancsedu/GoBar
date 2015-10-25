package com.example.sifat.gobar;

import com.example.sifat.Controller.HttpConnection;
import com.example.sifat.Custom.CustomMapFragmment;
import com.example.sifat.Domain.TaxiDetail;
import com.example.sifat.Parser.PlaceJSONParser;
import com.example.sifat.Receiver.TaxiDetailReceiver;
import com.example.sifat.Services.AddressFetcher;
import com.example.sifat.Services.TaxiLocation;
import com.example.sifat.Utilities.CustomAutoCompleteTextView;
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
import com.google.android.gms.maps.MapView;
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
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
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
        GoogleMap.OnCameraChangeListener,
        GoogleMap.OnMarkerClickListener,
        CustomMapFragmment.OnTouchListener {

    List<Address> addressList;
    private GoogleMap mMap;
    private UiSettings mUiSettings;
    private TextView tvSrcAddress,tvDistAddress;
    private ImageButton btBacktoMyPosition,btNextAction,btDestCancel,btHireAction,btDriverSelectionAction;
    private Geocoder geocoder;
    private Toolbar toolbar;
    private SearchView searchView;
    private LocationProvider locationProvider;
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    private AddressResultReceiver mAddressResultReceiver;
    private TaxiDetailResultReceiver taxiDetailResultReceiver;
    private boolean taxiDetailReceiverResgistered, gettingDist, distSelected, isTapped;
    private ArrayList<TaxiDetail> taxiDetails= new ArrayList<>();
    private ArrayList<Marker> markers=new ArrayList<>();
    private AlarmManager alarmManager;
    private Intent taxiDetailIntent;
    private PendingIntent pendingIntent;
    private LinearLayout hirePanel,destPanel,srcPanel;
    private CustomAutoCompleteTextView customAutoCompleteTextView;
    private HttpConnection httpConnection;
    private LatLng srcLatLng, distLatLng, searchLatLng;
    private Marker srcMarker,distMarker;
    private RelativeLayout mapPanel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        init();

        CustomMapFragmment customMapFragmment =
                (CustomMapFragmment) getFragmentManager().findFragmentById(R.id.map);
        customMapFragmment.setListener(this);
        customMapFragmment.getMapAsync(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Resgistering my custom broadcast receiver
        if (!taxiDetailReceiverResgistered) {
            registerReceiver(taxiDetailResultReceiver, new IntentFilter("taxi.position.information"));
            taxiDetailReceiverResgistered = true;
        }
        startAlarmManager();

    }

    private void init() {
        httpConnection=new HttpConnection();
        sharedpreferences = getSharedPreferences(String.valueOf(R.string.sharedPref), Context.MODE_PRIVATE);
        editor=sharedpreferences.edit();
        editor.putBoolean("flag", true);
        editor.putBoolean("init", false);
        editor.commit();

        //istapped-> so that when user is tapped into the marker & change the camera position service for getting address is not being called unnecessarily
        isTapped = distSelected = gettingDist = false;

        toolbar= (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        //inti TextViews
        tvSrcAddress= (TextView) findViewById(R.id.tvSrcAddrss);
        tvDistAddress=(TextView) findViewById(R.id.tvDistAddrss);

        //inti layouts
        hirePanel= (LinearLayout) findViewById(R.id.llHirePanel);
        hirePanel.setVisibility(View.INVISIBLE);
        destPanel= (LinearLayout) findViewById(R.id.llDistPanel);
        destPanel.setVisibility(View.INVISIBLE);
        srcPanel= (LinearLayout) findViewById(R.id.llSourcePanel);

        //Inti buttons
        btBacktoMyPosition= (ImageButton) findViewById(R.id.ibMyLocation);
        btBacktoMyPosition.setOnClickListener(this);
        btNextAction= (ImageButton) findViewById(R.id.ibNextAction);
        btNextAction.setOnClickListener(this);
        btDestCancel= (ImageButton) findViewById(R.id.ibBacktoSourceSection);
        btDestCancel.setOnClickListener(this);
        btDriverSelectionAction= (ImageButton) findViewById(R.id.ibDriverSelectionAction);
        btDriverSelectionAction.setOnClickListener(this);

        //initializing My Custom receivers
        mAddressResultReceiver= new AddressResultReceiver(new Handler());
        taxiDetailResultReceiver= new TaxiDetailResultReceiver();
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
        mMap.setOnMarkerClickListener(this);

        mUiSettings = mMap.getUiSettings();
        mapUiSetting(true);
        locationProvider = new LocationProvider(this,mMap,editor,sharedpreferences);
        locationProvider.getMyLocaton();
    }

    /**********
     * Get New LatLong When Camera is changed
     * ********/
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

        Log.i(LOG_TAG_TAXIPOSITIONSERVICE,"Camera Changed");
        if(!distSelected)
        {
            boolean flag=sharedpreferences.getBoolean("flag",true);
            boolean initMarker= sharedpreferences.getBoolean("init", true);
            if(!flag && initMarker)
                locationProvider.finish();
            searchLatLng = cameraPosition.target;
            if (!gettingDist)
                srcLatLng = searchLatLng;
            else
                distLatLng = searchLatLng;
            if (!isTapped)
                startLocationIntentService(searchLatLng);
        }
    }


    @Override
    public void onClick(View view) {
        if(view.getId()== R.id.ibMyLocation)
        {
            locationProvider.getMyLocaton();
        }
        else if(view.getId()==R.id.ibNextAction)
        {
            srcMarker = mMap.addMarker(new MarkerOptions()
                    .position(srcLatLng)
                    .title("Starting Point")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.srcmarker)));
            distLatLng = srcLatLng;
            gettingDist = true;
            srcPanel.animate().translationY(srcPanel.getHeight() + 10.0f);

            destPanel.setVisibility(View.VISIBLE);
            destPanel.setAlpha(0.0f);
            destPanel.animate()
                    .alpha(1.0f).setDuration(1000);
            btNextAction.setEnabled(false);
        }
        else if(view.getId()==R.id.ibBacktoSourceSection)
        {
            mMap.animateCamera(CameraUpdateFactory.newLatLng(srcLatLng));
            srcMarker.remove();
            srcPanel.animate().translationY(0.0f);
            destPanel.setVisibility(View.INVISIBLE);
            tvDistAddress.setText("Select an address");
            gettingDist=false;
            //If the destination was selected before it will remove the marker on it too
            if(distSelected)
            {
                distMarker.remove();
                distSelected=false;
            }
            btNextAction.setEnabled(true);
            btDriverSelectionAction.setEnabled(true);
        }

        else if(view.getId()==R.id.ibDriverSelectionAction)
        {
            //Check if the Destination has already been selected
            if(!distSelected)
            {
                if(srcLatLng!=distLatLng)
                {
                    distSelected=true;
                    distMarker = mMap.addMarker(new MarkerOptions()
                            .position(distLatLng)
                            .title("Destination Point")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.distmarker)));
                    getRoute();
                    btDriverSelectionAction.setEnabled(false);
                }
                else
                    Toast.makeText(this,"Choose a different location",Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(this,"Marker Clicked "+marker.getTitle(),Toast.LENGTH_SHORT).show();
        marker.showInfoWindow();
        return true;
    }


    @Override
    public void onCusTouchDown() {
        isTapped = true;
        ////Source & destination panel Navigate up
        srcPanel.animate().translationY(-1.0f * srcPanel.getWidth()).setDuration(1000);
        if (gettingDist)
            destPanel.animate().translationY(-1.0f * destPanel.getWidth()).setDuration(1000);
    }

    @Override
    public void onCusTouchUp() {
        isTapped = false;
        //Source & destination panel Navigate down
        if (gettingDist) {
            destPanel.animate().translationY(0.0f).setDuration(1000);
            srcPanel.animate().translationY(srcPanel.getHeight() + 10.0f).setDuration(1000);
        } else
            srcPanel.animate().translationY(0.0f).setDuration(1000);

        if (!distSelected)
            startLocationIntentService(searchLatLng);
    }

    //UI settings of map
    private void mapUiSetting(boolean flag) {
        mUiSettings.setZoomControlsEnabled(flag);
        mUiSettings.setCompassEnabled(flag);
        mUiSettings.setMyLocationButtonEnabled(flag);
        mUiSettings.setScrollGesturesEnabled(flag);
        mUiSettings.setZoomGesturesEnabled(flag);
        mUiSettings.setTiltGesturesEnabled(flag);
        mUiSettings.setRotateGesturesEnabled(flag);
    }

    /********
     *  Get A route Between source & destination
     * ********/
    private void getRoute()
    {
        final RouteDrawer routeDrawer = new RouteDrawer.RouteDrawerBuilder(mMap)
                .withColor(Color.BLUE)
                .withWidth(5)
                .withAlpha(0.0f)
                .withMarkerIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                .build();

        RouteRest routeRest= new RouteRest();
        routeRest.getJsonDirections(srcLatLng,distLatLng, TravelMode.DRIVING)
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
     * Search bar Operation
     * ******/
    @Override
    public boolean onQueryTextSubmit(String location) {
        if(location!=null && location!="")
        {
            geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location,1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(addressList.size()>0)
            {
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
                mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }
            else
                Toast.makeText(this, location+" not Found", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }



    /******
     *  Menu Settings
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

        //Unresgistering my custom broadcast receiver
        if (taxiDetailReceiverResgistered) {
            unregisterReceiver(taxiDetailResultReceiver);
            taxiDetailReceiverResgistered = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationProvider.finish();
    }

    /*****
     * Get My Location From LatLong through starting a service
     ******/
    protected void startLocationIntentService(LatLng latlng) {
        Intent intent = new Intent(this, AddressFetcher.class);
        intent.putExtra(ADDRESS_RECIEVER, mAddressResultReceiver);
        intent.putExtra(LATLNG_DATA_EXTRA, latlng);
        //Toast.makeText(this,"Start",Toast.LENGTH_SHORT).show();
        startService(intent);
    }

    /********
     * Add Taxi Marker to map
     ********/
    void setMarkers(ArrayList<TaxiDetail> taxiDetails) {
        Marker marker;
        for (int i = 0; i < markers.size(); i++) {
            marker = markers.get(i);
            marker.remove();
            markers.remove(i);
        }


        Log.i(LOG_TAG_TAXIPOSITIONSERVICE, "Setting marker");
        Log.i(LOG_TAG_TAXIPOSITIONSERVICE, "Size " + taxiDetails.size());
        for (int i = 0; i < taxiDetails.size(); i++) {
            Log.i(LOG_TAG_TAXIPOSITIONSERVICE, "Driver name " + taxiDetails.get(i).getDriverName());
            LatLng latlng = new LatLng(taxiDetails.get(i).getLatitude(), taxiDetails.get(i).getLongitude());
            marker = mMap.addMarker(new MarkerOptions()
                    .position(latlng)
                    .title(taxiDetails.get(i).getDriverName())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.taximarker)));
            markers.add(marker);
        }
    }

    /*******
     * Starting Alarm Manager
     ******/
    private void startAlarmManager() {
        Log.i(LOG_TAG_TAXIPOSITIONSERVICE, "startAlarmManager");

        Context context = getBaseContext();
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        taxiDetailIntent = new Intent(context, TaxiDetailReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(context, 0, taxiDetailIntent, 0);

        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(),
                1000 * 60, // 1 min
                pendingIntent);
    }

    /*******
     * Stoping Alarm Manager
     ******/
    private void cancelAlarmManager() {
        Log.i(LOG_TAG_TAXIPOSITIONSERVICE, "cancelAlarmManager");
        alarmManager.cancel(pendingIntent);
    }

    /*****
     *  Get Resulted address from service
     * ****/
    class AddressResultReceiver extends ResultReceiver {

        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            Log.i(LOG_TAG_SERVICE, "OnRecieve");

            // Display the address string
            // or an error message sent from the intent service.
            String address = resultData.getString(RESULT_ADDRESS_KEY);
            if (!gettingDist) {
                tvSrcAddress.setText(address);
            } else {
                tvDistAddress.setText(address);
            }
        }
    }

    /*****
     *  Get Resulted TaxiDetail Information from service
     * ****/
    class TaxiDetailResultReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(LOG_TAG_TAXIPOSITIONSERVICE, "On Recieve");
            // Display the address string
            // or an error message sent from the intent service.
            Bundle bundle;
            bundle = intent.getExtras();
            taxiDetails = (ArrayList<TaxiDetail>) bundle.getSerializable(RESULT_TAXIDETAIL_KEY);
            setMarkers(taxiDetails);
        }
    }
}