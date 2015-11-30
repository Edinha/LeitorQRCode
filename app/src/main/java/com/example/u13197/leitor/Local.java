package com.example.u13197.leitor;

import android.location.Location;

/**
 * Created by u13197 on 30/11/2015.
 */
public class Local {

    private Location location;
    private String text;

    public Local (Location l, String t) {
        this.location = l;
        this.text = t;
    }

    public Location getLocation() {return this.location;}
    public String getText() {return this.text;}

}
