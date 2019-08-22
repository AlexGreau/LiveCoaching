package com.example.livecoaching.Model;

import android.location.Location;
import android.location.LocationManager;

import com.example.livecoaching.Communication.Server;
import com.example.livecoaching.Interfaces.TrialOrganiser;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Trial {

    private TrialOrganiser organiser;
    private Server server;
    private RouteCalculator routeCalculator;

    private int interactionType;
    private int difficulty;

    // data
    private long startingTime;
    private Location actualLocation;
    private String participantID;
    private long totalTime;
    private Double totalDistanceParcourue;
    private Double totalDistanceTheorique;
    private boolean success;


    public Trial(String ID, int interactionType, int difficulty, TrialOrganiser organiser) {
        this.organiser = organiser;

        this.participantID = ID;
        this.interactionType = interactionType;
        this.difficulty = difficulty;

        this.totalTime = 0;
        this.totalDistanceParcourue = 0.0;
        this.totalDistanceTheorique = 0.0;

        init();
    }

    public void init() {
        actualLocation = new Location(LocationManager.GPS_PROVIDER);
    }

    public void stop() {
        organiser.launchNextTrial();
    }

    public Location parseInfos(String str) {
        String[] infos = str.split("-");
        float lat = Float.parseFloat(infos[0]);
        float longi = Float.parseFloat(infos[1]);
        calculateRealTotalDistance(lat,longi);
        actualLocation.setLatitude(lat);
        actualLocation.setLongitude(longi);
        return actualLocation;
    }
    public void initRouteCalculator(Location loc) {
        routeCalculator = new RouteCalculator(loc, difficulty);
        System.out.println("route : " + routeCalculator.getActualRoute());
    }

    public String getInteractionString(int type) {
        String res;
        if (type == 0) {
            res = "haptic";
        } else if (type == 1) {
            res = "visual";
        } else {
            res = "haptic and visual";
        }

        return res;
    }

    public void calculateTheoricDistance(){
        if (routeCalculator.getActualRoute().size() <2){
            return;
        } else {
            for (int i = 1; i < routeCalculator.getActualRoute().size(); i++){
                Location a = routeCalculator.getActualRoute().get(i-1);
                Location b = routeCalculator.getActualRoute().get(i);
                totalDistanceTheorique += a.distanceTo(b);
            }
        }

        this.totalDistanceTheorique = round(this.totalDistanceTheorique,4);
    }

    public void calculateRealTotalDistance(float lat, float longi){
        Location newLocation = new Location(LocationManager.GPS_PROVIDER);
        newLocation.setLatitude(lat);
        newLocation.setLongitude(longi);
        totalDistanceParcourue += round (actualLocation.distanceTo(newLocation),4);

        this.totalDistanceParcourue = round(this.totalDistanceParcourue,4);
    }

    public static double round(double value, int precision) {
        if (precision < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(precision, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public void calculateTotalTimeUntil( long time){
        this.totalTime = (time - this.startingTime);
    }

    public void setStartingTime(long t){
        this.startingTime = t;
    }

    public long getStartingTime(){
        return this.startingTime;
    }

    public void setSuccess(boolean isSuccessful){
        success = isSuccessful;
    }

    public boolean getSuccess(){
        return success;
    }

    public double getTheoricDistance() {
        return totalDistanceTheorique;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalDistanceParcourue(double d) {
        this.totalDistanceParcourue = d;
    }

    public void setTotalDistanceTheorique(double d) {
        this.totalDistanceTheorique = d;
    }

    public double getTotalDistance() {
        return totalDistanceParcourue;
    }

    public int getInteractionType() {
        return interactionType;
    }

    public void setInteractionType(int interactionType) {
        this.interactionType = interactionType;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public Location getActualLocation() {
        return actualLocation;
    }

    public void setActualLocation(Location actualLocation) {
        this.actualLocation = actualLocation;
    }

    public String getParticipantID() {
        return participantID;
    }

    public void setParticipantID(String participantID) {
        this.participantID = participantID;
    }

    public RouteCalculator getRouteCalculator() {
        return this.routeCalculator;
    }
}
