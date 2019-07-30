package com.example.livecoaching.Model;

import android.location.Location;

import java.util.ArrayList;

public class RouteCalculator {

    private Location startingPoint;
    private ArrayList<Location> route;

    public RouteCalculator(Location location) {
        startingPoint = location;
        route = new ArrayList<Location>();
        route.add(startingPoint);
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
        // calculate the route
        return route;
    }

    public ArrayList<Location> getRouteI(){
        // 2 critical points, straight line, distance of 10 meters

        return route;
    }
}
