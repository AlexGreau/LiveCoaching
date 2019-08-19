package com.example.livecoaching.Model;

import android.location.Location;
import android.location.LocationManager;

import com.example.livecoaching.Communication.Server;
import com.example.livecoaching.Interfaces.Decoder;
import com.example.livecoaching.Logs.Logger;

import java.util.ArrayList;

public class Trial implements Decoder {
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

    public Trial(String ID, int interactionType, int difficulty, Logger logger) {
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
        logger.initNewLog(participantID, getInteractionString(interactionType));
        actualLocation = new Location(LocationManager.GPS_PROVIDER);
        initRouteCalculator();
    }

    public void initRouteCalculator() {

    }


    public void run() {
        server = new Server(this);

    }

    public void stop() {
        server.setRunning(false);
    }

    @Override
    public String decodeMessage(String msg) {
        String replyMsg = "";

        // split message
        String[] parts = msg.split(":");
        String senderState = parts[0];
        // interpret results
        if (senderState.equals("Ready")) {
            replyMsg = "continue:" + 2;
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
            stopLogging();
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
        routeCalculator = new RouteCalculator(loc, 0);
        // routeCalculator.getRouteI();
        System.out.println("route : " + routeCalculator.getActualRoute());
    }

    public Logger getLogger() {
        return this.logger;
    }

    public String getInteractionString(int type) {
        String res;
        if (type == 1) {
            res = "haptic";
        } else if (type == 2) {
            res = "visual";
        } else {
            res = "haptic and visual";
        }

        return res;
    }
}
