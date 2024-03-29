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
            double firstDistance = 10.0;
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
            double distWanted = 10.0;
            double angleWanted = 90.0;
            Location nextLoc = calculateLocation(startingPoint, applyMagic(distWanted), angleWanted);
            route.add(nextLoc);
        }
        return route;
    }

    public ArrayList<Location> getRouteN(){
        if (route.size() < 2){
            double firstDistance = 10;
            double firstAngle = 90.0;
            double secondDistance = 20;
            double secondAngle = 135;
            double thirdDistance = 10;
            double thirdAngle = 225;
            Location firstLoc = calculateLocation(startingPoint,applyMagic(firstDistance), firstAngle);
            Location secondLoc = calculateLocation(firstLoc, applyMagic(secondDistance), secondAngle);
            Location thirdLoc = calculateLocation(secondLoc,applyMagic(thirdDistance), thirdAngle);
            route.add(firstLoc);
            route.add(secondLoc);
            route.add(thirdLoc);
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
