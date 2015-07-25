package com.derricklockwood.isucyrideapp.data.models;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by Derrick Lockwood on 7/24/15.
 */
public class StopLocation implements Serializable{
    private double latitude;
    private double longitude;
    public StopLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LatLng getLatLng() {
        return new LatLng(latitude, longitude);
    }
}
