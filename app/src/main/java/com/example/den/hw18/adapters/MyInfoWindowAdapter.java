package com.example.den.hw18.adapters;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.den.hw18.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

/**
 * Created by Den on 08.09.15.
 */
public class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private final View mContentView;
    private List<Bitmap> mBitmapList;

    public MyInfoWindowAdapter(LayoutInflater layoutInflater, List<Bitmap> bitmapList) {
        mContentView = layoutInflater.inflate(
                R.layout.custom_info_window, null);
        this.mBitmapList = bitmapList;
    }

    @Override
    public View getInfoContents(final Marker marker) {

        TextView txtTitle = ((TextView) mContentView.findViewById(R.id.title));
        txtTitle.setText(marker.getTitle());

        TextView txtSnippet = ((TextView) mContentView.findViewById(R.id.snippet));
        txtSnippet.setText(marker.getSnippet());

        ImageView ivIcon = ((ImageView) mContentView.findViewById(R.id.icon));
        ivIcon.setImageBitmap(mBitmapList.get(Integer.parseInt(
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
