package com.example.den.hw18.models;

import android.net.Uri;

/**
 * Created by Den on 08.09.15.
 */
public class MarkersModel {
    private String _id;
    private String latitude;
    private String longitude;
    private String text;
    private Uri uri;

    public MarkersModel() {
    }

    public String getId() {
        return _id;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getText() {
        return text;
    }

    public Uri getUri() {
        return uri;
    }

    public void setId(String id) {
        this._id = id;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
