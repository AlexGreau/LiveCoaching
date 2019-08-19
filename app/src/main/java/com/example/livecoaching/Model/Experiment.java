package com.example.livecoaching.Model;

import com.example.livecoaching.Communication.Server;
import com.example.livecoaching.Logs.Logger;

import java.util.ArrayList;

public class Experiment{
    private Server server;
    private Logger logger;

    // values
    private String participantID;
    private int indexInTrials;
    private Logger simpleLogger;
    private RouteCalculator routeCalculator;

    private ArrayList<Trial> trials;


    public Experiment(String participantID, Logger simpleLogger) {
        this.participantID = participantID;
        this.simpleLogger = simpleLogger;
        this.indexInTrials = 0;
        initTrials();
    }

    public void run(){
        trials.get(0).run();
    }

    public void stop(){
        server.setRunning(false);

    }

    public void runNextTrial(){
        this.indexInTrials ++;
        initTrials();
    }

    public void initTrials(){
        trials = new ArrayList<>();
        // test for now
        Trial trialTest = new Trial(this.simpleLogger);
        trials.add(trialTest);


        // build all the different trials here
        // 3 difficulties x 3 interaction Types x 4 tries
        if (indexInTrials == 1){

        }
    }

    // get and set
    public Logger getLogger(){
        return this.simpleLogger;
    }

}
