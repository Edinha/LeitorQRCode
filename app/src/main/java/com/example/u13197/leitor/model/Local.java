package com.example.u13197.leitor.model;

import android.location.Location;

/**
 * Created by u13197 on 30/11/2015.
 */
public class Local {

    private Location location;
    private String text;

    public Local() {
        this.location = new Location("reverseGeocoded");
    }

    public Local(Location location,String text) {
        this.text = text;
        this.location = location;

    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setLocationLongitude(double longitude) {
        this.location.setLongitude(longitude);

    }


    public void setLocationLatitude(double latitude) {
        this.location.setLatitude(latitude);
    }

    public void setText(String text) {

        this.text = text;
    }

    public Location getLocation() {
        return this.location;
    }

    public String getText() {
        return this.text;
    }

}
