package com.example.livecoaching.Model;

import android.location.Location;
import android.location.LocationManager;

import com.example.livecoaching.Communication.Server;
import com.example.livecoaching.Interfaces.TrialOrganiser;
import com.example.livecoaching.Logs.Logger;

public class Trial {

    private TrialOrganiser organiser;
    private Server server;
    private RouteCalculator routeCalculator;

    private int interactionType;
    private int difficulty;

    // logs
    private Logger logger;

    // data
    private Location actualLocation;
    private String participantID;
    private Double totalTime;
    private Double totalDistanceParcourue;
    private Double totalDistanceTheorique;

    // todo : logSimple()
    // todo : calculate relevant data : total distance, total time

    public Trial(String ID, int interactionType, int difficulty, Logger logger, TrialOrganiser organiser) {
        this.organiser = organiser;
        this.logger = logger;
        this.participantID = ID;
        this.interactionType = interactionType;
        this.difficulty = difficulty;

        this.totalTime = 0.0;
        this.totalDistanceParcourue = 0.0;
        this.totalDistanceTheorique = 0.0;

        init();
    }

    public void init() {
        logger.initNewLog(participantID, getInteractionString(interactionType), difficulty);
        actualLocation = new Location(LocationManager.GPS_PROVIDER);
    }

    public void stop() {
        //server.setRunning(false);
        stopLogging();
        organiser.launchNextTrial();
    }

    public void parseInfos(String str) {
        String[] infos = str.split("-");
        actualLocation.setLatitude(Float.parseFloat(infos[0]));
        actualLocation.setLongitude(Float.parseFloat(infos[1]));
        logger.getLogsArray().add(actualLocation);
        // System.out.println("added location to log : " + actualLocation);
    }

    public void stopLogging() {
        System.out.println("stopping the logging");
        System.out.println(logger.getLogsArray().size());
        logger.flushLogArray();
        logger.readLogFile();
    }

    public void initRouteCalculator(Location loc) {
        routeCalculator = new RouteCalculator(loc, difficulty);
        System.out.println("route : " + routeCalculator.getActualRoute());
    }

    public Logger getLogger() {
        return this.logger;
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
