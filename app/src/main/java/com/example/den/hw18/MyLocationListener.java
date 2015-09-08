package com.example.den.hw18;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by Den on 07.09.15.
 */
public class MyLocationListener implements LocationListener {

    private MainActivity mainActivity;

    public MyLocationListener(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onLocationChanged(Location location) {
        mainActivity.setMyLocation(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
