package com.example.livecoaching.Model;

import com.example.livecoaching.Logs.Logger;

import java.util.ArrayList;

public class Experiment{

    // values
    private String participantID;
    private int indexInTrials;
    private Logger logger;
    private RouteCalculator routeCalculator;
    private int interactionType;
    private int difficulty;

    private ArrayList<Trial> trials;


    public Experiment(String participantID, Logger simpleLogger) {
        this.participantID = participantID;
        this.logger = simpleLogger;
        this.indexInTrials = 0;
        initTrials();
    }

    public void run(){
        trials.get(0).run();
    }

    public void runNextTrial(){
        this.indexInTrials ++;
        initTrials();
    }

    public void initTrials(){
        trials = new ArrayList<>();
        // test for now
        interactionType = 1;
        difficulty = 1;
        Trial trialTest = new Trial(participantID, interactionType,difficulty,this.logger);
        trials.add(trialTest);


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
