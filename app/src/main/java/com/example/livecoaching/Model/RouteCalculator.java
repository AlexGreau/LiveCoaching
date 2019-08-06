package com.example.livecoaching.Model;

import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import java.util.ArrayList;

public class RouteCalculator {

    private final String TAG = "RouteCalculator";

    private Location startingPoint;
    private ArrayList<Location> route;

    // for test purposes
    private Location library;

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
        // 2 critical points, distance of x meters
        if(route.size() < 2 ){
            double distWanted = 20.0;
            double angleWanted = 90.0;
            Location nextLoc = calculateLocation(startingPoint, distWanted / 4.37, angleWanted); // rectif de 4.37 par magie
            route.add(nextLoc);
            Log.d(TAG, "route : " + route);
            Log.d(TAG, "Distance test : " + distanceTest(route.get(0),route.get(1), distWanted) );
        }
        return route;
    }

    public boolean distanceTest(Location dep, Location arr, double distSupposee){
        Log.d(TAG,"dist reelle : " + dep.distanceTo(arr) + "; angle reel : " + dep.bearingTo(arr));
        return dep.distanceTo(arr) == distSupposee;
    }

    public Location calculateLocation(Location start, double distance, double angle){
        Location res = new Location (LocationManager.GPS_PROVIDER);
        double dx = distance * Math.sin(Math.toRadians(angle));
        double dy = distance * Math.cos(Math.toRadians(angle));
        double deltaLong = dx / (Math.cos(start.getLatitude()) * 111320);
        double deltaLat = dy / 111320;

        res.setLatitude(start.getLatitude() + deltaLat);
        res.setLongitude(start.getLongitude() + deltaLong);

        return res;
    }

    public ArrayList<Location> getActualRoute(){
        return route;
    }
}
