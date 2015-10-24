package com.example.sifat.Domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Sifat on 10/22/2015.
 */
public class TaxiDetail implements Serializable{

    String driverName;
    double latitude,longitude;

    public TaxiDetail(){}
    public TaxiDetail(String driverName,double latitude,double longitude)
    {
        this.driverName=driverName;
        this.latitude=latitude;
        this.longitude=longitude;
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
}
