package com.example.den.hw18.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.media.ThumbnailUtils;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

/**
 * Created by Den on 08.09.15.
 */
public class PlaceMarkerUtils {

    public void placeMarker(GoogleMap mMap,
                            List<Bitmap> bitmapList,
                            double latitude,
                            double longitude,
                            String txt,
                            String filePath){

        int radius = 50;
        int stroke = 3;
        float verticalAnchor = 0.944f;

        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bmp = Bitmap.createBitmap(radius, radius + 25, conf);
        Canvas canvas = new Canvas(bmp);
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);

        bitmap = ThumbnailUtils.extractThumbnail(bitmap,
                radius - stroke,
                radius - stroke,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);

        BitmapShader shader = new BitmapShader(bitmap,
                Shader.TileMode.CLAMP,
                Shader.TileMode.CLAMP);

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

        createMarker(mMap, latitude, longitude, txt, verticalAnchor, bmp);
    }

    private void createMarker(GoogleMap mMap, double latitude, double longitude, String txt, float verticalAnchor, Bitmap bmp) {
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude,longitude))
                .title( "\n" + "Latitude: " + latitude +
                        "\n" + "Longitude: " + longitude)
                .snippet(txt)
                .icon(BitmapDescriptorFactory.fromBitmap(bmp))
                .anchor(0.5f, verticalAnchor));
    }

}
