package com.example.den.hw18.utils;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Den on 08.09.15.
 */
public class DialogUtils {

    private String myLocation;

    public void callShowMyLocationDialog(Context context, DialogFragment dialog, FragmentManager manager, double lat, double longt){
        Bundle bundle = new Bundle();
        bundle.putString("location", getInfoAboutMyLocation(context, lat, longt));
        bundle.putString("latitude", String.valueOf(lat));
        bundle.putString("longitude", String.valueOf(longt));
        dialog.setArguments(bundle);
        dialog.show(manager, "dialog");
    }

    private String getInfoAboutMyLocation(Context context, double lat, double longt) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(lat,longt, 1);
            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();

            myLocation =
                            "Country: "    + country     + "\n" +
                            "State: "      + state       + "\n" +
                            "City: "       + city        + "\n" +
                            "Adress: "     + address     + "\n" ;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return myLocation;
    }

}
