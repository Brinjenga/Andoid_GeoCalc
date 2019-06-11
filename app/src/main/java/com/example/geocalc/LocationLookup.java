package com.example.geocalc;

import org.parceler.Parcel;

import java.util.Date;

@Parcel
public class LocationLookup {
    String _key;
    double origLat;
    double origLng;
    double endLat;
    double endLng;
    String timestamp;


    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String get_key() {
        return _key;
    }

    public void set_key(String _key) {
        this._key = _key;
    }

    public double getOrigLat() {
        return origLat;
    }

    public void setOrigLat(double origLat) {
        this.origLat = origLat;
    }

    public double getOrigLng() {
        return origLng;
    }

    public void setOrigLng(double origLng) {
        this.origLng = origLng;
    }

    public double getEndLat() {
        return endLat;
    }

    public void setEndLat(double endLat) {
        this.endLat = endLat;
    }

    public double getEndLng() {
        return endLng;
    }

    public void setEndLng(double endLng) {
        this.endLng = endLng;
    }
}
