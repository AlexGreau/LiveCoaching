package com.example.livecoaching.Model;

import android.location.Location;
import android.location.LocationManager;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RouteCalculator {

    private Location startingPoint;
    private ArrayList<Location> route;

    // for test purposes
    private Location library;
    private Location Mrt;

    public RouteCalculator(Location location) {
        startingPoint = location;
        route = new ArrayList<Location>();
        route.add(startingPoint);
        initTestLocations();
    }

    public Location getStartingPoint(){
        return startingPoint;
    }

    public void initTestLocations(){
        // library : 1.340482, 103.962946
        library = new Location(LocationManager.GPS_PROVIDER);
        library.setLatitude(1.340482);
        library.setLongitude(103.962946);
        // mrt
    }

    public void setStartingPoint( Location loc) {
        this.route.clear();
        this.startingPoint = loc;
        this.route.add(loc);
    }

    // different shapes of routes
    public ArrayList <Location> getRouteL(){
        // calculate the route
        return route;
    }

    public ArrayList<Location> getRouteI(){
        // 2 critical points, straight line, distance of 10 meters
        route.add(library);

        return route;
    }

    public ArrayList<Location> getActualRoute(){
        return route;
    }
}
