package com.example.den.hw18;

import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import com.example.den.hw18.adapters.MyInfoWindowAdapter;
import com.example.den.hw18.callbacks.CallbackAddMarker;
import com.example.den.hw18.db.DataBaseHelper;
import com.example.den.hw18.dialogs.AddNewMarkerDialog;
import com.example.den.hw18.dialogs.ShowMyLocationDialog;
import com.example.den.hw18.models.MarkersModel;
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

    private List<Bitmap> mBitmapList = new ArrayList<>();

    private PlaceMarkerUtils placeMarkerUtils = new PlaceMarkerUtils();

    private DataBaseHelper mDataBaseHelper;

    private List<MarkersModel> markersModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDataBaseHelper = new DataBaseHelper(this);

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

        loadMarkersToMap();

        mMap.setInfoWindowAdapter(new MyInfoWindowAdapter(getLayoutInflater(), mBitmapList));

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
        placeMarkerUtils.placeMarker(mMap,
                                     mBitmapList,
                                     mLatLng.latitude,
                                     mLatLng.longitude,
                                     txt,
                                     path);

        mDataBaseHelper.addNewMarker("" + mLatLng.latitude,
                                     "" + mLatLng.longitude,
                                     txt,
                                     path);
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

    private void loadMarkersToMap() {
        markersModelList = mDataBaseHelper.getAllMarkers();

        if(markersModelList.size()>0) {
            for (int i = 0; i < markersModelList.size(); i++) {
                placeMarkerUtils.placeMarker(mMap,
                        mBitmapList,
                        Double.parseDouble(markersModelList.get(i).getLatitude()),
                        Double.parseDouble(markersModelList.get(i).getLongitude()),
                        markersModelList.get(i).getText(),
                        markersModelList.get(i).getFilePath());

                Log.d("LogDB: ", "\n lat: " + (markersModelList.get(i).getLatitude()) + "\n" +
                        "lng: " + (markersModelList.get(i).getLongitude()));
            }
        }
    }
}
