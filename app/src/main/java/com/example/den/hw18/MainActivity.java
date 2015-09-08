package com.example.den.hw18;

import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.den.hw18.callbacks.CallbackAddMarker;
import com.example.den.hw18.dialogs.AddNewMarkerDialog;
import com.example.den.hw18.dialogs.ShowMyLocationDialog;
import com.example.den.hw18.utils.DialogUtils;
import com.example.den.hw18.utils.MyLocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        OnMapReadyCallback, GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMapClickListener, View.OnClickListener,
        CallbackAddMarker, GoogleMap.OnInfoWindowClickListener{

    private GoogleMap mMap;
    private LocationManager mLocationManager;
    private MyLocationListener mMyLocationListener;
    private ImageButton mBtnWhereIAm;
    private Location location;
    public DialogFragment dialog;
    private LatLng latLng;

    private Bitmap bitmap;
    private List<Bitmap> bitmapList = new ArrayList<>();

    int radius;
    float verticalAnchor;

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

        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);

        mMap.setMyLocationEnabled(true);

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);// compass
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        Location loc = mLocationManager.getLastKnownLocation(getBestProvider());
        if (loc != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(loc.getLatitude(), loc.getLongitude()), 4));// move camera to coord + zoom
        }
        mMap.setInfoWindowAdapter(new MyInfoWindowAdapter());

        mMap.setOnInfoWindowClickListener(this);

    }

    @Override
    public void onMapClick(LatLng latLng) {
            dialog = new AddNewMarkerDialog();
            dialog.show(getFragmentManager(),"dialog");
            this.latLng = latLng;
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
//        dialog = new AddNewMarkerDialog();
//        dialog.show(getFragmentManager(),"dialog");
//
//        this.latLng = latLng;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnWhereIAm:
                dialog = new ShowMyLocationDialog();

                DialogUtils dialogUtils = new DialogUtils();
                dialogUtils.callShowMyLocationDialog(
                        getApplicationContext(),
                        dialog,getFragmentManager(),
                        location.getLatitude(),
                        location.getLongitude());

                break;
        }

    }

    public void setMyLocation(Location location) {
        this.location = location;
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
        placeMarker(latLng, path, txt);
    }

    private void placeMarker(LatLng latLng,String filePath, String txt){

        radius = 50;
        int stroke = 3;
        verticalAnchor = 0.944f;

        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bmp = Bitmap.createBitmap(radius, radius + 25, conf);
        Canvas canvas = new Canvas(bmp);
        bitmap = BitmapFactory.decodeFile(filePath);

        bitmap = ThumbnailUtils.extractThumbnail(bitmap, radius - stroke, radius - stroke, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        bitmapList.add(bitmap);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(0xff464646);
        paint.setStyle(Paint.Style.FILL);

        int pointedness = 20;
        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(radius / 2, radius + 15);
        path.lineTo(radius / 2 + pointedness, radius - 10);
        path.lineTo(radius / 2 - pointedness, radius - 10);
        canvas.drawPath(path, paint);

        RectF rect = new RectF(0, 0, radius, radius);
        canvas.drawRoundRect(rect, radius / 2, radius / 2, paint);

        paint.setShader(shader);
        rect = new RectF(stroke, stroke, radius - stroke, radius - stroke);
        canvas.drawRoundRect(rect, (radius - stroke) / 2, (radius - stroke) / 2, paint);

        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title( "\n" + "Latitude: " + latLng.latitude +
                        "\n" + "Longitude: " + latLng.longitude)
                .snippet(txt)
                .icon(BitmapDescriptorFactory.fromBitmap(bmp))
                .anchor(0.5f, verticalAnchor));
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        marker.hideInfoWindow();
    }

    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View mContentView;

        MyInfoWindowAdapter() {
            mContentView = getLayoutInflater().inflate(
                    R.layout.custom_info_window, null);
        }

        @Override
        public View getInfoContents(final Marker marker) {

            TextView txtTitle = ((TextView) mContentView.findViewById(R.id.title));
            txtTitle.setText(marker.getTitle());

            TextView txtSnippet = ((TextView) mContentView.findViewById(R.id.snippet));
            txtSnippet.setText(marker.getSnippet());

            ImageView ivIcon = ((ImageView) mContentView.findViewById(R.id.icon));
            ivIcon.setImageBitmap(bitmapList.get(Integer.parseInt(
                    (new StringBuffer(marker.getId())
                            .deleteCharAt(0)
                            .toString()))));

            return mContentView;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub
            return null;
        }
    }
}
