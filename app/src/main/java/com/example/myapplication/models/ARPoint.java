package com.example.myapplication.models;

import android.location.Location;

public class ARPoint {
    Location location;
    String name;
    double distance;
    String adress;

    public ARPoint(String name, double lat, double lon,double distance,String adress) {
        this.name = name;
        this.distance = distance;
        this.adress = adress;
        location = new Location("ARPoint");
        location.setLatitude(lat);
        location.setLongitude(lon);

    }
    public String getAdress(){ return adress;}
    public double getdistanceatm(){
        return distance;
    }

    public Location getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }
}
