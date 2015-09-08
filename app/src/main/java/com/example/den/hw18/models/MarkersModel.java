package com.example.den.hw18.models;

/**
 * Created by Den on 08.09.15.
 */
public class MarkersModel {
    private String _id;
    private String latitude;
    private String longitude;
    private String text;
    private String filePath;

    public MarkersModel() {
    }

    public MarkersModel(String latitude, String longitude, String text, String filePath) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.text = text;
        this.filePath = filePath;
    }

    public MarkersModel(String id, String latitude, String longitude, String text, String filePath) {
        this._id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.text = text;
        this.filePath = filePath;
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

    public String getFilePath() {
        return filePath;
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

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
