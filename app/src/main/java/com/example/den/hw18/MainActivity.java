package com.example.den.hw18;

import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import com.example.den.hw18.adapters.MyInfoWindowAdapter;
import com.example.den.hw18.callbacks.CallbackAddMarker;
import com.example.den.hw18.dialogs.AddNewMarkerDialog;
import com.example.den.hw18.dialogs.ShowMyLocationDialog;
import com.example.den.hw18.utils.DialogUtils;
import com.example.den.hw18.utils.MyLocationListener;
import com.example.den.hw18.utils.PlaceMarkerUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        OnMapReadyCallback, GoogleMap.OnMapClickListener, View.OnClickListener,
        CallbackAddMarker, GoogleMap.OnInfoWindowClickListener{

    private GoogleMap mMap;
    private LocationManager mLocationManager;
    private MyLocationListener mMyLocationListener;
    private ImageButton mBtnWhereIAm;
    private Location mLocation;
    private DialogFragment mDialog;
    private LatLng mLatLng;

    private List<Bitmap> bitmapList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mMyLocationListener = new MyLocationListener(this);

        mBtnWhereIAm = (ImageButton) findViewById(R.id.btnWhereIAm);

        mBtnWhereIAm.setOnClickListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mapSettings();

        moveToMyLocation();

        mMap.setInfoWindowAdapter(new MyInfoWindowAdapter(getLayoutInflater(), bitmapList));

        mapListeners();
    }

    @Override
    public void onMapClick(LatLng latLng) {
            mDialog = new AddNewMarkerDialog();
            mDialog.show(getFragmentManager(), "dialog");
            this.mLatLng = latLng;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnWhereIAm:
                mDialog = new ShowMyLocationDialog();

                DialogUtils dialogUtils = new DialogUtils();
                dialogUtils.callShowMyLocationDialog(
                        getApplicationContext(),
                        mDialog, getFragmentManager(),
                        mLocation.getLatitude(),
                        mLocation.getLongitude());

                break;
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        mLocationManager.requestLocationUpdates(getBestProvider(), 0, 0, mMyLocationListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocationManager.removeUpdates(mMyLocationListener);
    }

    @Override
    public void addMarker(String txt, String path) {
        PlaceMarkerUtils placeMarkerUtils = new PlaceMarkerUtils();
        placeMarkerUtils.placeMarker(mMap, bitmapList, mLatLng, path, txt);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        marker.hideInfoWindow();
    }

    public void setMyLocation(Location location) {
        this.mLocation = location;
    }

    public String getBestProvider(){

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(false);

        return mLocationManager.getBestProvider(criteria, true);

    }

    private void mapSettings() {
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        mMap.setMyLocationEnabled(true);
    }

    private void mapListeners() {
        mMap.setOnMapClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
    }

    private void moveToMyLocation() {
        Location loc = mLocationManager.getLastKnownLocation(getBestProvider());
        if (loc != null) {
            mMap.moveCamera(CameraUpdateFactory
                    .newLatLngZoom(new LatLng(loc.getLatitude(), loc.getLongitude()), 4));
        }
    }

}
