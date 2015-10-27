package com.example.sifat.gobar;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.example.sifat.Custom.CustomMapFragmment;
import com.github.polok.routedrawer.RouteApi;
import com.github.polok.routedrawer.RouteDrawer;
import com.github.polok.routedrawer.RouteRest;
import com.github.polok.routedrawer.model.Routes;
import com.github.polok.routedrawer.model.TravelMode;
import com.github.polok.routedrawer.parser.RouteJsonParser;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Sifat on 10/27/2015.
 */
public class UserTaxiStatus extends ActionBarActivity implements RouteApi,
        CustomMapFragmment.OnTouchListener,
        OnMapReadyCallback {

    private GoogleMap gMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_taxi_status);

        CustomMapFragmment customMapFragmment =
                (CustomMapFragmment) getFragmentManager().findFragmentById(R.id.map);
        customMapFragmment.setListener(this);
        customMapFragmment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
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
}
