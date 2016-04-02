package com.example.sifat.gobar;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sifat.Controller.ServerCommunicator;
import com.example.sifat.Custom.CustomMapFragmment;
import com.example.sifat.Dialogues.DriverRating;
import com.github.polok.routedrawer.RouteApi;
import com.github.polok.routedrawer.RouteDrawer;
import com.github.polok.routedrawer.RouteRest;
import com.github.polok.routedrawer.model.Routes;
import com.github.polok.routedrawer.model.TravelMode;
import com.github.polok.routedrawer.parser.RouteJsonParser;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.example.sifat.Utilities.CommonUtilities.*;


/**
 * Created by Sifat on 10/27/2015.
 */
public class UserTaxiStatus extends ActionBarActivity implements RouteApi,
        CustomMapFragmment.OnTouchListener,
        OnMapReadyCallback,
        DriverRating.Communicator,
        View.OnClickListener {

    private GoogleMap gMap;
    private UiSettings uiSettings;
    private CameraPosition showMyLocation;
    private LatLng srcLatLng, distLatLng;
    private Bundle bundle;
    private Marker srcMarker, distMarker;
    private TextView tvDrivername, tvDriverMobile;
    private RatingBar rbDriverRate;
    private float rating, lat, lng;
    private Toolbar toolbar;
    private String driverId;
    private DriverRating driverRating;
    private String driverName;
    private FloatingActionButton fbReleaseTaxi,fbWaitingState;
    private NotificationManager mNotificationManager;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ServerCommunicator serverCommunicator;
    private boolean isOnride;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_taxi_status);
        init();
        bundle = getIntent().getExtras();

        lat = Float.parseFloat(bundle.getString(SRC_LAT));
        lng = Float.parseFloat(bundle.getString(SRC_LNG));

        srcLatLng = new LatLng(lat, lng);

        lat = Float.parseFloat(bundle.getString(DIST_LAT));
        lng = Float.parseFloat(bundle.getString(DIST_LNG));

        distLatLng = new LatLng(lat, lng);

        driverName = bundle.getString(SELECTED_DRIVER_NAME);
        tvDrivername.setText(driverName);
        tvDriverMobile.setText(bundle.getString(SELECTED_DRIVER_MOBILE));
        rating = Float.parseFloat(bundle.getString(SELECTED_DRIVER_RATING));
        driverId = bundle.getString(SELECTED_DRIVER_ID);
        rbDriverRate.setRating(rating + 0.5f);
        rbDriverRate.setRating(rating);

        CustomMapFragmment customMapFragmment =
                (CustomMapFragmment) getFragmentManager().findFragmentById(R.id.map);
        customMapFragmment.setListener(this);
        customMapFragmment.getMapAsync(this);
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        sharedPreferences = getSharedPref(this);
        editor = sharedPreferences.edit();

        tvDriverMobile = (TextView) findViewById(R.id.tvDriverMobileNum);
        tvDrivername = (TextView) findViewById(R.id.tvDriverName);
        rbDriverRate = (RatingBar) findViewById(R.id.rbDriverRate);
        driverRating = new DriverRating();

        fbReleaseTaxi = (FloatingActionButton) findViewById(R.id.fbReleaseTaxi);
        fbReleaseTaxi.setOnClickListener(this);

        fbWaitingState = (FloatingActionButton) findViewById(R.id.fbWaitingState);
        fbWaitingState.setOnClickListener(this);

        serverCommunicator = new ServerCommunicator(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isOnride = sharedPreferences.getBoolean(IS_ON_RIDE,false);

        if(isOnride)
        {
            fbReleaseTaxi.setVisibility(View.VISIBLE);
            fbWaitingState.setVisibility(View.INVISIBLE);
        }
        else
        {
            fbReleaseTaxi.setVisibility(View.INVISIBLE);
            fbWaitingState.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        uiSettings = gMap.getUiSettings();
        mapUiSetting(true);
        zoomToMyLocation();
        setSrcDistMarker();
        getRoute(srcLatLng, distLatLng);
    }

    private void setSrcDistMarker() {
        srcMarker = gMap.addMarker(new MarkerOptions()
                .position(srcLatLng)
                .title("Starting Point")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.srcmarker)));
        distMarker = gMap.addMarker(new MarkerOptions()
                .position(distLatLng)
                .title("Destination Point")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.distmarker)));


    }

    private void zoomToMyLocation() {
        showMyLocation = new CameraPosition.Builder().target(srcLatLng)
                .zoom(15.5f)
                .bearing(340)
                .tilt(50)
                .build();
        gMap.animateCamera(CameraUpdateFactory.newCameraPosition(showMyLocation), 2000, null);
    }


    //UI settings of map
    private void mapUiSetting(boolean flag) {
        uiSettings.setZoomControlsEnabled(flag);
        uiSettings.setCompassEnabled(flag);
        uiSettings.setMyLocationButtonEnabled(flag);
        uiSettings.setScrollGesturesEnabled(flag);
        uiSettings.setZoomGesturesEnabled(flag);
        uiSettings.setTiltGesturesEnabled(flag);
        uiSettings.setRotateGesturesEnabled(flag);
    }


    /********
     * Get A route Between source & destination
     ********/
    private void getRoute(LatLng srcLatLng, LatLng distLatLng) {
        final RouteDrawer routeDrawer = new RouteDrawer.RouteDrawerBuilder(gMap)
                .withColor(Color.BLUE)
                .withWidth(5)
                .withAlpha(0.0f)
                .withMarkerIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                .build();

        RouteRest routeRest = new RouteRest();
        routeRest.getJsonDirections(srcLatLng, distLatLng, TravelMode.DRIVING)
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
    public void onCusTouchUp() {

    }

    @Override
    public void onCusTouchDown() {

    }

    @Override
    public Observable<String> getJsonDirections(LatLng latLng, LatLng latLng1, TravelMode travelMode) {
        return null;
    }

    @Override
    public void RatingDialog() {

        serverCommunicator.endRide(rating,driverId);
        mNotificationManager.cancel(NOTIFICATION_ID);
        finish();
        //doBkash();
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id==R.id.fbReleaseTaxi)
            driverRating.show(getFragmentManager(), "Rate the Driver");
        else if(id==R.id.fbWaitingState){
            fbReleaseTaxi.setVisibility(View.VISIBLE);
            fbWaitingState.setVisibility(View.INVISIBLE);
            serverCommunicator.startRide(driverId);
        }
    }


    public void doBkash() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("bKash:");
        alertDialog.setMessage("Please bKash tk 10.00 to 01xxxxxxxxx");
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                editor.putString(SELECTED_DRIVER_NAME, "");
                editor.putInt(SELECTED_DRIVER_ID, -1);
                editor.putBoolean(IS_ON_HIRE, false);
                editor.commit();

                dialog.cancel();
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:*247" + Uri.encode("#")));
                if (ActivityCompat.checkSelfPermission(UserTaxiStatus.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
                mNotificationManager.cancel(NOTIFICATION_ID);
                finish();
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(UserTaxiStatus.this, "Cancel", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });
        alertDialog.show();
    }
}