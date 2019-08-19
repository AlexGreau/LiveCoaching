package com.example.livecoaching.Model;

import com.example.livecoaching.Communication.Server;
import com.example.livecoaching.Logs.Logger;

public class Trial {
    private Server server;

    private int interactionType;
    private int trajectory;

    // logs
    private Logger logger;

    private String participantID;
    private Double totalTime;
    private Double totalDistanceParcourue;
    private Double totalDistanceTheorique;

    public Trial(String ID, int interactionType, Logger logger) {
        this.logger = logger;
        this.participantID = ID;
        this.interactionType = interactionType;

        this.totalTime = 0.0;
        this.totalDistanceParcourue = 0.0;
        this.totalDistanceTheorique = 0.0;

        init();
    }

    public void init(){
        logger.initNewLog(participantID, getInteractionString(interactionType));
    }


    public void run() {
        server = new Server(this);

    }

    public void stop() {

    }

    public Logger getLogger() {
        return this.logger;
    }

    public String getInteractionString(int type){
        String res;
        if (type == 1){
            res = "haptic";
        } else if (type == 2){
            res = "visual";
        } else {
            res = "haptic and visual";
        }

        return res;
    }
}
