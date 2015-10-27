package com.example.sifat.Domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Sifat on 10/22/2015.
 */
public class TaxiDetail implements Serializable{

    int driverId;
    String driverName, mobile;
    double latitude,longitude;
    float rating;

    public TaxiDetail(){}

    public TaxiDetail(int id, String driverName, double latitude, double longitude, float rating, String mobile)
    {
        this.driverId = id;
        this.driverName=driverName;
        this.latitude=latitude;
        this.longitude=longitude;
        this.rating = rating;
        this.mobile = mobile;
    }

    public String getDriverName()
    {
        return this.driverName;
    }

    public double getLatitude()
    {
        return this.latitude;
    }

    public double getLongitude()
    {
        return this.longitude;
    }

    public float getRating() {
        return this.rating;
    }

    public int getDriverId() {
        return this.driverId;
    }

    public String getMobile() {
        return this.mobile;
    }
}
