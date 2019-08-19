package com.example.livecoaching.Model;

import android.location.Location;
import android.location.LocationManager;

import com.example.livecoaching.Communication.Server;
import com.example.livecoaching.Interfaces.Decoder;
import com.example.livecoaching.Interfaces.TrialOrganiser;
import com.example.livecoaching.Logs.Logger;

import java.util.ArrayList;

public class Trial implements Decoder {

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

    public void run() {
        server = new Server(this);

    }

    public void stop() {
        //server.setRunning(false);
        stopLogging();
        organiser.launchNextTrial();
    }

    @Override
    public String decodeMessage(String msg) {
        String replyMsg = "";
        // split message
        String[] parts = msg.split(":");
        String senderState = parts[0];
        // interpret results
        if (senderState.equals("Ready")) {
            replyMsg = "continue:" + interactionType;
            if (parts.length >= 2) {
                logger.getLogsArray().clear();
                parseInfos(parts[1]);
                initRouteCalculator(logger.getLogsArray().get(0));
            }
        } else if (senderState.equals("Running")) {
            System.out.println("detected " + senderState);
            replyMsg = "";
            if (parts.length >= 2) {
                parseInfos(parts[1]);
            }
        } else if (senderState.equals("Stop")) {
            System.out.println("detected " + senderState);
            if (parts.length >= 2) {
                parseInfos(parts[1]);
            }
            replyMsg = "";
            stop();
        } else if (senderState.equals("End")) {
            replyMsg = "reset";
        } else if (senderState.equals("Asking")) {
            parseInfos(parts[1]);
            replyMsg = "route:" + format(routeCalculator.getActualRoute());
        }

        return replyMsg;
    }

    private void parseInfos(String str) {
        String[] infos = str.split("-");
        actualLocation.setLatitude(Float.parseFloat(infos[0]));
        actualLocation.setLongitude(Float.parseFloat(infos[1]));
        logger.getLogsArray().add(actualLocation);
        // System.out.println("added location to log : " + actualLocation);
    }

    private void stopLogging() {
        System.out.println("stopping the logging");
        System.out.println(logger.getLogsArray().size());
        logger.flushLogArray();
        logger.readLogFile();
    }

    private String format(ArrayList<Location> locs) {
        // formats the array of location into a sendable message
        String res = "";
        for (Location loc : locs) {
            res += loc.getLatitude() + "-" + loc.getLongitude() + ";";
        }
        return res;
    }

    private void initRouteCalculator(Location loc) {
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
}
