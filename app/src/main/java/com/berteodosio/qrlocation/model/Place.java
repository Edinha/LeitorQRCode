package com.berteodosio.qrlocation.model;

import android.location.Location;

public class Place {

    private Location location;
    private String text;

    public Place() {
        this.location = new Location("reverseGeocoded");
    }

    public Place(Location location, String text) {
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
