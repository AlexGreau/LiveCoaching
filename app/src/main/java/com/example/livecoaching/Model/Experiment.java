package com.example.livecoaching.Model;

import com.example.livecoaching.Communication.Server;
import com.example.livecoaching.Logs.Logger;

public class Experiment {
    private Server server;
    private Logger logger;

    // values
    private String participantID;
    private int indexInTrials;
    private Logger simpleLogger;
    private RouteCalculator routeCalculator;


    public Experiment(String participantID, Logger simpleLogger) {
        this.participantID = participantID;
        this.simpleLogger = simpleLogger;
        this.indexInTrials = 0;
    }

    public void run(){
        server = new Server(this);
    }

    public void stop(){
        server.setRunning(false);

    }

    public void runNextTrial(){
        this.indexInTrials ++;
        initTrial();
    }

    public void initTrial(){
        // build all the different trials here
        // 3 difficulties x 3 interaction Types x 4 tries
        if (indexInTrials == 1){

        }
    }

    // get and set
    public Logger getLogger(){
        return this.logger;
    }

}
