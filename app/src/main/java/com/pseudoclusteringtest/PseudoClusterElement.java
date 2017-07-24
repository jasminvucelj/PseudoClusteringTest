package com.pseudoclusteringtest;


import android.location.Location;

import java.util.ArrayList;
import java.util.List;

public class PseudoClusterElement {
    private List<Location> locationCluster;
    private Location meanLocation;


    PseudoClusterElement(Location location){
        locationCluster = new ArrayList<>();
        locationCluster.add(location);
        meanLocation = new Location("");
        meanLocation.setLatitude(location.getLatitude());
        meanLocation.setLongitude(location.getLongitude());
    }



    void add(Location location) {
        locationCluster.add(location);
        meanLocation = findMeanLocation(meanLocation, location);
    }


    int size() {
        return locationCluster.size();
    }

    List<Location> getLocations() {
        return locationCluster;
    }


    Location getMeanLocation() {
        return meanLocation;
    }


    private static Location findMeanLocation(Location location1, Location location2) {
        Location location = new Location("");
        location.setLatitude((location1.getLatitude() + location2.getLatitude()) / 2.0);
        location.setLongitude((location1.getLongitude() + location2.getLongitude()) / 2.0);
        return location;
    }

}
