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

    public RouteCalculator(Location location, int difficulty) {
        startingPoint = location;
        route = new ArrayList<Location>();
        route.add(startingPoint);
        if (difficulty == 0){
            getRouteI();
        } else if (difficulty == 1){
            getRouteL();
        } else if (difficulty == 2){
            getRouteN();
        }
    }

    public void initTestLocations(){
        // library : 1.340482, 103.962946
        library = new Location(LocationManager.GPS_PROVIDER);
        library.setLatitude(1.340482);
        library.setLongitude(103.962946);
    }

    public Location getStartingPoint(){
        return startingPoint;
    }

    public void setStartingPoint( Location loc) {
        this.route.clear();
        this.startingPoint = loc;
        this.route.add(loc);
    }

    // different shapes of routes
    public ArrayList <Location> getRouteL(){
        // 3 criticals points, L shaped with a turn of random angle
        if (route.size() == 1){
            double firstDistance = 30.0;
            double secondDistance = 10.0;
            double turnAngle = 45.0;
            double startAngle = 10.0;
            Location firstCP = calculateLocation(startingPoint, applyMagic(firstDistance), startAngle);
            route.add(firstCP);
            Location secondCP = calculateLocation(firstCP,applyMagic(secondDistance), turnAngle);
            route.add(secondCP);
        }
        return route;
    }

    public ArrayList<Location> getRouteI(){
        // 2 critical points, distance of x meters
        if(route.size() < 2 ){
            double distWanted = 20.0;
            double angleWanted = 90.0;
            Location nextLoc = calculateLocation(startingPoint, applyMagic(distWanted), angleWanted);
            route.add(nextLoc);
            Log.d(TAG, "Distance test : " + distanceTest(route.get(0),route.get(1), distWanted) );
        }
        return route;
    }

    public ArrayList<Location> getRouteN(){
        if (route.size() < 2){
            double firstDistance = 10;
            double firstAngle = 135.0;
            double secondDistance = 20;
            double secondAngle = 225.0;
            Location firstLoc = calculateLocation(startingPoint,applyMagic(firstDistance), firstAngle);
            Location secondLoc = calculateLocation(firstLoc, applyMagic(secondDistance), secondAngle);
            route.add(firstLoc);
            route.add(secondLoc);
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

    private double applyMagic(double d){
        return d/4.37;
    }
}
